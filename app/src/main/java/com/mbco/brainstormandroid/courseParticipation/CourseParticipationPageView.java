package com.mbco.brainstormandroid.courseParticipation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseAddOn;
import com.mbco.brainstormandroid.models.CourseData;
import com.mbco.brainstormandroid.models.Page;
import com.mbco.brainstormandroid.models.StudentAdvancing;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseParticipationPageView extends AppCompatActivity {

    private TextView txtCourseName, txtPageTitle, txtPageBody, txtMsg, txtClear, txtBack, txtNext;

    private Button btnUpload, btnContinue, btnReturn;

    private CircleImageView imgCourseLogo;

    private Context context;

    private Course course;

    private Stack<Page> pageStack;

    private Uri fileUri;

    private boolean fileSaved;

    private AlertDialog saveContinueDialog;

    private CourseData courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_participation_page_view);

        Initialize();

        ShowInitialData();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFile();
            }
        });

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpload.setText("Upload");
                fileUri = null;
                txtMsg.setText("File must be .pdf formated");
            }
        });

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageStack.peek().getUploadFeature().equals("True")){
                    if (fileUri != null && !fileSaved){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            View v = getLayoutInflater().inflate(R.layout.save_continue_dialog, null);

                            btnReturn = v.findViewById(R.id.btnReturn);
                            btnContinue = v.findViewById(R.id.btnConfirm);

                            btnReturn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    saveContinueDialog.cancel();
                                }
                            });

                            btnContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (pageStack.size()==course.getPages().size()){
                                        saveContinueDialog.cancel();
                                        new SaveCourseData().start();
                                    } else {
                                        try {
                                            if (!fileSaved) {
                                                InputStream iStream = getContentResolver().openInputStream(fileUri);
                                                byte[] inputData = getBytes(iStream);
                                                CourseAddOn addOn = new CourseAddOn(course.getInfo().getUID(),
                                                        HelpFunctions.CurrentUser.getUID(),
                                                        pageStack.size(), "", inputData);
                                                ;
                                                StudentAdvancing advancing = new StudentAdvancing(course.getInfo().getUID(),
                                                        HelpFunctions.CurrentUser.getUID(), pageStack.size(), -1);
                                                courseData.AddAddOn(addOn);
                                                courseData.AddAdvancing(advancing);
                                            }
                                            if (pageStack.size()==course.getPages().size()){
                                                new SaveCourseData().start();
                                            } else{
                                                pageStack.add(course.getPages().get(pageStack.size()));
                                                ShowData();
                                                saveContinueDialog.cancel();
                                            }

                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            builder.setView(v);
                            saveContinueDialog = builder.create();
                            saveContinueDialog.show();

                    } else if(fileSaved){
                        pageStack.add(course.getPages().get(pageStack.size()));
                        ShowData();
                    }
                    else{
                        Toast.makeText(context, "Must Upload the required file!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    if (pageStack.size()==course.getPages().size()){
                        new SaveCourseData().start();
                    } else{
                        StudentAdvancing advancing = new StudentAdvancing(course.getInfo().getUID(),
                                HelpFunctions.CurrentUser.getUID(), pageStack.size(), -1);
                        courseData.AddAdvancing(advancing);
                        pageStack.add(course.getPages().get(pageStack.size()));
                        ShowData();
                    }
                }
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileUri = null;
                pageStack.pop();
                ShowData();
            }
        });

    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void ShowData() {
        txtPageTitle.setText(pageStack.peek().getTitle());
        txtPageBody.setText(pageStack.peek().getBody());
        ShowFileSystem(pageStack.peek());
        if (FileSaved(pageStack.size())){
            fileSaved = true;
            btnUpload.setText("Saved");
            txtMsg.setText("File already uploaded");
            txtClear.setEnabled(false);
            btnUpload.setEnabled(false);
        } else {
            fileSaved = false;
            btnUpload.setText("Upload");
            txtClear.setEnabled(true);
            btnUpload.setEnabled(true);
            fileUri = null;
            txtMsg.setText("File must be .pdf formated");
        }
        txtBack.setEnabled(pageStack.size() > 1);
    }

    private boolean FileSaved(int index) {
        for (CourseAddOn addOn: courseData.getAddOns()){
            if (addOn.getPageNumber()==index){
                return true;
            }
        }
        return false;
    }

    private void UploadFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data != null){
            fileUri = data.getData();
            txtMsg.setText("File added!");
            btnUpload.setText("Change file");
        }
    }

    private void Initialize() {
        txtCourseName = findViewById(R.id.txtCourseName);
        txtPageTitle = findViewById(R.id.txtPageTitle);
        txtPageBody = findViewById(R.id.txtPageBody);
        txtMsg = findViewById(R.id.txtMsg);
        txtClear = findViewById(R.id.txtClear);
        txtBack = findViewById(R.id.txtBack);
        txtNext = findViewById(R.id.txtNext);
        btnUpload = findViewById(R.id.btnUpload);
        imgCourseLogo = findViewById(R.id.imgCourseLogo);

        context = this;

        course = HelpFunctions.CurrentCourse;

        pageStack = new Stack<>();
        pageStack.add(course.getPages().get(0));

        courseData = HelpFunctions.CurrentCourseData;
    }

    private void ShowInitialData() {
        txtCourseName.setText(course.getInfo().getName());
        imgCourseLogo.setImageBitmap(course.getInfo().getLogo());
        txtPageTitle.setText(pageStack.peek().getTitle());
        txtPageBody.setText(pageStack.peek().getBody());
        ShowFileSystem(pageStack.peek());

        txtBack.setEnabled(false);
    }

    private void ShowFileSystem(Page page){
        btnUpload.setVisibility(page.getUploadFeature().equals("True") ? View.VISIBLE : View.GONE);
        txtMsg.setVisibility(page.getUploadFeature().equals("True") ? View.VISIBLE : View.GONE);
        txtClear.setVisibility(page.getUploadFeature().equals("True") ? View.VISIBLE : View.GONE);
    }

    public class SaveCourseData extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.SaveCourseData(new RequestsResultListener<Boolean>(){
                    @Override
                    public void getResult(Boolean result) {
                        if (result){
                            startActivity(new Intent(context, CourseParticipationSummery.class));
                            finish();
                        } else{
                            Toast.makeText(context, "error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}