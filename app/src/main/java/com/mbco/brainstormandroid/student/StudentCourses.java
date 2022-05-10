package com.mbco.brainstormandroid.student;

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
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.courseParticipation.CourseParticipationPageView;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseData;
import com.mbco.brainstormandroid.models.Page;

import org.json.JSONException;

import java.util.ArrayList;

public class StudentCourses extends Fragment {

    private View rootView;

    private Context context;

    private TextView txtMsg;

    private Button btnAdd, btnCancelAdd, btnAddNewCourses;

    private ListView myCoursesListView, availableCourseListView;

    private AlertDialog NewCoursesDialog;

    private ArrayList<Course> allCourses, myCourses;

    private CourseSelectionAdapter adapter;

    private CourseListViewAdapter listViewAdapter;

    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student_courses, container, false);

        Initialize();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = getLayoutInflater().inflate(R.layout.student_add_courses_dialog, null);

                availableCourseListView = view.findViewById(R.id.availableCourseListView);
                btnCancelAdd = view.findViewById(R.id.btnCancelAdd);
                btnAddNewCourses = view.findViewById(R.id.btnAddNewCourses);
                TextView txtMsg = view.findViewById(R.id.txtMsg);

                btnCancelAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NewCoursesDialog.cancel();
                    }
                });

                btnAddNewCourses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> uids = adapter.getSelectedCoursesUid();
                        if (uids.size() > 0 ) {
                            new JoinCourses(adapter.getSelectedCoursesUid(),
                                    HelpFunctions.CurrentUser.getUID()).start();
                        } else {
                            Toast.makeText(context, "must select courses to add!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ArrayList<Course> temp = RemoveFromAll();

                txtMsg.setVisibility(temp.size()>0?View.GONE: View.VISIBLE);
                btnAddNewCourses.setEnabled(temp.size()>0);

                adapter = new CourseSelectionAdapter(temp, context);
                availableCourseListView.setAdapter(adapter);

                builder.setView(view);
                NewCoursesDialog = builder.create();
                NewCoursesDialog.show();
            }
        });

        btnAdd.setEnabled(false);

        new LoadCourses(HelpFunctions.CurrentUser.getUID(), true).start();

        new LoadCourses(HelpFunctions.CurrentUser.getUID(), false).start();

        return rootView;
    }

    private void Initialize(){
        btnAdd = rootView.findViewById(R.id.btnAdd);
        myCoursesListView = rootView.findViewById(R.id.listView);
        txtMsg = rootView.findViewById(R.id.txtMsg);

        context = getContext();
        fragment = this;

        allCourses = new ArrayList<>();
        myCourses = new ArrayList<>();
    }

    private ArrayList<Course> RemoveFromAll(){
        ArrayList<Course> temp = new ArrayList<>();
        boolean exist = false;
        for (Course course: allCourses){
            exist = false;
            for (Course course1: myCourses){
                if (course.getInfo().getUID().equals(course1.getInfo().getUID())){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                temp.add(course);
            }
        }
        allCourses = temp;
        return temp;
    }

    private class LoadCourses extends Thread{
        private final String UID;
        private final Boolean search;

        public LoadCourses(String UID, Boolean search){
            this.UID = UID;
            this.search = search;
        }

        @Override
        public void run() {
            super.run();
            try {
                if (search){
                    Requests.GetAvailableCourses(UID, new RequestsResultListener<ArrayList<Course>>(){
                        @Override
                        public void getResult(ArrayList<Course> result) {
                            allCourses = result;
                            for (Course course: allCourses){
                                for (Course course1: myCourses){
                                    if (course.equals(course1)){
                                        allCourses.remove(course);
                                        break;
                                    }
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnAdd.setEnabled(true);
                                }
                            });
                        }
                    });
                } else{
                    Requests.GetCoursesByUid(UID, new RequestsResultListener<ArrayList<Course>>(){
                        @Override
                        public void getResult(ArrayList<Course> result) {
                            myCourses = result;
                            listViewAdapter = new CourseListViewAdapter(myCourses, context, fragment);
                            myCoursesListView.setAdapter(listViewAdapter);
                            txtMsg.setVisibility(myCourses.size()>0 ? View.GONE: View.VISIBLE);
                        }
                    });
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class JoinCourses extends Thread{
        private ArrayList<String> selectedCoursesUid;
        private String UID;

        public JoinCourses(ArrayList<String> selectedCoursesUid, String UID) {
            this.selectedCoursesUid = selectedCoursesUid;
            this.UID = UID;
        }

        @Override
        public void run() {
            super.run();
            try {
                Requests.JoinCourses(UID, selectedCoursesUid, new RequestsResultListener<Boolean>(){
                    @Override
                    public void getResult(Boolean result) {
                        if (result){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NewCoursesDialog.cancel();
                                    Toast.makeText(context, "joined Courses!",Toast.LENGTH_SHORT).show();
                                    for (Course course: allCourses){
                                        for (String uid: selectedCoursesUid){
                                            if (course.getInfo().getUID().equals(uid)){
                                                myCourses.add(course);
                                                break;
                                            }
                                        }
                                    }
                                    txtMsg.setVisibility(myCourses.size()>0 ? View.GONE: View.VISIBLE);
                                    listViewAdapter = new CourseListViewAdapter(myCourses, context, fragment);
                                    myCoursesListView.setAdapter(listViewAdapter);
                                }
                            });
                        } else{
                            Toast.makeText(context, "Cant join courses you joined before", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class GetCourseData extends Thread{

        private String courseUid, studentUid;

        private Course course;

        public GetCourseData(String courseUid, String studentUid, Course course){
            this.courseUid = courseUid;
            this.studentUid = studentUid;
            this.course = course;
        }

        @Override
        public void run() {
            super.run();
            try {
                Requests.GetCourseData(courseUid, studentUid, new RequestsResultListener<CourseData>(){
                    @Override
                    public void getResult(CourseData result) {
                        HelpFunctions.CurrentCourse = course;
                        HelpFunctions.CurrentCourseData = result;
                        Intent intent = new Intent(context, CourseParticipationPageView.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void MoveToCourse(Course course){
        new GetCourseData(course.getInfo().getUID(), HelpFunctions.CurrentUser.getUID(), course).start();
    }
}