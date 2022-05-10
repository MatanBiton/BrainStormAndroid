package com.mbco.brainstormandroid.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseReview;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherCoursesAdapter extends BaseAdapter {

    private ArrayList<Course> courses;

    private Context context;

    private ArrayList<ArrayList<CourseReview>> reviews;

    public TeacherCoursesAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
        reviews = new ArrayList<>();
        for (Course course: courses){
            new LoadReviews(course.getInfo().getUID()).start();
        }
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

        //new LoadReviews(currentCourse.getInfo().getUID()).start();

        logo.setImageBitmap(currentCourse.getInfo().getLogo());
        txtTitle.setText(currentCourse.getInfo().getName());
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    View dialogView = inflater.inflate(R.layout.teacher_course_info_dialog, null);

                    CircleImageView imgCourseLogo = dialogView.findViewById(R.id.imgCourseLogo);
                    imgCourseLogo.setImageBitmap(currentCourse.getInfo().getLogo());

                    TextView txtCourseTitle = dialogView.findViewById(R.id.txtCourseTitle);
                    txtCourseTitle.setText(currentCourse.getInfo().getName());

                    TextView txtField = dialogView.findViewById(R.id.txtField);
                    txtField.setText(currentCourse.getInfo().getField());

                    TextView txtDescription = dialogView.findViewById(R.id.txtDescription);
                    txtDescription.setText(currentCourse.getInfo().getDescription());

                    ListView dialogListView = dialogView.findViewById(R.id.listView);
                    TeacherReviewAdapter adapter = new TeacherReviewAdapter(reviews.get(i), context);
                    dialogListView.setAdapter(adapter);


                    builder.setView(dialogView);
                    builder.create().show();
                } catch (Exception e){
                    Toast.makeText(context, "please wait!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listView;
    }

    public class LoadReviews extends Thread{
        private String courseUid;

        public LoadReviews(String courseUid){this.courseUid = courseUid;}

        @Override
        public void run() {
            super.run();
            Requests.GetReviewsTeacher(courseUid, new RequestsResultListener<ArrayList<CourseReview>>(){
                @Override
                public void getResult(ArrayList<CourseReview> result) {
                    reviews.add(result);
                }
            });
        }
    }
}
