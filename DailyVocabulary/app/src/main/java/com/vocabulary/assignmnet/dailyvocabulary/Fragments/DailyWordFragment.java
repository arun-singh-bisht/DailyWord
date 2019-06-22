package com.vocabulary.assignmnet.dailyvocabulary.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabulary.assignmnet.dailyvocabulary.R;

/**
 * Created by ARU ANJI on 11/28/2016.
 */
public class DailyWordFragment extends Fragment {

    public static String WORD ="EXTRA_WORD";
    public static String MEANING ="EXTRA_MEANING";
    public static String EXAMPLE_1 ="EXTRA_EXAMPLE_1";
    public static String EXAMPLE_2 ="EXTRA_EXAMPLE_2";


    public static final DailyWordFragment newInstance(String word,String meaning,String example_1,String example_2)
    {
        DailyWordFragment f = new DailyWordFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(WORD, word);
        bdl.putString(MEANING, meaning);
        bdl.putString(EXAMPLE_1, example_1);
        bdl.putString(EXAMPLE_2, example_2);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_daily_word,null);
        String word = getArguments().getString(WORD);
        String meaning = getArguments().getString(MEANING);
        String example_1 = getArguments().getString(EXAMPLE_1);
        String example_2 = getArguments().getString(EXAMPLE_2);

        ((TextView)view.findViewById(R.id.txt_word)).setText(word);
        ((TextView)view.findViewById(R.id.txt_meaning)).setText(meaning);
        ((TextView)view.findViewById(R.id.txt_example_1)).setText(example_1);
        ((TextView)view.findViewById(R.id.txt_example_2)).setText(example_2);

        return view;
    }
}

