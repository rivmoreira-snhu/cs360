package com.example.minimalcalendarapp_uionly;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSPermissionActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1001;
    private TextView permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        // Find views after setting content view
        Button requestButton = findViewById(R.id.requestPermissionButton);
        Button backButton = findViewById(R.id.goBackButton);
        Button openSettingsButton = findViewById(R.id.openSettingsButton);
        permissionStatus = findViewById(R.id.permissionStatus);

        // Trigger permission request
        requestButton.setOnClickListener(view -> checkPermission());

        // Go back to previous screen
        backButton.setOnClickListener(v -> finish());

        //  Open app settings for manual permission management
        openSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent); // Opens app settings
        });
    }

    // Check + request SMS permission
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE
            );
        } else {
            permissionStatus.setText("Permission already granted.");
            sendTestSMS();  // <- send test notification
        }
    }

    // Handle user's response to permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionStatus.setText("SMS permission granted!");
                sendTestSMS();  // Send once permission is granted
            } else {
                permissionStatus.setText("Permission denied. App will work but no SMS alerts.");
            }
        }
    }

    // Send test SMS to confirm permission works
    private void sendTestSMS() {
        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(
                    "5554", // emulator number; replace with real phone number when testing on device
                    null,
                    "Test alert: SMS notifications are active!",
                    null,
                    null
            );
            permissionStatus.setText("SMS sent successfully.");
        } catch (Exception e) {
            permissionStatus.setText("Failed to send SMS: " + e.getMessage());
        }
    }

}
