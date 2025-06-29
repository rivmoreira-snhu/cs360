package com.example.minimalcalendarapp_uionly;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isLoggedIn = getSharedPreferences("appPrefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);

        Button loginButton = findViewById(R.id.loginButton);
        Button smsPermissionButton = findViewById(R.id.requestSmsPermissionButton);
        Button calendarButton = findViewById(R.id.switchToCalendarButton);
        Button viewDatabaseButton = findViewById(R.id.viewDatabaseButton);

        if (!isLoggedIn) {
            // Not logged in: only show login
            smsPermissionButton.setVisibility(View.GONE);
            calendarButton.setVisibility(View.GONE);
            viewDatabaseButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            // Logged in: hide login, show the rest
            loginButton.setVisibility(View.GONE);
            smsPermissionButton.setVisibility(View.VISIBLE);
            calendarButton.setVisibility(View.VISIBLE);
            viewDatabaseButton.setVisibility(View.VISIBLE);
        }

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        smsPermissionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SMSPermissionActivity.class);
            startActivity(intent);
        });

        calendarButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        viewDatabaseButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
            startActivity(intent);
        });
    }





}
