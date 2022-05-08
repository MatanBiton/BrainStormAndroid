package com.mbco.brainstormandroid.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.Course;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseSelectionAdapter extends BaseAdapter {

    private ArrayList<Course> courses;

    private ArrayList<String> selectedCoursesUid;
    private Context context;

    public CourseSelectionAdapter(ArrayList<Course> courses, Context context){
        this.courses = courses;
        this.context = context;
        this.selectedCoursesUid = new ArrayList<>();
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

        View listView = inflater.inflate(R.layout.course_list_selection_template, null);

        CircleImageView logo = listView.findViewById(R.id.logo);
        TextView txtTitle = listView.findViewById(R.id.txtTitle);
        ImageButton btnInformation = listView.findViewById(R.id.btnInformation);
        CheckBox checkBox = listView.findViewById(R.id.checkBox);

        Course course = courses.get(i);

        logo.setImageBitmap(course.getInfo().getLogo());
        txtTitle.setText(course.getInfo().getName());

        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectedCoursesUid.add(course.getInfo().getUID());
                } else{
                    try{
                        selectedCoursesUid.remove(course.getInfo().getUID());
                    } catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        return listView;
    }

    public ArrayList<String> getSelectedCoursesUid(){return selectedCoursesUid;}
}
