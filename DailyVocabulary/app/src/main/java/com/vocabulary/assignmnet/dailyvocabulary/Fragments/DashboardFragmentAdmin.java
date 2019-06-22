package com.vocabulary.assignmnet.dailyvocabulary.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabulary.assignmnet.dailyvocabulary.AddNewWordActivity;
import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.FavouriteActivity;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.R;
import com.vocabulary.assignmnet.dailyvocabulary.RecycleViewActivity;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.AppUtils;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.DateUtil;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.SPUtils;
import com.vocabulary.assignmnet.dailyvocabulary.controller.APIHandler;

import java.util.List;
import java.util.Objects;

/**
 * Created by ARU ANJI on 11/28/2016.
 */
public class DashboardFragmentAdmin extends Fragment implements APIHandler.IApi_Response {

    View fragmentView;
    List<DailyWord> dailyWordList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragmnet_dashboard_admin, null);

            loadData();
        }
        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_favorite).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                //Show All Favorite words list in new screen.
                startActivityForResult(new Intent(getActivity(), AddNewWordActivity.class), 1123);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        String last_sync_date = new SPUtils(getActivity()).getValue(SPUtils.LAST_SYNC_DATE);
        if (last_sync_date == null || last_sync_date.isEmpty())
            last_sync_date = "2000-01-01";
        String current_date = DateUtil.getTodayDateString("yyyy-MM-dd");
        new APIHandler(getActivity(), this).getWordsList(last_sync_date, DateUtil.getDateByIncrement(current_date, "yyyy-MM-dd", 7, "yyyy-MM-dd"));
    }

    private void initViews() {
        //initialize Recycle View
        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dailyWordList = DatabaseHandler.getAllWords();
        DateUtil.sortDataLsit_according_to_date(dailyWordList,"yyyy-MM-dd",0);
        recyclerView.setAdapter(new RecycleViewAdapter(dailyWordList));

    }


    class RecycleViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<DailyWord> objectList;

        public RecycleViewAdapter(List<DailyWord> objectList) {
            this.objectList = objectList;
        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_view_admin_word, null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {


            holder.txt_word.setText(objectList.get(position).word);
            holder.date.setText(  DateUtil.convertDateFormat(objectList.get(position).date,"yyyy-MM-dd","dd MMM yyyy") );
            holder.view_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddNewWordActivity.class);
                    intent.putExtra("value",objectList.get(position));
                    startActivityForResult(intent, 1123);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_word;
        public TextView date;
        public View view_row;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_word = (TextView) itemView.findViewById(R.id.txt_word);
            date = (TextView) itemView.findViewById(R.id.txt_date);
            view_row = itemView.findViewById(R.id.view_row);
        }
    }

    @Override
    public void getApi_Response(List<DailyWord> dailyWordList) {
        if (dailyWordList != null && dailyWordList.size() > 0) {
            String current_date = DateUtil.getTodayDateString("yyyy-MM-dd");
            new SPUtils(getActivity()).setValue(SPUtils.LAST_SYNC_DATE, current_date);
        }
        initViews();
    }

    @Override
    public void getApi_Response(boolean is_success, String response_messg) {
        //No Use
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1123 && resultCode == getActivity().RESULT_OK)
        {
            loadData();
        }

    }

}
