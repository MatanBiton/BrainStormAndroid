package com.mbco.brainstormandroid.teacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseInfo;
import com.mbco.brainstormandroid.models.CourseReview;
import com.mbco.brainstormandroid.models.StudentInCourse;
import com.mbco.brainstormandroid.models.StudentsCourseInfo;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeacherProfile extends Fragment {

    private View rootView;

    private TextView txtName, txtEmail, txtPhone, txtAddress, txtMsg;

    private GridView gridView;

    private CircleImageView circleImageView;

    private Context context;

    private Fragment fragment;

    private ArrayList<StudentsCourseInfo> courseInfos;

    private ArrayList<Course> courses;

    private ListView listView;

    private ArrayList<CourseReview> coursesReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        Initialize();

        SetValues(HelpFunctions.CurrentUser);

        new LoadStudentInCourses(HelpFunctions.CurrentUser.getUID()).start();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View v = getLayoutInflater().inflate(R.layout.finished_students_dialog, null);

                listView = v.findViewById(R.id.listView);
                ArrayList<String> uids = new ArrayList<>();

                for (StudentInCourse student: courseInfos.get(i).getStudents()){
                    uids.add(student.getStudentUid());
                }
                new GetStudents(courseInfos.get(i)).start();

                builder.setView(v);
                Dialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.show();
            }
        });

        return rootView;
    }

    public void Initialize(){
        txtName = rootView.findViewById(R.id.txtName);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPhone = rootView.findViewById(R.id.txtPhone);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        circleImageView = rootView.findViewById(R.id.circleImageView);
        txtMsg = rootView.findViewById(R.id.txtMsg);
        gridView = rootView.findViewById(R.id.gridView);

        context = getContext();
        fragment = this;

        courses = new ArrayList<>();
    }

    public void SetValues(User user){
        txtName.setText(user.getFirstName() + " " + user.getLastName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        txtAddress.setText(user.getAddress());
        circleImageView.setImageBitmap(user.getPhoto());
    }

    private class LoadStudentInCourses extends Thread{

        private String creator_uid;

        public LoadStudentInCourses(String creator_uid){this.creator_uid = creator_uid;}

        @Override
        public void run() {
            super.run();
            Requests.LoadStudentInCourses(creator_uid, new RequestsResultListener<ArrayList<StudentsCourseInfo>>(){
                @Override
                public void getResult(ArrayList<StudentsCourseInfo> result) {
                    courseInfos = result;
                    CoursesGridAdapter adapter = new CoursesGridAdapter(courseInfos, fragment);
                    gridView.setAdapter(adapter);
                    txtMsg.setVisibility(courseInfos.size() > 0 ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

   private class GetStudents extends Thread{
        private StudentsCourseInfo courseInfo;
        public GetStudents(StudentsCourseInfo courseInfo){
            this.courseInfo = courseInfo;
        }

       @Override
       public void run() {
           super.run();
           Requests.GetStudents(courseInfo.getCourseInfo().getUID(), new RequestsResultListener<ArrayList<User>>(){
               @Override
               public void getResult(ArrayList<User> result) {
                   StudentInCourseAdapter adapter = new StudentInCourseAdapter(fragment, courseInfo, result, context);
                   listView.setAdapter(adapter);
               }
           });
       }
   }
}