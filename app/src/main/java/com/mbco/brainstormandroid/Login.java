package com.mbco.brainstormandroid;

import static java.lang.Compiler.enable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class Login extends AppCompatActivity {

    private TextView txtSign;

    private EditText editEmail, editPassword;

    private Button btnLog;

    private final Requests requests = Requests.getInstance();

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtSign = findViewById(R.id.txtSign);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLog = findViewById(R.id.btnLog);


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                enable(false);
                new LoginThread(email, password).start();
            }
        });

        txtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Register.class));
                finish();
            }
        });
    }

    public void enable(boolean enable) {
        editEmail.setEnabled(enable);
        editPassword.setEnabled(enable);
        btnLog.setEnabled(enable);
        txtSign.setEnabled(enable);
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
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                    enable(true);
                                } else {
                                    startActivity(new Intent(context, SplashScreen.class));
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