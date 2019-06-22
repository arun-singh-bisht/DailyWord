package com.vocabulary.assignmnet.dailyvocabulary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.vocabulary.assignmnet.dailyvocabulary.Fragments.DashboardFragmentAdmin;
import com.vocabulary.assignmnet.dailyvocabulary.Fragments.DashboardFragmentUser;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.SPUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Insert Words in local db
//        insertWordsListInDB();

        String user_type = new SPUtils(MainActivity.this).getValue(SPUtils.USER_TYPE);
        if (user_type.equalsIgnoreCase("normal"))
            getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragmentUser()).commit();
        else if (user_type.equalsIgnoreCase("admin"))
            getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragmentAdmin()).commit();


        //Load fragment

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_logout: {
                //Show Log out Confirmation Dialog
                new SPUtils(MainActivity.this).setValue(SPUtils.IS_USER_LOGIN, "");
                new SPUtils(MainActivity.this).setValue(SPUtils.LAST_SYNC_DATE, null);
                //Delete DailyWord Table
                new Delete().from(DailyWord.class).execute();
                //Start Splash Screen again
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
            break;
           /* case R.id.action_favorite: {
                //Show All Favorite words list in new screen.
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
//                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
            }*/


        }
        return super.onOptionsItemSelected(item);
    }

    private void insertWordsListInDB() {

        List<DailyWord> dailyWordList = new Select()
                .from(DailyWord.class)
                .execute();

        if (dailyWordList.size() > 0)
            return;

        DailyWord dailyWord = new DailyWord();
        dailyWord.word = "Whimsical";
        dailyWord.meaning = "acting or behaving in a capricious manner.";
        dailyWord.example_1 = "She has a whimsical sense of humor.";
        dailyWord.example_2 = "It's hard to make plans with such a whimsical best friend.";
        dailyWord.date = "24 Nov 2016";
        dailyWord.save();

        dailyWord = new DailyWord();
        dailyWord.word = "Savvy";
        dailyWord.meaning = "shrewdness and practical knowledge, especially in politics or business.";
        dailyWord.example_1 = "The corporate-finance bankers lacked the necessary political savvy.";
        dailyWord.example_2 = "The city's young, media-savvy crowd.";
        dailyWord.date = "25 Nov 2016";
        dailyWord.save();

        dailyWord = new DailyWord();
        dailyWord.word = "Miser";
        dailyWord.meaning = "a person who hoards wealth and spends as little money as possible.";
        dailyWord.example_1 = "a typical miser, he hid his money in the house in various places.";
        dailyWord.example_2 = "This God of his is the fault-finder of eternity, the miser of paradise.";
        dailyWord.date = "26 Nov 2016";
        dailyWord.save();

        dailyWord = new DailyWord();
        dailyWord.word = "Unforthcoming";
        dailyWord.meaning = "(of something required) not ready or made available when wanted or needed.";
        dailyWord.example_1 = "He sergeant seemed unforthcoming, so he enquired at the gate.";
        dailyWord.example_2 = "With money unforthcoming from the company, the project has had to be delayed.";
        dailyWord.date = "27 Nov 2016";
        dailyWord.save();

        dailyWord = new DailyWord();
        dailyWord.word = "Jovial";
        dailyWord.meaning = "cheerful and friendly.";
        dailyWord.example_1 = "She was in a jovial mood.";
        dailyWord.example_2 = "His manner is jovial, and stories are told with polished assurance: part performer, part toff.";
        dailyWord.date = "28 Nov 2016";
        dailyWord.save();

        dailyWord = new DailyWord();
        dailyWord.word = "Degrade";
        dailyWord.meaning = "treat or regard (someone) with contempt or disrespect.";
        dailyWord.example_1 = "She thought that many supposedly erotic pictures degraded whis is a very common interest in trying to degrade these networks.";
        dailyWord.example_2 = "The group accuses the company of degrading women in its ads.";
        dailyWord.date = "29 Nov 2016";
        dailyWord.save();

    }

}
