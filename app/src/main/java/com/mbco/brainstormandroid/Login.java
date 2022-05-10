package com.mbco.brainstormandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.admin.AdminMainMenu;
import com.mbco.brainstormandroid.models.User;
import com.mbco.brainstormandroid.student.StudentMainMenu;
import com.mbco.brainstormandroid.teacher.TeacherMainMenu;

import org.json.JSONException;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private TextView txtSign;

    private EditText editEmail, editPassword;

    private Button btnLog;

    private ImageButton btnSwitch;

    private boolean IsOn;

    private final Requests requests = Requests.getInstance();

    private final Context context = this;

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtSign = findViewById(R.id.txtSign);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLog = findViewById(R.id.btnLog);
        btnSwitch = findViewById(R.id.btnSwitch);


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelpFunctions.ShowWaitDialog(context, activity);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                enable(false);
                if (checkInputs(email, password)){

                    new LoginThread(email, password).start();
                } else{
                    HelpFunctions.CancelWaitDialog();
                    enable(true);
                    Toast.makeText(context, "Invalid Inputs!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        txtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Register.class));
                finish();
            }
        });

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if (!isVisible){
                    editEmail.clearFocus();
                    editPassword.clearFocus();
                }
            }
        });

        IsOn = false;
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = editPassword.getSelectionStart();
                if(IsOn == true){
                    IsOn = false;
                    editPassword.setInputType(129);
                    btnSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else{
                    IsOn = true;
                    editPassword.setInputType(97);
                    btnSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
                editPassword.setSelection(pos);
            }
        });
    }

    public void enable(boolean enable) {
        editEmail.setEnabled(enable);
        editPassword.setEnabled(enable);
        btnLog.setEnabled(enable);
        txtSign.setEnabled(enable);
    }

    public boolean checkInputs(String email, String password){
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailpattern = Pattern.compile(emailRegex);
        String passwordregex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        Pattern passwordpattern = Pattern.compile(passwordregex);
        return passwordpattern.matcher(password).matches() && emailpattern.matcher(email).matches();
    }

    public class  LoginThread extends Thread{

        private final String email;
        private final String password;

        public LoginThread(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        public void run() {
            super.run();
            try {
                requests.Login(email, password, new RequestsResultListener<User>() {
                    @Override
                    public void getResult(User result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result == null){
                                    HelpFunctions.CancelWaitDialog();
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                    enable(true);
                                } else {
                                    HelpFunctions.CurrentUser = result;
                                    Intent intent;
                                    if (HelpFunctions.IsAdmin()) {
                                        intent = new Intent(context, AdminMainMenu.class);
                                    }
                                    else if (result.getUserType().equals("student")){
                                        intent = new Intent(context, StudentMainMenu.class);
                                    } else{
                                        intent = new Intent(context, TeacherMainMenu.class);
                                    }
                                    HelpFunctions.CancelWaitDialog();
                                    startActivity(intent);
                                    finish();
                                }
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