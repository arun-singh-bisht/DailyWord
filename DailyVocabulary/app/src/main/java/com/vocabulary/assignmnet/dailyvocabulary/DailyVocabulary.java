package com.vocabulary.assignmnet.dailyvocabulary;

import android.app.Application;
import com.activeandroid.ActiveAndroid;

/**
 * Created by arun.singh on 11/29/2016.
 */
public class DailyVocabulary extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
