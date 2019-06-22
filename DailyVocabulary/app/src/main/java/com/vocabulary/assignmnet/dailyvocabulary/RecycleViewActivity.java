package com.vocabulary.assignmnet.dailyvocabulary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.Model.RecycleModelDailyWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arun.singh on 11/29/2016.
 */
public class RecycleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        init_SetupActivity();
        init_ListFavouriteWords();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void init_SetupActivity() {
        //Set back button on Home Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Set Title of Activity
        getSupportActionBar().setTitle("Recycle View");
    }

    private void init_ListFavouriteWords() {
        //Get All Favourite words from DB
        final List<DailyWord> favouriteWordList = DatabaseHandler.getAllFavourite();


        if (favouriteWordList.size() == 0) {
            findViewById(R.id.lay_empty_state).setVisibility(View.VISIBLE);
            findViewById(R.id.recycler_view).setVisibility(View.GONE);
            return;
        }

        List<DailyWord> recycleModelDailyWords = new ArrayList<DailyWord>();
        for(int i =0 ;i<favouriteWordList.size();i++)
        {
            if(favouriteWordList.get(i).is_added_favourite)
            {

                RecycleModelDailyWord recycleModelDailyWord = new RecycleModelDailyWord();
                recycleModelDailyWord.word = favouriteWordList.get(i).word;
                recycleModelDailyWord.meaning = favouriteWordList.get(i).meaning;
                recycleModelDailyWord.example_1 = favouriteWordList.get(i).example_1;
                recycleModelDailyWord.example_2 = favouriteWordList.get(i).example_2;
                recycleModelDailyWord.category = "(Normal)";

                recycleModelDailyWords.add(recycleModelDailyWord);
            }else
            {
                recycleModelDailyWords.add(favouriteWordList.get(i));
            }

        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecycleViewAdapter(recycleModelDailyWords));
    }

    class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<DailyWord> recycleModelDailyWords;

        int VIEW_TYPE_DAILY_WORD_RECYCLE = 1;
        int VIEW_TYPE_DAILY_WORD = 2;

        public RecycleViewAdapter(List<DailyWord> recycleModelDailyWords) {
            this.recycleModelDailyWords = recycleModelDailyWords;
        }

        @Override
        public int getItemCount() {
            return recycleModelDailyWords.size();
        }

        @Override
        public int getItemViewType(int position) {

            if (recycleModelDailyWords.get(position) instanceof RecycleModelDailyWord)
                return 1;
            else if (recycleModelDailyWords.get(position) instanceof DailyWord)
                return 2;

            return -1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if(viewType == VIEW_TYPE_DAILY_WORD_RECYCLE)
            {
                View v = inflater.inflate(R.layout.list_row_view_favourite_word_recycle_view,null);
                viewHolder = new ViewHolder1(v);

            }else if(viewType == VIEW_TYPE_DAILY_WORD)
            {
                View v = inflater.inflate(R.layout.list_row_view_favourite_word_recycle_view,null);
                viewHolder = new ViewHolder1(v);
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            if(holder instanceof ViewHolder1)
            {
                RecycleModelDailyWord recycleModelDailyWord = (RecycleModelDailyWord)recycleModelDailyWords.get(position);
                ((ViewHolder1)holder).txt_word.setText(recycleModelDailyWord.word);
                ((ViewHolder1)holder).txt_meaning.setText(recycleModelDailyWord.meaning);
                ((ViewHolder1)holder).txt_example_1.setText(recycleModelDailyWord.example_1);
                ((ViewHolder1)holder).txt_example_2.setText(recycleModelDailyWord.example_2);
                ((ViewHolder1)holder).txt_category.setText(recycleModelDailyWord.category);

            }else if(holder instanceof ViewHolder2)
            {
                DailyWord dailyWord = recycleModelDailyWords.get(position);
                ((ViewHolder2)holder).txt_word.setText(dailyWord.word);
                ((ViewHolder2)holder).txt_meaning.setText(dailyWord.meaning);
                ((ViewHolder2)holder).txt_example_1.setText(dailyWord.example_1);
                ((ViewHolder2)holder).txt_example_2.setText(dailyWord.example_2);
            }
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {

        public TextView txt_word;
        public TextView txt_meaning;
        public TextView txt_example_1;
        public TextView txt_example_2;
        public TextView txt_category;
        public ViewHolder1(View itemView) {
            super(itemView);
            txt_word = (TextView) itemView.findViewById(R.id.txt_word);
            txt_meaning = (TextView) itemView.findViewById(R.id.txt_meaning);
            txt_example_1 = (TextView) itemView.findViewById(R.id.txt_example_1);
            txt_example_2 = (TextView) itemView.findViewById(R.id.txt_example_2);
            txt_category = (TextView) itemView.findViewById(R.id.txt_category);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView txt_word;
        public TextView txt_meaning;
        public TextView txt_example_1;
        public TextView txt_example_2;
        public ViewHolder2(View itemView) {
            super(itemView);
            txt_word = (TextView) itemView.findViewById(R.id.txt_word);
            txt_meaning = (TextView) itemView.findViewById(R.id.txt_meaning);
            txt_example_1 = (TextView) itemView.findViewById(R.id.txt_example_1);
            txt_example_2 = (TextView) itemView.findViewById(R.id.txt_example_2);
        }
    }
}
