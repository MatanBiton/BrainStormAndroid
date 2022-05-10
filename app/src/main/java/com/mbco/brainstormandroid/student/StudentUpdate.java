package com.mbco.brainstormandroid.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mbco.brainstormandroid.HelpFunctions;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.Register;
import com.mbco.brainstormandroid.Requests;
import com.mbco.brainstormandroid.RequestsResultListener;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentUpdate extends AppCompatActivity {

    private CircleImageView editPhoto;

    private EditText editName, editId, editAddress, editPhone;

    private Spinner spinnerPhone, spinnerCity;

    private Button btnDate, btnSave;

    private CheckBox checkBox;

    private ImageButton btnBack;

    private User currentUser, newUser;

    private Context context;

    private ArrayList<String> Phones, cities;

    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);

        Initialize();

        SetCurrentValues();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentMainMenu.class);
                startActivity(intent);
                finish();
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SetEnabled(b);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUser = new User(currentUser.getUID(), Integer.parseInt(editId.getText().toString()),
                        currentUser.getEmail(), currentUser.getPassword(),
                        editName.getText().toString().split(" ")[0],
                        editName.getText().toString().split(" ")[1],
                        photo, editAddress.getText().toString(),spinnerCity.getSelectedItem().toString(),
                        spinnerPhone.getSelectedItem().toString() + editPhone.getText().toString(),
                        btnDate.getText().toString(), currentUser.getUserType());
                if (!newUser.Compare(currentUser)){
                    new UpdateUser().start();
                } else{
                    Toast.makeText(context, "no new Values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Initialize() {
        context = this;

        editPhoto = findViewById(R.id.editPhoto);
        editName = findViewById(R.id.editFullName);
        editId = findViewById(R.id.editID);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        spinnerPhone = findViewById(R.id.spinnerPhone);
        spinnerCity = findViewById(R.id.spinnerCity);
        btnDate = findViewById(R.id.btnDate);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        checkBox = findViewById(R.id.checkBox);

        currentUser = HelpFunctions.CurrentUser;

        photo = currentUser.getPhoto();

        Phones = new ArrayList<>();
        Phones.add("050");
        Phones.add("051");
        Phones.add("052");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.reg_spinner_item, Phones);
        spinnerPhone.setAdapter(arrayAdapter);


        cities = new ArrayList<>();
        cities.add("Select Your City");
        cities.add("Ramat gan");
        cities.add("Tel Aviv");
        arrayAdapter = new ArrayAdapter<>(context, R.layout.reg_spinner_item, cities);
        spinnerCity.setAdapter(arrayAdapter);
    }


    private void SetCurrentValues(){
        editPhoto.setImageBitmap(currentUser.getPhoto());
        editName.setText(currentUser.GetName());
        editId.setText(String.valueOf(currentUser.getId()));
        editAddress.setText(currentUser.getAddress());
        editPhone.setText(currentUser.getPhone().substring(3));
        String initial = currentUser.getPhone().substring(0,3);
        for (String temp: Phones){
            if (temp.equals(initial)){
                spinnerPhone.setSelection(Phones.indexOf(temp));
            }
        }
        for (String city: cities){
            if (city.equals(currentUser.getCity())){
                spinnerCity.setSelection(cities.indexOf(city));
            }
        } // improve?
        btnDate.setText(currentUser.getBirthDay());
    }

    private void SetEnabled(boolean value){
        editPhoto.setEnabled(value);
        editName.setEnabled(value);
        editId.setEnabled(value);
        editAddress.setEnabled(value);
        editPhone.setEnabled(value);
        btnDate.setEnabled(value);
        spinnerCity.setClickable(value);
        spinnerPhone.setClickable(value);
    }

    public class UpdateUser extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Requests.UpdateUser(newUser, new RequestsResultListener<Boolean>(){
                    @Override
                    public void getResult(Boolean result) {
                        if(result){
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, StudentMainMenu.class);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}