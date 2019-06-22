package com.vocabulary.assignmnet.dailyvocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.SPUtils;
import com.vocabulary.assignmnet.dailyvocabulary.controller.APIHandler;

import java.util.List;

/**
 * Created by ARU ANJI on 11/28/2016.
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener, APIHandler.IApi_Response {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews() {
        //Check Condition
        if (new SPUtils(SplashActivity.this).getValue(SPUtils.IS_USER_LOGIN).equalsIgnoreCase("true")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            //Set Event Listener On View
            findViewById(R.id.btn_user).setOnClickListener(this);
            findViewById(R.id.btn_admin).setOnClickListener(this);
            findViewById(R.id.img_go).setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_user: {

                new SPUtils(SplashActivity.this).setValue(SPUtils.IS_USER_LOGIN, "true");
                new SPUtils(SplashActivity.this).setValue(SPUtils.USER_TYPE, "normal");
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.btn_admin: {
                findViewById(R.id.lay_admin_key).setVisibility(View.VISIBLE);

                /*new SPUtils(SplashActivity.this).setValue(SPUtils.IS_USER_LOGIN, "true");
                new SPUtils(SplashActivity.this).setValue(SPUtils.USER_TYPE, "admin");
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/

            }
            break;
            case R.id.img_go: {

                String key = ((EditText)findViewById(R.id.ed_admin_key)).getText().toString();
                if(key.isEmpty() )
                {
                    Toast.makeText(SplashActivity.this, "Enter a valid Admin key", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Call Login API
                new APIHandler(SplashActivity.this, this).loginAsAdmin(key);


            }
            break;
            default: {

            }
        }
    }

    @Override
    public void getApi_Response(List<DailyWord> dailyWordList) {

    }

    @Override
    public void getApi_Response(boolean is_success, String response_messg) {

        if (is_success) {
            new SPUtils(SplashActivity.this).setValue(SPUtils.IS_USER_LOGIN, "true");
            new SPUtils(SplashActivity.this).setValue(SPUtils.USER_TYPE, "admin");
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SplashActivity.this, response_messg + "", Toast.LENGTH_SHORT).show();
        }
    }
}
