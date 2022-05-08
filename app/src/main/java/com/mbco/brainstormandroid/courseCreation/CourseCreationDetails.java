package com.mbco.brainstormandroid.courseCreation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.models.CourseInfo;
import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.teacher.TeacherMainMenu;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseCreationDetails extends AppCompatActivity {

    private ImageButton btnReturn;

    private EditText editName, editDescription;

    private CircleImageView btnLogo;

    private Button btnSave;

    private Context context;

    private AlertDialog returnDialog;

    private Bitmap logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_creation_details);

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
                        Intent intent = new Intent(context, TeacherMainMenu.class);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String description = editDescription.getText().toString();
                if (validInputs(name, description)){
                    CourseInfo info = new CourseInfo();
                    info.setName(name);
                    info.setDescription(description);
                    info.setLogo(logo);
                    info.setCreatorUid(HelpFunctions.CurrentUser.getUID());
                    info.setField(((Teacher)HelpFunctions.CurrentUser).getCertification());
                    HelpFunctions.CurrentCourseCreationInfo = info;
                    try {
                        HelpFunctions.CurrentCourseCreationInfo.GenerateUid();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        Log.println(Log.ASSERT, "error",e.getMessage());
                    }
                    Intent intent = new Intent(context, CourseCreationPageCreation.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(context, "Invalid Input!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1  && data!=null)
        {
            try {
                logo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                btnLogo.setImageBitmap(logo);
            }
            catch (IOException e) {
                btnLogo.setImageBitmap(logo);
                e.printStackTrace();
            }
        }
    }

    private void Initialize(){
        btnReturn = findViewById(R.id.btnReturn);
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        btnLogo = findViewById(R.id.logo);
        btnSave = findViewById(R.id.btnSave);

        context = this;
    }

    private boolean validInputs(String name, String description){
        Bitmap defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.add);
        return !name.isEmpty() && !description.isEmpty() && !defaultPhoto.sameAs(logo);
    }
}