package com.example.onlyjobs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText; // Assuming you have password EditTexts
import android.widget.Toast;   // For messages

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmployerRegisterStep2Fragment extends Fragment {

    private static final String TAG = "EmpRegStep2Frag";

    private EditText edtPassword, edtConfirmPassword;
    private Button btnRegister;

    // To receive data from Step 1 (if any was passed)
    private String fullName, email, phone, company, industry;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_employer_register_step2, container, false);
        // MAKE SURE FILENAME IS fragment_employer_register_step2.xml

        edtPassword = view.findViewById(R.id.editTextEmployerPassword); // Check ID in your XML
        edtConfirmPassword = view.findViewById(R.id.editTextEmployerConfirmPassword); // Check ID
        btnRegister = view.findViewById(R.id.btnEmployerRegister); // Check ID

        // TODO: Apply text color fixes for EditTexts if not done in XML

        if (getArguments() != null) {
            fullName = getArguments().getString("fullName");
            email = getArguments().getString("email");
            // ... retrieve other arguments if passed from step 1
            Log.d(TAG, "Received arguments: Email - " + email);
        }

        btnRegister.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked");
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter and confirm password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) { // Example validation
                Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Implement actual employer registration logic here
            // This would involve taking all data (from Step 1 bundle + password)
            // and sending it to your backend/Firebase.
            Toast.makeText(getActivity(), "Registration logic for: " + email + " (TODO)", Toast.LENGTH_LONG).show();

            // After successful registration, you might want to:
            // - Navigate to a login screen or a main employer dashboard
            // - Clear the back stack of registration steps
            // Example: Pop back to the Login tab if successful
            // if (getActivity() instanceof EmployerAuthActivity) {
            // ((EmployerAuthActivity) getActivity()).viewPager.setCurrentItem(0); // Go to login
            // Clear the child backstack of the container if needed
            // if (getParentFragment() instanceof EmployerRegisterContainerFragment) {
            // ((EmployerRegisterContainerFragment) getParentFragment()).clearChildBackStack();
            // }
            // }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}
