package com.example.onlyjobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Add these required imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentRegisterStep2Fragment extends Fragment {

    private EditText edtPassword, edtConfirmPassword;
    private Button btnSetPassword;
    private FirebaseAuth mAuth; // FirebaseAuth instance

    // Fields to hold data from Step 1
    private String name, university, branch, department, email, phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_register_step2, container, false);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Find the views from the layout
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnSetPassword = view.findViewById(R.id.btnSetPassword);

        // Get the data passed from Step 1
        if (getArguments() != null) {
            name = getArguments().getString("name");
            university = getArguments().getString("university");
            branch = getArguments().getString("branch");
            department = getArguments().getString("department");
            email = getArguments().getString("email");
            phone = getArguments().getString("phone");
        }

        btnSetPassword.setOnClickListener(v -> {
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Perform validation
            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter and confirm your password", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // If validation passes, call the registerUser method
                registerUser(email, password);
            }
        });

        return view;
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Authentication was successful, now save the user's details
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserDetails(userId);
                    } else {
                        // If registration fails, show a message
                        Toast.makeText(getContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserDetails(String userId) {
        // Get a reference to the 'users' node in your Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Create a new User object with all the collected data
        User newUser = new User(userId, name, email, university, branch); // Assuming User class constructor

        // Save the user object under their unique user ID
        databaseReference.child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                    // TODO: Navigate to the Student Dashboard Activity here
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save user details.", Toast.LENGTH_SHORT).show();
                });
    }
}