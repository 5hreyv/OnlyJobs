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

public class StudentRegisterStep2Fragment extends Fragment {

    private EditText edtPassword, edtConfirmPassword;
    private Button btnSetPassword;

    private String name, university, branch, department, email, phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_register_step2, container, false);

        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnSetPassword = view.findViewById(R.id.btnSetPassword);

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

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // All details are collected here
                Toast.makeText(getActivity(),
                        "Registered Successfully!\n" + name + " - " + email,
                        Toast.LENGTH_LONG).show();

                // Later: Send data + password to Firebase
            }
        });

        return view;
    }
}
