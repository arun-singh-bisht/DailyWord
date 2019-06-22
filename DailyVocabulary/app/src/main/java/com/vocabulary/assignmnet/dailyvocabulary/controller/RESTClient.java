package com.vocabulary.assignmnet.dailyvocabulary.controller;

import android.content.Context;
import android.util.Log;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * ...
 * @author Mohsin Khan
 * @date May 17 2016
 */
public class RESTClient {




    public static String HOST_IP = "http://subhdailyvocab.esy.es/";
    public static final String LOGIN = "login.php";
    public static final String GET_WORDS_LIST = "list_vocab.php";
    public static final String ADD_NEW_WORD = "create_word.php";




    private static OkHttpClient client = new OkHttpClient();


    private static MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");


    public static Call Login(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(HOST_IP+LOGIN)
                .post(RequestBody.create(mediaType, body)).build();

        Call call = client.newCall(request);

        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    public static Call getTodayWords(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(HOST_IP+GET_WORDS_LIST)
                .post(RequestBody.create(mediaType, body)).build();

        Call call = client.newCall(request);

        call.enqueue(callback);
        printRequest(request);
        return call;
    }
    public static Call addNewWord(Context context, String body, Callback callback) {
        Request request = new Request.Builder().url(HOST_IP+ADD_NEW_WORD)
                .post(RequestBody.create(mediaType, body)).build();
        Call call = client.newCall(request);

        call.enqueue(callback);
        printRequest(request);
        return call;
    }

    /**
     * Method will print Http Request body to the LogCat Error
     * @param request pass the request to print its body
     */
    private static void printRequest(Request request) {
        Log.e("-----------", "-------------------------------------------------------------------------");
        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            Log.e("WEB_SERVICE", "BODY \t-> " + buffer.readUtf8());
            Log.e("WEB_SERVICE", "URL\t-> " + request.url().toString());
            Log.e("-----------", "-------------------------------------------------------------------------");
        } catch (IOException |StringIndexOutOfBoundsException e) {
            Log.e("WEB_SERVICE", e.getMessage());
        }
    }
}
