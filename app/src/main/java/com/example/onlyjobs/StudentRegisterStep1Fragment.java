package com.example.onlyjobs;

import android.content.Context;
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

public class StudentRegisterStep1Fragment extends Fragment {

    private EditText edtName, edtUniversity, edtBranch, edtDepartment, edtEmail, edtPhone;
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_register_step1, container, false);

        // Find all the UI components from the XML layout
        edtName = view.findViewById(R.id.edtName);
        edtUniversity = view.findViewById(R.id.edtUniversity);
        edtBranch = view.findViewById(R.id.edtBranch);
        edtDepartment = view.findViewById(R.id.edtDepartment);
        edtEmail = view.findViewById(R.id.edtEmailRegister);
        edtPhone = view.findViewById(R.id.edtPhone);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            // Get text from all fields and remove leading/trailing spaces
            String name = edtName.getText().toString().trim();
            String university = edtUniversity.getText().toString().trim();
            String branch = edtBranch.getText().toString().trim();
            String department = edtDepartment.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            // Validate that no field is empty
            if (name.isEmpty() || university.isEmpty() || branch.isEmpty() ||
                    department.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return; // Stop the process if any field is empty
            }

            // Create a Bundle to pass data to the next fragment
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("university", university);
            bundle.putString("branch", branch);
            bundle.putString("department", department);
            bundle.putString("email", email);
            bundle.putString("phone", phone);

            // Navigate to the next step using the parent container fragment
            if (getParentFragment() instanceof StudentRegisterContainerFragment) {
                ((StudentRegisterContainerFragment) getParentFragment()).navigateToStep2(bundle);
            }
        });

        return view;
    }
}