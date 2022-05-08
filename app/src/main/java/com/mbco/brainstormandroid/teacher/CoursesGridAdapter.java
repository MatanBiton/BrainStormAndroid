package com.mbco.brainstormandroid.teacher;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.StudentsCourseInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CoursesGridAdapter extends BaseAdapter {

    private ArrayList<StudentsCourseInfo> courseInfos;

    private Fragment fragment;

    public CoursesGridAdapter(ArrayList<StudentsCourseInfo> courseInfos, Fragment fragment) {
        this.courseInfos = courseInfos;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return courseInfos.size();
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

        View v = fragment.getLayoutInflater().inflate(R.layout.teacher_course_grid_dialog, null);

        CircleImageView imgLogo = v.findViewById(R.id.imgLogo);
        imgLogo.setImageBitmap(courseInfos.get(i).getCourseInfo().getLogo());

        TextView txtTitle = v.findViewById(R.id.txtTitle);
        txtTitle.setText(courseInfos.get(i).getCourseInfo().getName());

        return v;
    }
}
