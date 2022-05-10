package com.mbco.brainstormandroid.teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.SearchUsersAdapter;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONException;

import java.util.ArrayList;


public class TeacherSearch extends Fragment {

    private View rootView;

    private EditText editSearch;

    private Button btnSearch, btnClear;

    private Button btnFilter, btnApply;

    private ListView listView;

    private ArrayList<RadioButton> radioButtons;

    private boolean[] checkedList;

    private Context context;

    private String filter, filterValue;

    private ArrayList<User> users;

    private AlertDialog alertDialog;

    private final Fragment fragment = this;

    private Activity activity;

    private TextView txtMsg;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_teacher_search, container, false);

        activity = getActivity();

        Initialize();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = inflater.inflate(R.layout.student_filter_dialog, null);

                radioButtons = new ArrayList<>();

                radioButtons.add(view.findViewById(R.id.rdBtnFirstName));//0
                radioButtons.add(view.findViewById(R.id.rdBtnLastName));//1
                radioButtons.add(view.findViewById(R.id.rdBtnId));//2
                radioButtons.add(view.findViewById(R.id.rdBtnCity));//3
                radioButtons.add(view.findViewById(R.id.rdBtnAddress));//4
                radioButtons.add(view.findViewById(R.id.rdBtnPhone));//5
                radioButtons.add(view.findViewById(R.id.rdBtnBirthday));//6
                radioButtons.add(view.findViewById(R.id.rdBtnEmail));//7
                radioButtons.add(view.findViewById(R.id.rdBtnExperience));//8
                radioButtons.add(view.findViewById(R.id.rdBtnCertification));//9

                for (int i = 0; i< radioButtons.size(); i++){
                    radioButtons.get(i).setChecked(checkedList[i]);
                }


                btnClear = view.findViewById(R.id.btnClear);
                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RadioButton btn:radioButtons){
                            btn.setChecked(false);
                        }
                        filter = "NONE";
                    }
                });

                btnApply = view.findViewById(R.id.btnApply);
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < radioButtons.size();i++){
                            if (radioButtons.get(i).isChecked()){
                                filter = String.valueOf(radioButtons.get(i).getTag());
                            }
                            checkedList[i] = radioButtons.get(i).isChecked();
                        }
                        alertDialog.cancel();
                    }
                });

                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterValue = editSearch.getText().toString();
                if (filterValue.equals("")){
                    filterValue = "NONE";
                }
                if (filterValue.equals("NONE") && !filter.equals("NONE")){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
                else if (!filterValue.equals("NONE") && filter.equals("NONE")){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
                else{
                    listView.setVisibility(View.GONE);
                    txtMsg.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    new LoadStudents().start();
                }
            }
        });

        new LoadStudents().start();
        return rootView;
    }

    public void Initialize() {
        filter = "NONE";
        filterValue = "NONE";

        checkedList = new boolean[12];
        for (boolean b : checkedList) {
            b = false;
        }

        context = getContext();

        editSearch = rootView.findViewById(R.id.editSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        btnFilter = rootView.findViewById(R.id.btnFilter);
        listView = rootView.findViewById(R.id.listView);
        txtMsg = rootView.findViewById(R.id.txtMsg);
        progressBar = rootView.findViewById(R.id.progressBar);
    }

    public class LoadStudents extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.GetUsersByFilter(filter, filterValue, "student", new RequestsResultListener<ArrayList<User>>() {
                    @Override
                    public void getResult(ArrayList<User> result) {
                        if (result != null) {
                            users = result;
                            if (users.size() > 0) {
                                SearchUsersAdapter searchAdapter = new SearchUsersAdapter(users, fragment, context);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.setAdapter(searchAdapter);
                                        listView.setVisibility(View.VISIBLE);
                                        txtMsg.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                listView.setVisibility(View.GONE);
                                txtMsg.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}