package com.example.minimalcalendarapp_uionly;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/*
 * LoginActivity handles both login and sign-up logic.
 * It checks against our local SQLite DB and stores new users if needed.
 */
public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SQLite DB helper
        dbHelper = new DatabaseHelper(this);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);

        // Declare buttons early so they can be referenced below
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        // Add listeners early (not inside click block)
        usernameInput.addTextChangedListener(new SimpleTextWatcher(() ->
                validateInputs(usernameInput, passwordInput, loginButton, signupButton)));

        passwordInput.addTextChangedListener(new SimpleTextWatcher(() ->
                validateInputs(usernameInput, passwordInput, loginButton, signupButton)));

        // Log In Button logic
        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Read from DB and validate credentials
            if (dbHelper.checkUserCredentials(username, password)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                getSharedPreferences("appPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Sign Up Button logic
        signupButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            boolean success = dbHelper.addUser(username, password);
            if (success) {
                getSharedPreferences("appPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .apply();
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });

        // Disable both buttons initially
        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
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

        boolean enable = !username.isEmpty() && !password.isEmpty();
        loginButton.setEnabled(enable);
        signupButton.setEnabled(enable);
    }
}
