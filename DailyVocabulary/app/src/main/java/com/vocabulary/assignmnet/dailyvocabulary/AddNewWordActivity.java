package com.vocabulary.assignmnet.dailyvocabulary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.DateUtil;
import com.vocabulary.assignmnet.dailyvocabulary.controller.APIHandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static com.vocabulary.assignmnet.dailyvocabulary.R.id.card_view_submit;

/**
 * Created by arun.singh on 11/29/2016.
 */
public class AddNewWordActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, APIHandler.IApi_Response {

    DailyWord dailyWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);


        Serializable serializable = getIntent().getSerializableExtra("value");
        if(serializable!=null)
        {
            dailyWord = (DailyWord)serializable;
        }
        //Set up Activity Views
        init_SetupActivity();
        initViews();

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
        getSupportActionBar().setTitle("Add New Words");
    }

    private void initViews() {
        findViewById(R.id.lay_date_selector).setOnClickListener(this);
        findViewById(card_view_submit).setOnClickListener(this);

        if(dailyWord!=null)
        {
            ((TextView) findViewById(R.id.txt_date)).setText(DateUtil.convertDateFormat(dailyWord.date,"yyyy-MM-dd","dd-MMM-yyyy"));
            ((EditText)findViewById(R.id.input_word)).setText(dailyWord.word);
            ((EditText)findViewById(R.id.input_meaning)).setText(dailyWord.meaning);
            ((EditText)findViewById(R.id.input_example_1)).setText(dailyWord.example_1);
            ((EditText)findViewById(R.id.input_example_2)).setText(dailyWord.example_2);
            ((TextView) findViewById(R.id.txt_submit)).setText("UPDATE");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lay_date_selector: {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddNewWordActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);

                Calendar max_date_cal = Calendar.getInstance();
                max_date_cal.setTimeInMillis(now.getTimeInMillis() + (1000 * 60 * 60 * 24 * 6));
                dpd.setMaxDate(max_date_cal);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
            break;
            case card_view_submit: {

                DailyWord dailyWord = new DailyWord();
                dailyWord.date = ((TextView) findViewById(R.id.txt_date)).getText().toString();
                dailyWord.word = ((EditText) findViewById(R.id.input_word)).getText().toString();
                dailyWord.meaning = ((EditText) findViewById(R.id.input_meaning)).getText().toString();
                dailyWord.example_1 = ((EditText) findViewById(R.id.input_example_1)).getText().toString();
                dailyWord.example_2 = ((EditText) findViewById(R.id.input_example_2)).getText().toString();

                if (dailyWord.date.equalsIgnoreCase("dd-MMM-yyyy")) {
                    Toast.makeText(AddNewWordActivity.this, "Select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dailyWord.word.isEmpty()) {
                    Toast.makeText(AddNewWordActivity.this, "Write a new Word before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dailyWord.meaning.isEmpty()) {
                    Toast.makeText(AddNewWordActivity.this, "Write meaning of Word before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dailyWord.example_1.isEmpty()) {
                    Toast.makeText(AddNewWordActivity.this, "Make first sentence using word before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dailyWord.example_2.isEmpty()) {
                    Toast.makeText(AddNewWordActivity.this, "Make second sentence using word before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailyWord.date = DateUtil.convertDateFormat(dailyWord.date,"dd-MMM-yyyy","yyyy-MM-dd");
                new APIHandler(AddNewWordActivity.this, this).addNewWord(dailyWord);

            }
            default: {

            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String formated_date_string = DateUtil.convertDateFormat(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, "dd-MM-yyyy", "dd-MMM-yyyy");
        ((TextView) findViewById(R.id.txt_date)).setText(formated_date_string + "");
    }

    @Override
    public void getApi_Response(List<DailyWord> dailyWordList) {
        //No Use
    }

    @Override
    public void getApi_Response(boolean is_success, String response_messg) {
        Toast.makeText(AddNewWordActivity.this, response_messg, Toast.LENGTH_SHORT).show();
        if (is_success) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
