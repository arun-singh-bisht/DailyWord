package com.vocabulary.assignmnet.dailyvocabulary.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by arun.singh on 11/29/2016.
 */
@Table(name = "DailyWord")
public class DailyWord extends Model implements Serializable {

    @Column(name = "Word")
    public String word;
    @Column(name = "Meaning")
    public String meaning;
    @Column(name = "Example_1")
    public String example_1;
    @Column(name = "Example_2")
    public String example_2;
    @Column(name = "Is_Added_Favourite")
    public boolean is_added_favourite;
    @Column(name = "Date")
    public String date;


    public DailyWord() {
        super();
    }

    public DailyWord(String word, String meaning, String example_1, String example_2, boolean is_added_favourite, String date) {
        super();
        this.word = word;
        this.meaning = meaning;
        this.example_1 = example_1;
        this.example_2 = example_2;
        this.is_added_favourite = is_added_favourite;
        this.date = date;
    }

}
