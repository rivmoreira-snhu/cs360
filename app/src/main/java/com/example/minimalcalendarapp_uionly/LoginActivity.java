package com.example.minimalcalendarapp_uionly;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
 * LoginActivity handles both login and sign-up logic.
 * Enhancements include navigation fixes, input validation improvements,
 * and password hashing integration.
 */
public class LoginActivityEnhanced extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        usernameInput.addTextChangedListener(new SimpleTextWatcher(() ->
                validateInputs(usernameInput, passwordInput, loginButton, signupButton)));

        passwordInput.addTextChangedListener(new SimpleTextWatcher(() ->
                validateInputs(usernameInput, passwordInput, loginButton, signupButton)));

        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String hashedPassword = SecurityUtils.hashPassword(password);

            try {
                if (dbHelper.checkUserCredentials(username, hashedPassword)) {
                    handleLoginSuccess();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String hashedPassword = SecurityUtils.hashPassword(password);

            try {
                boolean success = dbHelper.addUser(username, hashedPassword);
                if (success) {
                    handleLoginSuccess();
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
    }

    private void handleLoginSuccess() {
        getSharedPreferences("appPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", true)
                .apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class SimpleTextWatcher implements android.text.TextWatcher {
        private final Runnable callback;

        SimpleTextWatcher(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(android.text.Editable s) { callback.run(); }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    }

    private void validateInputs(EditText usernameInput, EditText passwordInput,
                                Button loginButton, Button signupButton) {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        boolean enable = !username.isEmpty() && !password.isEmpty() && password.length() >= 6;
        loginButton.setEnabled(enable);
        signupButton.setEnabled(enable);
    }
}