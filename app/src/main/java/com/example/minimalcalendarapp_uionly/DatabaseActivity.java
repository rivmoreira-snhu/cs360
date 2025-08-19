package com.example.minimalcalendarapp_uionly;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Simple activity that shows a table of static mock data
public class DatabaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        // Nothing else needed here for static table
    }
}
