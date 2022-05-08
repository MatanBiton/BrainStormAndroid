package com.mbco.brainstormandroid.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.Course;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherCoursesAdapter extends BaseAdapter {

    private ArrayList<Course> courses;

    private Context context;

    public TeacherCoursesAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
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

        View listView = inflater.inflate(R.layout.teacher_course_list_template, null);

        CircleImageView logo = listView.findViewById(R.id.logo);
        TextView txtTitle = listView.findViewById(R.id.txtTitle);
        ImageButton btnInformation = listView.findViewById(R.id.btnInformation);

        Course currentCourse = courses.get(i);

        logo.setImageBitmap(currentCourse.getInfo().getLogo());
        txtTitle.setText(currentCourse.getInfo().getName());
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return listView;
    }
}
