package com.mbco.brainstormandroid.student;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.CourseInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends BaseAdapter {

    private ArrayList<CourseInfo> info;

    private Fragment fragment;

    public ReviewsAdapter(ArrayList<CourseInfo> info, Fragment fragment) {
        this.info = info;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return info.size();
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
        View v= fragment.getLayoutInflater().inflate(R.layout.review_grid_template, null);

        CircleImageView imgLogo = v.findViewById(R.id.imgLogo);
        imgLogo.setImageBitmap(info.get(i).getLogo());

        TextView txtTitle = v.findViewById(R.id.txtTitle);
        txtTitle.setText(info.get(i).getName());

        return v;
    }
}
