package com.example.androidintegration;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

import com.clevertap.android.sdk.CleverTapAPI;

public class MainActivity extends AppCompatActivity  {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private EditText identityEditText, nameEditText, emailEditText, phoneEditText;
    private Button registerButton;
    private CleverTapAPI clevertapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CleverTap
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        //Set Log level to VERBOSE
        clevertapDefaultInstance.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

        //Notification and Lat, Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the location permission is not granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the location permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }

        //Location
        Location location = clevertapDefaultInstance.getLocation();
        clevertapDefaultInstance.setLocation(location);

        clevertapDefaultInstance.enableDeviceNetworkInfoReporting(true);

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });


        // Initialize UI elements - updated to use EditText and Button
        identityEditText = findViewById(R.id.identityEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.registerButton);

        // Set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get input values
        String identity = identityEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate inputs
        if (identity.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate phone format
        if (!isValidPhone(phone)) {
            Toast.makeText(this, "Please enter a valid phone number (+1234567890)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare CleverTap user profile update
        HashMap<String, Object> profileUpdate = new HashMap<>();

        // Add required profile attributes
        profileUpdate.put("Name", name);
        profileUpdate.put("Identity", identity);
        profileUpdate.put("Email", email);
        profileUpdate.put("Phone", phone);

        // Default notification preferences
        profileUpdate.put("MSG-email", true);
        profileUpdate.put("MSG-push", true);
        profileUpdate.put("MSG-sms", false);
        profileUpdate.put("MSG-whatsapp", false);

        // Send user login/profile update to CleverTap
        if (clevertapDefaultInstance != null) {
            // Record Registration Started event
            // recordEvent("Registration Started", profileUpdate);

            clevertapDefaultInstance.onUserLogin(profileUpdate);

            // Record Registration Success event
            // recordEvent("Registration Success", profileUpdate);

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userName", name);
            startActivity(intent);
            clearForm();
            finish();

        } else {
            Toast.makeText(this, "Error: CleverTap instance not available", Toast.LENGTH_SHORT).show();
            /* recordEvent("Registration Failed", new HashMap<String, Object>() {{
                put("Reason", "CleverTap Not Available");
            }});*/
        }
    }

    // recordEvent Function
    /* private void recordEvent(String eventName, HashMap<String, Object> properties) {
        if (clevertapDefaultInstance != null) {
            // Add common properties
            properties.put("platform", "Android");
            properties.put("os_version", Build.VERSION.RELEASE);
            properties.put("device_model", Build.MODEL);

            // Record the event
            clevertapDefaultInstance.pushEvent(eventName, properties);
        }
    }*/

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhone(String phone) {
        String phonePattern = "\\+[0-9]+";
        return phone.matches(phonePattern);
    }

    private void clearForm() {
        // Clear all form fields after successful registration
        identityEditText.setText("");
        nameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
    }
}