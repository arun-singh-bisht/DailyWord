package com.vocabulary.assignmnet.dailyvocabulary.Utils;

import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by arun.singh on 12/12/2016.
 */

public class AppUtils {

    static class ListSorter implements Comparator<DailyWord>
    {
        @Override
        public int compare(DailyWord dailyWord, DailyWord t1) {

            Date date_1 = DateUtil.convertStringToDate(dailyWord.date,"yyyy-MM-dd");
            Date date_2 = DateUtil.convertStringToDate(t1.date,"yyyy-MM-dd");


            if(date_1.before(date_2))
                return 1;
            else if(date_1.after(date_2))
                return -1;

            return 0;
        }
    }

    public static void sortWordsList_old_to_new(List<DailyWord> dailyWordList)
    {
        Collections.sort(dailyWordList,new ListSorter());
    }


}
