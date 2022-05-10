package com.mbco.brainstormandroid.courseParticipation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.student.StudentMainMenu;

import org.json.JSONException;

public class CourseParticipationSummery extends AppCompatActivity {

    private EditText editComment;

    private RatingBar ratingBar;

    private Button btnFinish;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_participation_summery);

        editComment = findViewById(R.id.editComment);
        ratingBar = findViewById(R.id.ratingBar);
        btnFinish = findViewById(R.id.btnFinish);

        context = this;

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editComment.getText().toString().isEmpty()){
                    Toast.makeText(context, "must leave a comment!", Toast.LENGTH_SHORT).show();
                } else{
                    new LeaveReview().start();
                }
            }
        });
    }

    public class LeaveReview extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.LeaveReview(HelpFunctions.CurrentCourse.getInfo().getUID(),
                        HelpFunctions.CurrentUser.getUID(), Math.round(ratingBar.getRating()),
                        editComment.getText().toString(), new RequestsResultListener<Boolean>(){
                            @Override
                            public void getResult(Boolean result) {
                                if (result){
                                    Toast.makeText(context, "Course Finished!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, StudentMainMenu.class);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}