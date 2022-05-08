package com.mbco.brainstormandroid.teacher;

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
import com.mbco.brainstormandroid.admin.AdminApprove;
import com.mbco.brainstormandroid.admin.AdminSearch;
import com.mbco.brainstormandroid.student.StudentCourses;
import com.mbco.brainstormandroid.student.StudentProfile;
import com.mbco.brainstormandroid.student.StudentSearch;

public class TeacherMainMenu extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_menu);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new TeacherProfile()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = new Fragment();
                switch (item.getItemId()){
                    case R.id.profile_nav:
                        f = new TeacherProfile();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.courses_nav:
                        f = new TeacherCourses();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.search_nav:
                        f = new TeacherSearch();
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