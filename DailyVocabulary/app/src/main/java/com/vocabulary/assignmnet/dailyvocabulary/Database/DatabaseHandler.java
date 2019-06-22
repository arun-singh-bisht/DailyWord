package com.vocabulary.assignmnet.dailyvocabulary.Database;

import com.activeandroid.query.Select;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;

import java.util.List;

/**
 * Created by arun.singh on 11/29/2016.
 */
public class DatabaseHandler {

    public static List<DailyWord> getAllWords() {
        return new Select()
                .from(DailyWord.class)
                .execute();
    }
    public static List<DailyWord> getAllFavourite() {
        return new Select()
                .from(DailyWord.class)
                .where("Is_Added_Favourite = ?", true)
                .execute();
    }
    public static List<DailyWord> findInDB(String date) {
        try {
            return new Select()
                    .from(DailyWord.class)
                    .where("date = ?", date)
                    .execute();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static void updateRecord(DailyWord dailyWord)
    {
        dailyWord.save();
    }

}
