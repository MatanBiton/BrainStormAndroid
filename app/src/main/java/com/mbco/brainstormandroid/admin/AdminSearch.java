package com.mbco.brainstormandroid.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.KeyboardUtils;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.SearchUsersAdapter;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONException;

import java.util.ArrayList;

public class AdminSearch extends Fragment {

    private EditText editSearch;

    private Button btnSearch, btnClear;

    private Button btnFilter, btnApply;

    private ListView listView;

    private ArrayList<RadioButton> radioButtons;

    private boolean[] checkedList;

    private CheckBox checkStudent, checkTeacher;

    private Context ctx;

    private String filter, filterValue, type;

    private ArrayList<User> users;

    private AlertDialog alertDialog;

    private final Fragment fragment = this;

    private Activity thisActivity;

    private ProgressBar progressBar;

    private TextView txtMsg;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thisActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_admin_search, container, false);

        Initialize(rootView);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                View view = fragment.getLayoutInflater().inflate(R.layout.admin_filter_dialog, null);

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



                checkStudent = view.findViewById(R.id.checkStudent);
                checkStudent.setChecked(checkedList[10]);
                if (checkStudent.isChecked()){
                    radioButtons.get(9).setEnabled(false);
                    radioButtons.get(9).setChecked(false);
                    radioButtons.get(8).setEnabled(false);
                    radioButtons.get(8).setChecked(false);
                }
                checkStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            radioButtons.get(9).setEnabled(false);
                            radioButtons.get(9).setChecked(false);
                            radioButtons.get(8).setEnabled(false);
                            radioButtons.get(8).setChecked(false);
                            if (checkTeacher.isChecked()){
                                checkTeacher.setChecked(false);
                            }
                        }
                        else{
                            radioButtons.get(9).setEnabled(true);
                            radioButtons.get(8).setEnabled(true);
                        }
                    }
                });

                checkTeacher = view.findViewById(R.id.checkTeacher);
                checkTeacher.setChecked(checkedList[11]);
                checkTeacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            if (checkStudent.isChecked()){
                                radioButtons.get(8).setEnabled(true);
                                radioButtons.get(9).setEnabled(true);
                                checkStudent.setChecked(false);
                            }
                        }
                    }
                });

                btnClear = view.findViewById(R.id.btnClear);
                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RadioButton btn:radioButtons){
                            btn.setChecked(false);
                        }
                        checkStudent.setChecked(false);
                        checkTeacher.setChecked(false);
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
                        checkedList[11] = checkTeacher.isChecked();
                        if (checkStudent.isChecked()){
                            type = "student";
                            checkedList[10] = true;
                            checkedList[11] = false;
                        }
                        else if (checkTeacher.isChecked()){
                            checkedList[11] = true;
                            checkedList[10] = false;
                            type = "teacher";
                        }
                        else {
                            type = "none";
                            checkedList[10] = false;
                            checkedList[11] = false;

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
                if (filterValue.equals("")) {
                    filterValue = "NONE";
                }
                if (filter.equals("NONE") && !filterValue.equals("NONE")){
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
                else if (!filter.equals("NONE") && filterValue.equals("NONE")){
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
                else{
                    listView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    txtMsg.setVisibility(View.GONE);
                    new LoadUsers().start();
                }
            }
        });

        KeyboardUtils.addKeyboardToggleListener(thisActivity, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if (!isVisible){
                    editSearch.clearFocus();
                }
            }
        });

        new LoadUsers().start();
        return rootView;
    }

    public void Initialize(View rootView){
        filter = "NONE";
        filterValue = "NONE";
        type = "none";

        checkedList = new boolean[12];
        for (boolean b: checkedList) {
            b = false;
        }

        ctx = getContext();

        editSearch = rootView.findViewById(R.id.editSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        btnFilter = rootView.findViewById(R.id.btnFilter);
        listView = rootView.findViewById(R.id.listView);
        progressBar = rootView.findViewById(R.id.progressBar);
        txtMsg = rootView.findViewById(R.id.txtMsg);
    }

    public class LoadUsers extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.GetUsersByFilter(filter, filterValue, type, new RequestsResultListener<ArrayList<User>>() {
                    @Override
                    public void getResult(ArrayList<User> result) {
                        if (result != null) {
                            users = result;
                            if (result.size() > 0) {
                                SearchUsersAdapter searchAdapter = new SearchUsersAdapter(users, fragment, ctx);
                                thisActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        txtMsg.setVisibility(View.GONE);
                                        listView.setAdapter(searchAdapter);
                                        listView.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else{
                                progressBar.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                                txtMsg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}