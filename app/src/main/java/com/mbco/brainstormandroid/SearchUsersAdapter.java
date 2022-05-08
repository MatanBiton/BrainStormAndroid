package com.mbco.brainstormandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUsersAdapter extends BaseAdapter {

    private ArrayList<User> users;

    private Fragment fragment;

    private Context ctx;

    public SearchUsersAdapter(ArrayList<User> users, Fragment fragment, Context ctx) {
        this.users = users;
        this.fragment = fragment;
        this.ctx = ctx;
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
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView = layoutInflater.inflate(R.layout.user_search_card, null);

        User curUser = users.get(i);

        CircleImageView personalPhoto = listView.findViewById(R.id.personalPhoto);
        personalPhoto.setImageBitmap(curUser.getPhoto());

        TextView txtName = listView.findViewById(R.id.txtName);
        txtName.setText(curUser.getFirstName() + " " + curUser.getLastName());

        ImageButton btnInfo = listView.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                View view = fragment.getLayoutInflater().inflate(R.layout.profile_dialog, null);

                TextView txtFirstName = view.findViewById(R.id.txtFirstName);
                txtFirstName.setText(curUser.getFirstName());
                TextView txtLastName = view.findViewById(R.id.txtLastName);
                txtLastName.setText(curUser.getLastName());
                TextView txtId = view.findViewById(R.id.txtId);
                txtId.setText(String.valueOf(curUser.getId()));
                TextView txtEmail = view.findViewById(R.id.txtEmail);
                txtEmail.setText(curUser.getEmail());
                TextView txtPhone = view.findViewById(R.id.txtPhone);
                txtPhone.setText(curUser.getPhone());
                CircleImageView profilePhoto = view.findViewById(R.id.profilePhoto);
                profilePhoto.setImageBitmap(curUser.getPhoto());
                TextView txtAddress = view.findViewById(R.id.txtAddress);
                txtAddress.setText(curUser.getAddress());
                TextView txtCity = view.findViewById(R.id.txtCity);
                txtCity.setText(curUser.getCity());
                TextView txtBirthDay = view.findViewById(R.id.txtBirthday);
                txtBirthDay.setText(curUser.getBirthDay());

                TextView txtCertification = view.findViewById(R.id.txtCertification);
                TextView txtExperience = view.findViewById(R.id.txtExperience);
                TextView txt1 = view.findViewById(R.id.txt5);
                TextView txt6 = view.findViewById(R.id.txt6);

                if (curUser.getUserType().equals("TEACHER")){
                    txtCertification.setText(((Teacher)curUser).getCertification());

                    txtExperience.setText(String.valueOf(((Teacher)curUser).getExperience()));
                }
                else {
                    txtCertification.setVisibility(View.GONE);
                    txtExperience.setVisibility(View.GONE);
                    txt1.setVisibility(View.GONE);
                    txt6.setVisibility(View.GONE);
                }

                builder.setView(view);
                builder.create().show();
            }
        });

        return listView;
    }
}
