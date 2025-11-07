package com.example.onlyjobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnGetStarted;
    private TextView txtLearnMore;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // link with your XML

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        btnGetStarted = findViewById(R.id.btnGetStarted);
        txtLearnMore = findViewById(R.id.txtLearnMore);

        // Handle "Get Started" button click
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RolesActivity.class);
                startActivity(intent);
            }
        });

        // Handle "Learn More" text click
        txtLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLearnMorePage(v);
            }
        });

        // Check if the app was opened via Firebase Email Link
        handleEmailLinkSignIn();
    }

    // -------------------------------------------------------------------------
    // EMAIL LINK HANDLER
    // -------------------------------------------------------------------------
    private void handleEmailLinkSignIn() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String emailLink = data.toString();

            // Check if this is a valid Firebase sign-in link
            if (auth.isSignInWithEmailLink(emailLink)) {

                // Retrieve email & role saved when link was sent
                SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
                String email = prefs.getString("emailForSignIn", null);
                String userType = prefs.getString("userType", "student"); // default: student

                if (email == null) {
                    Toast.makeText(this, "Email not found. Please re-enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailLink(email, emailLink)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();

                                    String uid = task.getResult().getUser().getUid();
                                    String emailAddress = task.getResult().getUser().getEmail();

                                    // Save role + email to Firestore
                                    saveUserRoleToFirestore(uid, emailAddress, userType);

                                    // Redirect user to correct dashboard
                                    if (userType.equals("employer")) {
                                        startActivity(new Intent(MainActivity.this, EmployerDashboardActivity.class));
                                    } else {
                                        startActivity(new Intent(MainActivity.this, StudentDashboardActivity.class));
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("EmailLinkAuth", "Error signing in", task.getException());
                                }
                            }
                        });
            }
        }
    }

    private void saveUserRoleToFirestore(String uid, String email, String userType) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("role", userType);
        userData.put("timestamp", System.currentTimeMillis());

        db.collection("users").document(uid)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User role saved successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error saving user role", e));
    }

    // -------------------------------------------------------------------------
    // EXISTING LEARN MORE HANDLER
    // -------------------------------------------------------------------------
    public void openLearnMorePage(View view) {
        Intent intent = new Intent(this, LearnMoreActivity.class);
        startActivity(intent);
    }
}
