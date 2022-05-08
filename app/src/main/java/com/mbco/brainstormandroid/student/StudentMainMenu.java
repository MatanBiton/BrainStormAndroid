package com.mbco.brainstormandroid.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.SplashScreen;

public class StudentMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new StudentProfile()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = new Fragment();
                switch (item.getItemId()){
                    case R.id.profile_nav:
                        f = new StudentProfile();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.courses_nav:
                        f = new StudentCourses();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.search_nav:
                        f = new StudentSearch();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.logout_nav:
                        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }
}