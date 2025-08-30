package com.example.onlyjobs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "StdRegStep1Frag";

    private EditText edtName, edtUniversity, edtBranch,
            edtDepartment, edtEmail, edtPhone;
    private Button btnNext;
    private StudentRegisterContainerFragment containerFragment; // Reference to parent container

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        // Get parent fragment if this fragment is nested
        if (getParentFragment() instanceof StudentRegisterContainerFragment) {
            containerFragment = (StudentRegisterContainerFragment) getParentFragment();
            Log.d(TAG, "Successfully attached to StudentRegisterContainerFragment");
        } else {
            Log.e(TAG, "Parent fragment is NOT StudentRegisterContainerFragment. Actual parent: " +
                    (getParentFragment() != null ? getParentFragment().getClass().getName() : "null"));
            // This is a critical error in setup if the expectation is to be inside the container.
            // Consider throwing an IllegalStateException or ensuring this path is never hit.
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup cont,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_student_register_step1, cont, false);

        edtName = view.findViewById(R.id.edtName);
        edtUniversity = view.findViewById(R.id.edtUniversity);
        edtBranch = view.findViewById(R.id.edtBranch);
        edtDepartment = view.findViewById(R.id.edtDepartment);
        edtEmail = view.findViewById(R.id.edtEmailRegister);
        edtPhone = view.findViewById(R.id.edtPhone);
        btnNext = view.findViewById(R.id.btnNext);

        // Apply text color fixes here if not already done in XML
        // edtName.setTextColor(getResources().getColor(android.R.color.black, null));
        // ... for all EditTexts and Buttons ...

        btnNext.setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");
            String name = edtName.getText().toString().trim();
            String university = edtUniversity.getText().toString().trim();
            String branch = edtBranch.getText().toString().trim();
            String department = edtDepartment.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();


            if (name.isEmpty() || university.isEmpty() || branch.isEmpty() ||
                    department.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("university", university);
            bundle.putString("branch", branch);
            bundle.putString("department", department);
            bundle.putString("email", email);
            bundle.putString("phone", phone);

            if (containerFragment != null) {
                Log.d(TAG, "Telling container fragment to navigate to Step 2");
                containerFragment.navigateToStep2(bundle);
            } else {
                Log.e(TAG, "containerFragment is null. Cannot navigate to Step 2. This indicates a setup problem.");
                Toast.makeText(getContext(), "Navigation Error. Please try again or report this issue.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // Ensure containerFragment reference is up-to-date, especially if fragment was restored.
        if (containerFragment == null && getParentFragment() instanceof StudentRegisterContainerFragment) {
            containerFragment = (StudentRegisterContainerFragment) getParentFragment();
            Log.d(TAG, "containerFragment re-assigned in onResume");
        } else if (containerFragment == null) {
            Log.w(TAG, "onResume: containerFragment is still null.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        containerFragment = null; // Clean up reference
    }
}
