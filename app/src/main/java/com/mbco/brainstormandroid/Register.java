package com.mbco.brainstormandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.models.User;
import com.mbco.brainstormandroid.student.StudentMainMenu;
import com.mbco.brainstormandroid.teacher.TeacherMainMenu;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    private final Context context = this;

    private TextView txtLogin;

    private EditText editEmail, editPassword, editFullName,
             editID, editAddress, editPhone, editCertification, editExperience;

    private Button btnDate, btnType, btnRegister, btnStudent, btnTeacher;

    private CircleImageView editPhoto;

    private Spinner spinnerCity, spinnerPhone;

    private LinearLayout linearLayout;

    private Bitmap photoBit;

    private LayoutInflater inflater;

    private DatePickerDialog datePickerDialog;

    private final Requests request = Requests.getInstance();

    private Boolean student;

    private View UserTypeView;

    private AlertDialog userTypeDialog;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inflater = getLayoutInflater();

        initializeUserTypeDialog();

        ConnectWidgets();

        InitializeSpinners();

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
                if (checkValidInputs()){
                    HelpFunctions.ShowWaitDialog(context, activity);
                    new RegisterThread(GetUser()).start();
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int curYear = calendar.get(Calendar.YEAR);
                int curMonth = calendar.get(Calendar.MONTH);
                int curDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
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
                userTypeDialog.show();
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student = true;
                editCertification.setEnabled(false);
                editExperience.setEnabled(false);
                btnStudent.setBackgroundColor(getResources().getColor(R.color.brightblue, getTheme()));
                btnTeacher.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                btnType.setText("Student");
                linearLayout.setVisibility(View.GONE);
            }
        });

        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student = false;
                editCertification.setEnabled(true);
                editExperience.setEnabled(true);
                btnStudent.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
                btnTeacher.setBackgroundColor(getResources().getColor(R.color.brightblue, getTheme()));
                btnType.setText("Teacher");
                linearLayout.setVisibility(View.VISIBLE);
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
                    editFullName.clearFocus();
                    editID.clearFocus();
                    editAddress.clearFocus();
                    editPhone.clearFocus();
                    spinnerPhone.clearFocus();
                    spinnerCity.clearFocus();
                    editCertification.clearFocus();
                    editExperience.clearFocus();
                }
            }
        });
    }

    public boolean checkValidInputs(){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String name = editFullName.getText().toString();
        String id = editID.getText().toString();
        String address = editAddress.getText().toString();
        String phone = spinnerPhone.getSelectedItem().toString() + editPhone.getText().toString();
        String city = spinnerCity.getSelectedItem().toString();
        String certificate = editCertification.getText().toString();
        String experience = editExperience.getText().toString();
        boolean type = student != null;
        boolean pic = photoBit != null;
        if (type && pic){
            boolean Bemail = validations.emailValid(email);
            boolean Bpassword = validations.passwordValid(password);
            boolean Bname = validations.nameValid(name);
            boolean Bid =       validations.idValid(id);
            boolean Baddress =       validations.addressValid(address);
            boolean Bphone =       validations.phoneValid(phone);
            boolean Bcity =      validations.cityValid(city);
            boolean Bpic =      validations.photoValid(context, photoBit);
            boolean base = Bemail && Bpassword && Bname && Bid && Baddress && Bphone && Bcity && Bpic;
            return student ? base : base && validations.certificateValid(certificate)
                    && validations.experienceValid(experience);
        }
        return false;
    }

    public static class validations {
        private static boolean emailValid(String email){
            if (email == null || email.isEmpty()) {
                return false;
            }
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Log.println(Log.ASSERT,"email",String.valueOf(pattern.matcher(email).matches()));
            return pattern.matcher(email).matches();
        }
        private static boolean passwordValid(String password){
            if (password == null || password.isEmpty()) {
                return false;
            }
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(regex);
            Log.println(Log.ASSERT,"password",String.valueOf(pattern.matcher(password).matches()));
            return pattern.matcher(password).matches();
        }
        private static boolean nameValid(String name){
            if (name == null || name.isEmpty()){
                return false;
            }
            String[] parts = name.split(" ");
            if (parts.length == 2) {
                for (String part : parts) {
                    String regex = ".*[a-zA-Z].";
                    Pattern pattern = Pattern.compile(regex);
                    if (!part.chars().allMatch(Character::isLetter)) {
                        Log.println(Log.ASSERT,"name","false");
                        return false;
                    }
                }
            }
            Log.println(Log.ASSERT,"name",String.valueOf(true));
            return true;
        }
        private static boolean idValid(String id){
            if (id == null || id.isEmpty()){
                return false;
            }
            Log.println(Log.ASSERT,"id",String.valueOf(id.length()==9));
            return id.length() == 9;
        }
        private static boolean addressValid(String address){
            if (address == null || address.isEmpty()){
                return false;
            }
            address = address.replace(" ", "");
            Log.println(Log.ASSERT,"address",String.valueOf(!Pattern.compile("[0-9]").matcher(address).find()));
            return !Pattern.compile("[0-9]").matcher(address).find();
        }
        private static boolean phoneValid(String phone){
            if (phone == null || phone.isEmpty()){
                return false;
            }
            Log.println(Log.ASSERT,"phone",String.valueOf(phone.length() == 10));
            return phone.length() == 10;
        }
        private static boolean cityValid(String city){
            if (city == null || city.isEmpty()){
                return false;
            }
            Log.println(Log.ASSERT,"city",String.valueOf(!city.equals("Select Your City")));
            return !city.equals("Select Your City");
        }
        private static boolean photoValid(Context context, Bitmap photo){
            Bitmap defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.add);
            Log.println(Log.ASSERT,"pic",String.valueOf(!defaultPhoto.sameAs(photo)));
            return !defaultPhoto.sameAs(photo);
        }
        private static boolean certificateValid(String certificate){
            if (certificate == null || certificate.isEmpty()){
                return false;
            }
            Log.println(Log.ASSERT,"certification",String.valueOf(Pattern.compile(".*[a-zA-Z].").matcher(certificate).matches()));
            return Pattern.compile(".*[a-zA-Z].").matcher(certificate).matches();
        }
        private static boolean experienceValid(String experience){
            //Log.println(Log.ASSERT,"experience",String.valueOf(experience != null || experience.isEmpty()));
            return experience != null && !experience.isEmpty();
        }
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
        String userType = student ? "student" : "teacher";
        String certification = !student ? editCertification.getText().toString() : "";
        int experience = !student ? Integer.parseInt(editExperience.getText().toString()) : -1;
        User user =  new User("", id, email, password, firstName, lastName, photoBit, address, city, phone, date, userType);
        return student ? user : new Teacher(user, certification, experience);

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
        btnStudent = UserTypeView.findViewById(R.id.btnStudent);
        btnTeacher = UserTypeView.findViewById(R.id.btnTeacher);
        editCertification = UserTypeView.findViewById(R.id.editCertification);
        editExperience = UserTypeView.findViewById(R.id.editExperience);
        linearLayout = UserTypeView.findViewById(R.id.linearLayout);
        activity = this;
    }

    public void InitializeSpinners(){
        ArrayList<String> Phones = new ArrayList<>();
        Phones.add("050");
        Phones.add("051");
        Phones.add("052");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.reg_spinner_item, Phones);
        spinnerPhone.setAdapter(arrayAdapter);


        ArrayList<String> cities = new ArrayList<>();
        cities.add("Select Your City");
        cities.add("Ramat gan");
        cities.add("Tel Aviv");
        arrayAdapter = new ArrayAdapter<>(context, R.layout.reg_spinner_item, cities);
        spinnerCity.setAdapter(arrayAdapter);
    }

    public void initializeUserTypeDialog(){
        UserTypeView = inflater.inflate(R.layout.user_type_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(UserTypeView);
        userTypeDialog = builder.create();
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
                                    HelpFunctions.CurrentUser = result;
                                    Intent intent = new Intent(context,
                                            result.getUserType().equals("student")?
                                            StudentMainMenu.class: TeacherMainMenu.class);
                                    HelpFunctions.CancelWaitDialog();
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                                    HelpFunctions.CancelWaitDialog();
                                }
                            }
                        });
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                HelpFunctions.CancelWaitDialog();
            }
        }

    }
}