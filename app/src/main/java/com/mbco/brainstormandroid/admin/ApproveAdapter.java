package com.mbco.brainstormandroid.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.Teacher;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApproveAdapter extends BaseAdapter implements View.OnClickListener{

    private ArrayList<Object> data;

    private Fragment fragment;

    private Context context;

    private String type;

    private Object CurrantItem;

    public ApproveAdapter(ArrayList<Object> data, Fragment fragment, Context context, String type) {
        this.data = data;
        this.fragment = fragment;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return data.size();
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

        View listView = inflater.inflate(R.layout.request_approve_template, null);

        CircleImageView circleImageView = listView.findViewById(R.id.profileImage);
        TextView txtName = listView.findViewById(R.id.txtName);
        Button btnAccept = listView.findViewById(R.id.btnAccept);
        Button btnReject = listView.findViewById(R.id.btnReject);
        ImageButton btnInfo = listView.findViewById(R.id.btnInfo);

        CurrantItem = data.get(i);

        if ("teacher".equals(type)) {
            Teacher teacher = (Teacher) data.get(i);
            circleImageView.setImageBitmap(teacher.getPhoto());
            txtName.setText(String.format("%s %s", teacher.getFirstName(), teacher.getLastName()));
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    View v = fragment.getLayoutInflater().inflate(R.layout.request_teacher_profile, null);

                    TextView txtFirstName = v.findViewById(R.id.txtFirstName);
                    txtFirstName.setText(teacher.getFirstName());
                    TextView txtLastName = v.findViewById(R.id.txtLastName);
                    txtLastName.setText(teacher.getLastName());
                    TextView txtEmail = v.findViewById(R.id.txtEmail);
                    txtEmail.setText(teacher.getEmail());
                    TextView txtPhone = v.findViewById(R.id.txtPhone);
                    txtPhone.setText(teacher.getPhone());
                    TextView txtCertification = v.findViewById(R.id.txtCertification);
                    txtCertification.setText(teacher.getCertification());
                    TextView txtExperience = v.findViewById(R.id.txtExperience);
                    txtExperience.setText(String.valueOf(teacher.getExperience()));
                    CircleImageView profilePhoto = v.findViewById(R.id.profilePhoto);
                    profilePhoto.setImageBitmap(teacher.getPhoto());

                    builder.setView(v);
                    builder.create().show();
                }
            });
        }
        else{
            txtName.setText(((Course)CurrantItem).getInfo().getName());
            circleImageView.setImageBitmap(((Course)CurrantItem).getInfo().getLogo());
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);

        return listView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        boolean approved;
        switch (view.getId()){
            case R.id.btnAccept:
                approved = true;
                break;
            case R.id.btnReject:
                approved = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        try {
            Requests.DeleteRequest(type.equals("teacher") ? ((Teacher)CurrantItem).getUID() :
                            ((Course)CurrantItem).getInfo().getUID(),approved,
                    new RequestsResultListener<Boolean>(){
                        @Override
                        public void getResult(Boolean result) {
                            if (result){
                                ((AdminApprove)fragment).UpdateListview(CurrantItem);
                                Toast.makeText(context, approved?"approved!":"rejected!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
