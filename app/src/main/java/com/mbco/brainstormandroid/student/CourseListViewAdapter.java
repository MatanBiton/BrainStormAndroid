package com.mbco.brainstormandroid.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.Course;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseListViewAdapter extends BaseAdapter {

    private ArrayList<Course> courses;

    private Context context;

    private Fragment fragment;

    public CourseListViewAdapter(ArrayList<Course> courses, Context context, Fragment fragment) {
        this.courses = courses;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return courses.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView = inflater.inflate(R.layout.my_course_tab, null);

        CircleImageView logo = listView.findViewById(R.id.logo);
        TextView txtTitle = listView.findViewById(R.id.txtTitle);
        Button btnEnter = listView.findViewById(R.id.btnEnter);

        Course course = courses.get(i);
        logo.setImageBitmap(course.getInfo().getLogo());
        txtTitle.setText(course.getInfo().getName());
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StudentCourses)fragment).MoveToCourse(course);
            }
        });

        return listView;
    }
}
