package com.mbco.brainstormandroid.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;

import org.json.JSONException;

import java.util.ArrayList;


public class AdminApprove extends Fragment implements View.OnClickListener{

    private View rootView;

    private final Fragment fragment = this;

    private Context context;

    private Button btnTeachers, btnCourses;

    private ListView listView;

    private ApproveAdapter adapter;

    private ArrayList<Object> data;

    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_admin_approve, container, false);

        Initialize();

        btnTeachers.setOnClickListener(this);

        btnCourses.setOnClickListener(this);

        return rootView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        type = view.getId() == R.id.btnTeachers ? "teacher" : "course";
        new LoadRequests().start();
    }

    private void Initialize(){
        btnTeachers = rootView.findViewById(R.id.btnTeachers);
        btnCourses = rootView.findViewById(R.id.btnCourses);
        listView = rootView.findViewById(R.id.listView);
        context = getContext();
    }

    public void UpdateListview(Object o) {
        data.remove(o);
        listView.setAdapter(new ApproveAdapter(data,fragment,context,type));
    }

    private class LoadRequests extends Thread{

        @Override
        public void run() {
            super.run();
            try {
                Requests.GetRequests(type, new RequestsResultListener<ArrayList<Object>>(){
                    @Override
                    public void getResult(ArrayList<Object> result) {
                        if (result != null) {
                            data = result;
                            adapter = new ApproveAdapter(data, fragment, context, type);
                            listView.setAdapter(adapter);
                        }
                        else{
                            //error
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}