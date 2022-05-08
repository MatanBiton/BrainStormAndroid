package com.mbco.brainstormandroid.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.courseCreation.CourseCreationDetails;

import org.json.JSONException;

import java.util.ArrayList;

public class TeacherCourses extends Fragment {

    private View rootView;

    private Button btnCreate;

    private ListView listView;

    private Context context;

    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_teacher_courses, container, false);

        Initialize();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = inflater.inflate(R.layout.create_course_confirmation_dialog, null);

                Button btnYes = view.findViewById(R.id.btnYes);
                Button btnNo = view.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((Teacher) HelpFunctions.CurrentUser).getApproval()!=1){
                            Toast.makeText(context, "You Must Be Approved!", Toast.LENGTH_SHORT).show();
                        } else{
                            Intent intent = new Intent(context, CourseCreationDetails.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        new LoadCourses().start();

        return rootView;
    }

    private void Initialize(){
        btnCreate = rootView.findViewById(R.id.btnCreate);
        listView = rootView.findViewById(R.id.listView);
        context = getContext();
    }

    private class LoadCourses extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.GetTeacherCourses(HelpFunctions.CurrentUser.getUID(),
                        new RequestsResultListener<ArrayList<Course>>(){
                            @Override
                            public void getResult(ArrayList<Course> result) {
                                TeacherCoursesAdapter adapter = new TeacherCoursesAdapter(result, context);
                                listView.setAdapter(adapter);
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}