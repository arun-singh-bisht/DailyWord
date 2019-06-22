package com.vocabulary.assignmnet.dailyvocabulary.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.FavouriteActivity;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.R;
import com.vocabulary.assignmnet.dailyvocabulary.SplashActivity;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.DateUtil;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.SPUtils;
import com.vocabulary.assignmnet.dailyvocabulary.controller.APIHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ARU ANJI on 11/28/2016.
 */
public class DashboardFragmentUser extends Fragment implements ViewPager.OnPageChangeListener, APIHandler.IApi_Response {

    View fragmentView;
    List<DailyWord> dailyWordList;
    private int shownFragmentPositionInViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragmnet_dashboard_user, null);

            String last_sync_date = new SPUtils(getActivity()).getValue(SPUtils.LAST_SYNC_DATE);
            if (last_sync_date == null || last_sync_date.isEmpty())
                last_sync_date = "2000-01-01";
            String current_date = DateUtil.getTodayDateString("yyyy-MM-dd");

            if (!last_sync_date.equalsIgnoreCase(current_date))
                new APIHandler(getActivity(), this).getWordsList(last_sync_date, current_date);
            else
                initViews();

        }
        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_favorite).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite: {
                //Show All Favorite words list in new screen.
                startActivity(new Intent(getActivity(), FavouriteActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        ViewPager pager = (ViewPager) fragmentView.findViewById(R.id.viewpager);
        pager.setOnPageChangeListener(this);

        List<Fragment> fragments = getFragments();
        MyPageAdapter pageAdapter = new MyPageAdapter(getActivity().getSupportFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(pageAdapter.getCount() - 1);
        fragmentView.findViewById(R.id.img_favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dailyWordList != null) {
                    boolean new_state = !dailyWordList.get(shownFragmentPositionInViewPager).is_added_favourite;

                    setWordFavourite(new_state);
                    dailyWordList.get(shownFragmentPositionInViewPager).is_added_favourite = new_state;
                    //Update word in db
                    DatabaseHandler.updateRecord(dailyWordList.get(shownFragmentPositionInViewPager));
                    //Show toast
                    Toast.makeText(getActivity(), new_state ? "Word is added as favourite" : "Word is no more favourite", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private List<Fragment> getFragments() {
        dailyWordList = DatabaseHandler.getAllWords();
        DateUtil.sortDataLsit_according_to_date(dailyWordList,"yyyy-MM-dd",1);
        List<Fragment> fList = new ArrayList<Fragment>();
        for (int i = 0; i < dailyWordList.size(); i++) {
            fList.add(DailyWordFragment.newInstance(dailyWordList.get(i).word, dailyWordList.get(i).meaning, dailyWordList.get(i).example_1, dailyWordList.get(i).example_2));
        }
        return fList;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        if (dailyWordList != null) {
            setWordFavourite(dailyWordList.get(position).is_added_favourite);
            ((TextView)fragmentView.findViewById(R.id.txt_date_of_word)).setText( DateUtil.convertDateFormat(dailyWordList.get(position).date,"yyyy-MM-dd","dd MMM yyyy") );
            shownFragmentPositionInViewPager = position;
        }
        else
        {
            ((TextView)fragmentView.findViewById(R.id.txt_date_of_word)).setText("");
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void getApi_Response(List<DailyWord> dailyWordList) {


        if (dailyWordList != null && dailyWordList.size()>0) {
            String current_date = DateUtil.getTodayDateString("yyyy-MM-dd");
            new SPUtils(getActivity()).setValue(SPUtils.LAST_SYNC_DATE, current_date);
        }
        initViews();
    }

    @Override
    public void getApi_Response(boolean is_success, String response_messg) {
        //No Use
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    private void setWordFavourite(boolean is_favourite) {
        ImageView imageView = (ImageView) fragmentView.findViewById(R.id.img_favourite);

        if (is_favourite) {
            imageView.setImageResource(R.mipmap.ic_star_white_24dp);
            imageView.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        } else {
            imageView.setImageResource(R.mipmap.ic_star_border_white_24dp);
            imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }

}
