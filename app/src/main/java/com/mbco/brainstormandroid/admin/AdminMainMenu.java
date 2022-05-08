package com.mbco.brainstormandroid.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mbco.brainstormandroid.R;
import com.mbco.brainstormandroid.SplashScreen;

public class AdminMainMenu extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new AdminSearch()).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = new Fragment();
                switch (item.getItemId()){
                    case R.id.search_nav:
                        f = new AdminSearch();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                                f).commit();
                        break;
                    case R.id.approve_nav:
                        f = new AdminApprove();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, f).commit();
                        break;
                    case R.id.nav_logOut:
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }
}