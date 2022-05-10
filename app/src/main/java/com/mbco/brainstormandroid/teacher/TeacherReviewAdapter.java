package com.mbco.brainstormandroid.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.CourseReview;
import com.mbco.brainstormandroid.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherReviewAdapter extends BaseAdapter {

    private ArrayList<CourseReview> reviews;

    private Context context;

    public TeacherReviewAdapter(ArrayList<CourseReview> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviews.size();
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
        View v = inflater.inflate(R.layout.course_review_template, null);

        User currentUser = reviews.get(i).getStudent();
        CircleImageView imgStudent = v.findViewById(R.id.imgStudent);
        imgStudent.setImageBitmap(currentUser.getPhoto());

        TextView txtName = v.findViewById(R.id.txtName);
        txtName.setText(currentUser.GetName());

        TextView txtComment = v.findViewById(R.id.txtComment);
        txtComment.setText(reviews.get(i).getComment());

        TextView txtRating = v.findViewById(R.id.txtRating);
        txtRating.setText(String.valueOf(reviews.get(i).getRating()));

        return v;
    }
}
