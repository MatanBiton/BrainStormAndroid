package com.mbco.brainstormandroid.courseCreation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mbco.brainstormandroid.BuildConfig;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.Page;

import java.util.ArrayList;

public class PagesAdapter extends BaseAdapter {

    private ArrayList<Page> pages;

    private Context context;

    public PagesAdapter(ArrayList<Page> pages, Context context){
        this.pages = pages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView = inflater.inflate(R.layout.page_list_template, null);

        TextView txtNumber = listView.findViewById(R.id.txtNumber);
        TextView txtTitle = listView.findViewById(R.id.txtTitle);
        ImageButton btnInformation = listView.findViewById(R.id.btnInformation);

        txtNumber.setText("Page No. " + (i+1));
        txtTitle.setText(pages.get(i).getTitle());
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return listView;
    }
}
