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
    private static Requests instance;
    private static RequestQueue queue;

    public Requests(Context context){
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public static synchronized Requests getInstance(Context context){
        return instance == null ? new Requests(context) : instance;
    }

    public static synchronized Requests getInstance(){
        if (null == instance)
        {
            throw new IllegalStateException(Requests.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    //this function checks if the device can get data to and from the api
    public void TestConnection(RequestsResultListener<Boolean> listener){
        String url = BASE_URL + "test";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {listener.getResult(true);}, error -> {listener.getResult(false); });
        queue.add(request);
    }

    public User Login(String email, String password){

    }
}
