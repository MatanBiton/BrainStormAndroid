package com.mbco.brainstormandroid.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.StudentsCourseInfo;
import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentInCourseAdapter extends BaseAdapter {

    private StudentsCourseInfo courseInfo;

    private Fragment fragment;

    private ArrayList<User> users;

    private Context context;

    public StudentInCourseAdapter(Fragment fragment, StudentsCourseInfo courseInfo, ArrayList<User> users, Context context) {
        this.users =users;
        this.fragment = fragment;
        this.courseInfo = courseInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = fragment.getLayoutInflater().inflate(R.layout.student_grade_list_template, null);

        CircleImageView imgProfile = v.findViewById(R.id.imgProfile);
        TextView txtName = v.findViewById(R.id.txtName);
        TextView txtGrade = v.findViewById(R.id.txtGrade);

        imgProfile.setImageBitmap(users.get(i).getPhoto());
        txtName.setText(users.get(i).GetName());
        txtGrade.setText("Grade: " + courseInfo.getStudents().get(i).getGrade());

        return v;
    }

}
