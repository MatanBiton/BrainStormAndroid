package com.mbco.brainstormandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    private final Context context = this;

    private TextView txtLogin;

    private EditText editEmail, editPassword, editFullName,
             editID, editAddress, editPhone;

    private Button btnDate, btnType, btnRegister;

    private CircleImageView editPhoto;

    private Spinner spinnerCity, spinnerPhone;

    private Bitmap photoBit;

    private DatePickerDialog datePickerDialog;

    private final Requests request = Requests.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ConnectWidgets();

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Login.class));
                finish();
            }
        });

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterThread(GetUser()).start();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int curYear = calendar.get(Calendar.YEAR);
                int curMonth = calendar.get(Calendar.MONTH);
                int curDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month+1) + "/" + year;
                        btnDate.setText(date);
                    }
                },curYear, curMonth, curDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1  && data!=null)
        {
            try {
                photoBit = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                editPhoto.setImageBitmap(photoBit);
            }
            catch (IOException e) {
                editPhoto.setImageBitmap(photoBit);
                e.printStackTrace();
            }
        }
    }

    public User GetUser(){
        int id = Integer.parseInt(editID.getText().toString());
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String name = editFullName.getText().toString();
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        String address = editAddress.getText().toString();
        String city = spinnerCity.getSelectedItem().toString();
        String phone = spinnerPhone.getSelectedItem().toString() + editPhone.getText().toString();
        String year = String.valueOf(datePickerDialog.getDatePicker().getYear());
        String month = String.valueOf(datePickerDialog.getDatePicker().getMonth());
        String day = String.valueOf(datePickerDialog.getDatePicker().getDayOfMonth());
        String date = day + "/" + month + "/" + year;
        String userType = "";
        return new User("", id, email, password, firstName, lastName, photoBit, address, city, phone, date, userType);
    }

    public void ConnectWidgets(){
        txtLogin = findViewById(R.id.txtLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editFullName = findViewById(R.id.editFullName);
        editID = findViewById(R.id.editID);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        editPhoto = findViewById(R.id.editPhoto);
        spinnerPhone = findViewById(R.id.spinnerPhone);
        spinnerCity = findViewById(R.id.spinnerCity);
        btnRegister = findViewById(R.id.btnRegister);
        btnType = findViewById(R.id.btnType);
        btnDate = findViewById(R.id.btnDate);
    }

    public class RegisterThread extends Thread{

        private final User user;

        public RegisterThread(User user){
            this.user = user;
        }

        public void run(){
            super.run();
            try {
                request.Register(user, new RequestsResultListener<User>() {
                    @Override
                    public void getResult(User result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null){
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
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