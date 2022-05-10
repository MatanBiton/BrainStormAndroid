package com.mbco.brainstormandroid.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.CourseInfo;
import com.mbco.brainstormandroid.models.CourseReview;
import com.mbco.brainstormandroid.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends Fragment {

    private View rootView;

    private TextView txtName, txtEmail, txtPhone, txtAddress, txtMsg;

    private GridView gridView;

    private CircleImageView circleImageView;

    private Context context;

    private Fragment fragment;

    private ArrayList<CourseInfo> coursesInfo;

    private ArrayList<CourseReview> coursesReviews;

    private Button btnProfile;

    private ImageButton btnReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);

        Initialize();

        SetValues(HelpFunctions.CurrentUser);


        new LoadReviews(HelpFunctions.CurrentUser.getUID()).start();


        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseInfo info = coursesInfo.get(i);
                CourseReview review = coursesReviews.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View v = getLayoutInflater().inflate(R.layout.review_dialog_template, null);
            }
        });*/

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentUpdate.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                        inflate(R.layout.report_dialog, container, false);

                builder.setView(v);

                AlertDialog reportDialog = builder.create();

                EditText editSubject = v.findViewById(R.id.editSubject);
                EditText editBody = v.findViewById(R.id.editBody);

                Button btnCompose = v.findViewById(R.id.btnCompose);
                btnCompose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject = editSubject.getText().toString();
                        String body = editBody.getText().toString();
                        if(!subject.isEmpty() && !body.isEmpty()){
                            ComposeEmail(subject, body);
                            reportDialog.cancel();
                        } else{
                            Toast.makeText(context, "must enter valid inputs!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                reportDialog.show();
            }
        });

        return rootView;
    }

    private void ComposeEmail(String subject, String body) {
        String[] addresses = new String[1];
        addresses[0] = "matanb1905@gmail.com";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void Initialize(){
        txtName = rootView.findViewById(R.id.txtName);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPhone = rootView.findViewById(R.id.txtPhone);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        circleImageView = rootView.findViewById(R.id.circleImageView);
        txtMsg = rootView.findViewById(R.id.txtMsg);
        gridView = rootView.findViewById(R.id.gridView);
        btnProfile = rootView.findViewById(R.id.btnProfile);
        btnReport = rootView.findViewById(R.id.btnReport);

        context = getContext();
        fragment = this;

        coursesInfo = new ArrayList<>();
        coursesReviews = new ArrayList<>();
    }

    public void SetValues(User user){
        txtName.setText(user.getFirstName() + " " + user.getLastName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        txtAddress.setText(user.getAddress());
        circleImageView.setImageBitmap(user.getPhoto());
    }

    public class LoadReviews extends Thread{

        private String uid;

        public LoadReviews(String uid){
            this.uid = uid;
        }

        @Override
        public void run() {
            super.run();
            Requests.GetReviews(uid, new RequestsResultListener<ArrayList<ArrayList<Object>>>(){
                @Override
                public void getResult(ArrayList<ArrayList<Object>> result) {
                    ArrayList<CourseInfo> coursesInfo = new ArrayList<>();
                    ArrayList<CourseReview> coursesReviews = new ArrayList<>();
                    for (int i = 0; i < result.get(0).size(); i++) {
                        coursesInfo.add((CourseInfo) result.get(0).get(i));
                        coursesReviews.add((CourseReview) result.get(1).get(i));
                    }
                    txtMsg.setVisibility(coursesInfo.size()>0?View.GONE:View.VISIBLE);
                    ReviewsAdapter adapter = new ReviewsAdapter(coursesInfo, fragment);
                    gridView.setAdapter(adapter);

                }
            });
        }
    }
}