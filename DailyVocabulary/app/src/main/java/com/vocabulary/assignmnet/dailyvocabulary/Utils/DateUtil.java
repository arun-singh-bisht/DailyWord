package com.vocabulary.assignmnet.dailyvocabulary.Utils;

import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ARU ANJI on 12/3/2016.
 */
public class DateUtil {

    public static String getTodayDateString(String return_date_pattern) {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(return_date_pattern);
        String DateToStr = format.format(curDate);
        return DateToStr;
    }

    public static Date convertStringToDate(String date,String current_date_format)
    {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(current_date_format);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateByIncrement(String current_date,String current_date_format,int next_date_offset,String next_date_format)
    {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(current_date_format);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(current_date));
            c.add(Calendar.DATE, next_date_offset);  // number of days to add
            sdf = new SimpleDateFormat(next_date_format);
            return sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String convertDateFormat(String date, String current_format, String new_format) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(current_format);
            Date date1 = format.parse(date);

            format = new SimpleDateFormat(new_format);
            return format.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Data list sorter acoording to date
    //List<DailyWord> dailyWordList - List of custom data having date field
    //String date_format - format of date string in data list
    //int sort_order - desired sort order 0: most recent date to old date , 1:old date to most recent date.
    public static void sortDataLsit_according_to_date(List<DailyWord> dailyWordList,String date_format,int sort_order)
    {

        Collections.sort(dailyWordList,new DateUtil.ListSorter(date_format,sort_order));
    }
    private static class ListSorter implements Comparator<DailyWord>
    {
        private String date_format;
        private int sort_order;
        ListSorter(String date_format,int sort_order)
        {
            this.date_format = date_format;
            this.sort_order = sort_order;
        }
        @Override
        public int compare(DailyWord dailyWord, DailyWord t1) {

            Date date_1 = DateUtil.convertStringToDate(dailyWord.date,date_format);
            Date date_2 = DateUtil.convertStringToDate(t1.date,date_format);


            if(date_1.before(date_2))
                return sort_order ==0?1:-1;
            else if(date_1.after(date_2))
                return sort_order ==0?-1:1;

            return 0;
        }
    }

}
