package com.mbco.brainstormandroid.courseCreation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.Page;
import com.mbco.brainstormandroid.teacher.TeacherMainMenu;

import org.json.JSONException;

import java.util.ArrayList;

public class CourseCreationPageCreation extends AppCompatActivity {

    private ImageButton btnReturn, btnAdd;

    private Button btnCreate;

    private ListView listView;

    private Context context;

    private AlertDialog returnDialog, addPageDialog;

    private EditText editTitle;

    private EditText editInformation;

    private CheckBox checkBox;

    private ArrayList<Page> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_creation_page_creation);

        Initialize();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = getLayoutInflater().inflate(
                        R.layout.return_from_course_creation_dialog, null);

                Button btnYes = view.findViewById(R.id.btnYes);
                Button btnNo = view.findViewById(R.id.btnNo);


                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CourseCreationDetails.class);
                        startActivity(intent);
                        finish();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        returnDialog.cancel();
                    }
                });

                builder.setView(view);
                returnDialog = builder.create();
                returnDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = getLayoutInflater().inflate(R.layout.create_page_dialog,null);

                editTitle = view.findViewById(R.id.editTitle);
                editInformation = view.findViewById(R.id.editInformation);
                checkBox = view.findViewById(R.id.checkBox);
                Button btnAdd = view.findViewById(R.id.btnAdd);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = editTitle.getText().toString();
                        String information =editInformation.getText().toString();
                        if (validateInputs(title, information)){
                            pages.add(new Page(HelpFunctions.CurrentCourseCreationInfo.getUID(),
                                    title, information, checkBox.isChecked() ? "True" : "False"));
                            Toast.makeText(context, "Page Added!", Toast.LENGTH_SHORT).show();
                            ShowPages();
                            addPageDialog.cancel();
                        } else{
                            Toast.makeText(context, "Invalid Inputs!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addPageDialog.cancel();
                    }
                });

                builder.setView(view);
                addPageDialog = builder.create();
                addPageDialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pages.size()!=0){
                    new CreateCourseThread().start();
                }
            }
        });
    }

    private void Initialize(){
        btnReturn = findViewById(R.id.btnReturn);
        btnAdd = findViewById(R.id.btnAdd);
        btnCreate = findViewById(R.id.btnCreate);
        listView = findViewById(R.id.listView);
        context = this;
        pages = new ArrayList<>();
    }

    private boolean validateInputs(String title, String information){
        return !title.isEmpty() && !information.isEmpty();
    }

    private void ShowPages(){
        PagesAdapter adapter = new PagesAdapter(pages, context);
        listView.setAdapter(adapter);
    }

    private class CreateCourseThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.CreateCourse(HelpFunctions.CurrentCourseCreationInfo, pages,
                        new RequestsResultListener<Boolean>(){
                            @Override
                            public void getResult(Boolean result) {
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(context, TeacherMainMenu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}