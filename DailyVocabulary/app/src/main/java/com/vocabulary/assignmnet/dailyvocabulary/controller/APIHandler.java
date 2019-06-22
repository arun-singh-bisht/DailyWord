package com.vocabulary.assignmnet.dailyvocabulary.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler;
import com.vocabulary.assignmnet.dailyvocabulary.Model.DailyWord;
import com.vocabulary.assignmnet.dailyvocabulary.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.vocabulary.assignmnet.dailyvocabulary.Database.DatabaseHandler.findInDB;

/**
 * Created by ARU ANJI on 12/3/2016.
 */
public class APIHandler {

    Context context;
    private AlertDialog dialog;
    private okhttp3.Call call;
    boolean is_succcess = false;
    String response_message = null;
    public interface IApi_Response {
        public void getApi_Response(List<DailyWord> dailyWordList);
        public void getApi_Response(boolean is_success,String response_messg);


    }

    IApi_Response iApi_response;

    public APIHandler(Context context, IApi_Response iApi_response) {
        this.context = context;
        dialog = new SpotsDialog(context, R.style.Custom);
        this.iApi_response = iApi_response;
    }

    public void getWordsList(String from_date, String to_date) {
        dialog.show();
        String body = "from=" + from_date +
                "&to=" + to_date;


        call = RESTClient.getTodayWords(context, body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                System.out.println("myarun: onFailure");
                dialog.dismiss();

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(null);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final ArrayList<DailyWord> dailyWords = new ArrayList<DailyWord>();
                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);


                        if (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dailyWord_json_obj = jsonArray.getJSONObject(i);

                                //Find if particular date item is already added in db
                                List<DailyWord> dailyWordsList = DatabaseHandler.findInDB(dailyWord_json_obj.getString("created"));

                                DailyWord dailyWord = null;
                                if(dailyWordsList!=null && dailyWordsList.size()>0)
                                {
                                    dailyWord =dailyWordsList.get(0);
                                    dailyWord.word = dailyWord_json_obj.getString("word");
                                    dailyWord.meaning = dailyWord_json_obj.getString("meaning");
                                    dailyWord.example_1 = dailyWord_json_obj.getString("example1");
                                    dailyWord.example_2 = dailyWord_json_obj.getString("example2");
                                    dailyWord.date = dailyWord_json_obj.getString("created");
                                    dailyWord.save();
                                }else
                                {
                                    dailyWord = new DailyWord(dailyWord_json_obj.getString("word"),dailyWord_json_obj.getString("meaning"),dailyWord_json_obj.getString("example1"),dailyWord_json_obj.getString("example2"),false,dailyWord_json_obj.getString("created"));
                                    dailyWord.save();
                                }
                                dailyWords.add(dailyWord);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        dialog.dismiss();

                    }
                } else {
                    dialog.dismiss();
                }

                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(dailyWords);
                    }
                });

            }
        });
    }

    public void addNewWord(DailyWord dailyWord) {
        dialog.show();
        String body = "word=" + dailyWord.word.trim() +
                "&meaning=" + dailyWord.meaning.trim()+
                "&example1=" + dailyWord.example_1.trim()+
                "&example2=" + dailyWord.example_2.trim()+
                "&created=" + dailyWord.date.trim();



        call = RESTClient.addNewWord(context, body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                System.out.println("myarun: onFailure");
                dialog.dismiss();

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(false,"Request failure.");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);


                        if (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("1")) {
                            is_succcess = true;
                            response_message = "New word added";
                        }else if(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("2"))
                        {
                            is_succcess = false;
                            response_message = "Word is already added for selected date,please pick a different word or date.";
                        }else
                        {
                            is_succcess = false;
                            response_message = "Request failure.";
                        }
                        System.out.println("myarun: " + res);
                    } catch (Exception e) {
                        e.printStackTrace();
                        is_succcess = false;
                        response_message = "Error in parsing response!";
                    } finally {
                        dialog.dismiss();

                    }
                } else {
                    dialog.dismiss();
                }

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(is_succcess,response_message);
                    }
                });

            }
        });
    }

    public void loginAsAdmin(String admin_key) {
        dialog.show();
        String body = "username=" + "sbisht" +
                "&password=" + admin_key ;


        call = RESTClient.Login(context, body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(false,"Request failure.");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);


                        if (jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("1")) {
                            is_succcess = true;
                            response_message = "Login Succesfull";
                        }else if(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("0"))
                        {
                            is_succcess = false;
                            response_message = "Login fail, Key may not be correct";
                        }else
                        {
                            is_succcess = false;
                            response_message = "Login fail, Key may not be correct";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        is_succcess = false;
                        response_message = "Error in parsing response!";
                    } finally {
                        dialog.dismiss();

                    }
                } else {
                    dialog.dismiss();
                }

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iApi_response.getApi_Response(is_succcess,response_message);
                    }
                });

            }
        });
    }

}
