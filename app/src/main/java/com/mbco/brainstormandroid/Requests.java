package com.mbco.brainstormandroid;

import android.content.Context;
import android.widget.BaseAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.StringJoiner;

public class Requests {

    //base url for the api
    private final static String BASE_URL = "http://192.168.1.117:5000/";
    private static RequestQueue queue;


    //this function checks if the device can get data to and from the api
    public static void TestConnection(Context context, RequestsResultListener<Boolean> listener){
        queue = Volley.newRequestQueue(context);
        String url = BASE_URL + "test";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {listener.getResult(true);}, error -> {listener.getResult(false); });
        queue.add(request);
    }
}
