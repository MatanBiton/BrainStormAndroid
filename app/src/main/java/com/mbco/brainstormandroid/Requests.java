package com.mbco.brainstormandroid;

import android.content.Context;
import android.widget.BaseAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringJoiner;

public class Requests {

    //base url for the api
    private final static String BASE_URL = "http://192.168.10.116:5000/";
    private static Requests instance;
    private static RequestQueue queue;

    public Requests(Context context){
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public static synchronized Requests getInstance(Context context){
        if (null == instance)
            instance = new Requests(context);
        return instance;
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

    public void Login(String email, String password, RequestsResultListener<User> listener) throws JSONException {
        String url = BASE_URL + "login";
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("message").equals("User not found")){
                        listener.getResult(null);
                    } else{
                        listener.getResult(response.get("user_type").equals("teacher") ? new Teacher(response) : new User(response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        queue.add(request);
    }

    public void Register(User user, RequestsResultListener<User> listener) throws JSONException {
        String url = BASE_URL + "register";
        JSONObject body = user.getJSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("message").equals("Registration Successful!")){
                        listener.getResult(response.get("user_type").equals("teacher") ? new Teacher(response) : new User(response));
                    } else{
                        listener.getResult(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        queue.add(request);
    }
}
