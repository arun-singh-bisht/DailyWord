package com.vocabulary.assignmnet.dailyvocabulary.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ARU ANJI on 11/28/2016.
 */
public class SPUtils {

    Context context;
    String PREFERENCES = "daily_vocab";
    public static String IS_USER_LOGIN = "is_user_login";
    public static String USER_TYPE = "user_type";
    public static String LAST_SYNC_DATE ="last_sync_date";
    SharedPreferences sp;

    public SPUtils(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getValue(String tag) {
        return sp.getString(tag, "");
    }

    public void setValue(String tag, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag, value);
        editor.apply();
    }

}
