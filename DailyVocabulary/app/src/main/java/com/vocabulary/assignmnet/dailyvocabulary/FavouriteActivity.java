package com.vocabulary.assignmnet.dailyvocabulary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import java.util.List;

/**
 * Created by arun.singh on 11/29/2016.
 */
public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        //Set up Activity Views
        init_SetupActivity();
        //Set up and show list of words
        init_ListFavouriteWords();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(menuItem);
    }

    private void init_SetupActivity() {
        //Set back button on Home Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Set Title of Activity
        getSupportActionBar().setTitle("Favourite Words");
    }

    private void init_ListFavouriteWords() {
        //Get All Favourite words from DB
        final List<DailyWord> favouriteWordList = DatabaseHandler.getAllFavourite();

        //Validation for No records
        if(favouriteWordList.size()==0)
        {
            findViewById(R.id.lay_empty_state).setVisibility(View.VISIBLE);
            findViewById(R.id.listView_favourite_words).setVisibility(View.GONE);
            return;
        }

        //initialize and Show Favourite words in list
        ListView listView = (ListView) findViewById(R.id.listView_favourite_words);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return favouriteWordList.size();
            }

            @Override
            public Object getItem(int position) {
                return favouriteWordList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder viewHolder = null;
                if(convertView == null)
                {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(FavouriteActivity.this).inflate(R.layout.list_row_view_favourite_word, null);
                    viewHolder.txt_word = (TextView)convertView.findViewById(R.id.txt_word);
                    viewHolder.txt_meaning = (TextView)convertView.findViewById(R.id.txt_meaning);
                    viewHolder.txt_example_1 = (TextView)convertView.findViewById(R.id.txt_example_1);
                    viewHolder.txt_example_2 = (TextView)convertView.findViewById(R.id.txt_example_2);
                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder = (ViewHolder)convertView.getTag();
                }

                viewHolder.txt_word.setText(favouriteWordList.get(position).word);
                viewHolder.txt_meaning.setText(favouriteWordList.get(position).meaning);
                viewHolder.txt_example_1.setText(favouriteWordList.get(position).example_1);
                viewHolder.txt_example_2.setText(favouriteWordList.get(position).example_2);

                return convertView;
            }
            class ViewHolder
            {
                TextView txt_word;
                TextView txt_meaning;
                TextView txt_example_1;
                TextView txt_example_2;
            }
        });
    }
}
