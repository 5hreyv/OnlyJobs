package com.example.onlyjobs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText; // Assuming you have EditTexts
import android.widget.Toast;   // For validation messages

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmployerRegisterStep1Fragment extends Fragment {

    private static final String TAG = "EmpRegStep1Frag";

    // Declare your EditTexts for employer details
    private EditText edtFullName, edtEmail, edtPhone, edtCompany, edtIndustry;
    private Button btnNext;
    private EmployerRegisterContainerFragment containerFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (getParentFragment() instanceof EmployerRegisterContainerFragment) {
            containerFragment = (EmployerRegisterContainerFragment) getParentFragment();
            Log.d(TAG, "Successfully attached to EmployerRegisterContainerFragment");
        } else {
            Log.e(TAG, "Parent fragment is NOT EmployerRegisterContainerFragment. Actual: " +
                    (getParentFragment() != null ? getParentFragment().getClass().getName() : "null"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup cont,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_employer_register_step1, cont, false);

        // Initialize your EditTexts - ENSURE THESE IDs MATCH YOUR XML
        edtFullName = view.findViewById(R.id.editTextEmployerFullName);
        edtEmail = view.findViewById(R.id.editTextEmployerEmail); // ID from your XML
        edtPhone = view.findViewById(R.id.editTextEmployerPhone);
        edtCompany = view.findViewById(R.id.editTextEmployerCompany);
        edtIndustry = view.findViewById(R.id.editTextEmployerIndustry);
        btnNext = view.findViewById(R.id.btnNextEmployerStep); // Your XML uses btnNextEmployerStep

        // TODO: Apply text color fixes for EditTexts if not done in XML
        // edtFullName.setTextColor(getResources().getColor(android.R.color.black, null));
        // ... etc.

        btnNext.setOnClickListener(v -> {
            Log.d(TAG, "Next button clicked");

            String fullName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String company = edtCompany.getText().toString().trim();
            String industry = edtIndustry.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || company.isEmpty() || industry.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Add more specific validation (e.g., email format, phone format)

            Bundle bundle = new Bundle();
            bundle.putString("fullName", fullName);
            bundle.putString("email", email);
            bundle.putString("phone", phone);
            bundle.putString("company", company);
            bundle.putString("industry", industry);
            // Add any other data you need for step 2

            if (containerFragment != null) {
                Log.d(TAG, "Telling container fragment to navigate to Step 2");
                containerFragment.navigateToStep2(bundle);
            } else {
                Log.e(TAG, "containerFragment is null. Cannot navigate to Employer Step 2.");
                Toast.makeText(getContext(), "Navigation Error. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (containerFragment == null && getParentFragment() instanceof EmployerRegisterContainerFragment) {
            containerFragment = (EmployerRegisterContainerFragment) getParentFragment();
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
        containerFragment = null;
    }
}
