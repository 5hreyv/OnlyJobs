package com.example.onlyjobs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EmployerRegisterContainerFragment extends Fragment {

    private static final String TAG = "EmpRegContainerFrag";
    private static final String STATE_IS_ON_STEP_2_EMP = "isCurrentlyOnStep2EmpState";

    private boolean isCurrentlyOnStep2 = false;
    private OnBackPressedCallback containerOnBackPressedCallback;

    public boolean isCurrentlyOnStep2() {
        return isCurrentlyOnStep2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        if (savedInstanceState != null) {
            isCurrentlyOnStep2 = savedInstanceState.getBoolean(STATE_IS_ON_STEP_2_EMP, false);
            Log.d(TAG, "Restored isCurrentlyOnStep2 from savedInstanceState to: " + isCurrentlyOnStep2);
        }

        containerOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Container OnBackPressedCallback. isCurrentlyOnStep2: " + isCurrentlyOnStep2 +
                        ", ChildBackStack: " + getChildFragmentManager().getBackStackEntryCount());
                if (isCurrentlyOnStep2 && getChildFragmentManager().getBackStackEntryCount() > 0) {
                    Log.d(TAG, "Container on Step 2. Popping child to Step 1.");
                    getChildFragmentManager().popBackStack();
                    // isCurrentlyOnStep2 will be updated by the backstack listener or onResume
                } else {
                    Log.d(TAG, "Container on Step 1. Disabling self, re-triggering on Activity.");
                    setEnabled(false);
                    if (getActivity() != null) {
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                    setEnabled(true);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, containerOnBackPressedCallback);

        getChildFragmentManager().addOnBackStackChangedListener(() -> {
            boolean wasOnStep2 = isCurrentlyOnStep2;
            isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
            Log.d(TAG, "ChildBackStack changed. isCurrentlyOnStep2 is now: " + isCurrentlyOnStep2);
            if (getActivity() instanceof EmployerAuthActivity) {
                if (isCurrentlyOnStep2) {
                    // ((EmployerAuthActivity) getActivity()).onNavigateToEmployerRegisterStep2(); // Optional if already done
                } else if (wasOnStep2 && !isCurrentlyOnStep2) {
                    ((EmployerAuthActivity) getActivity()).onNavigateBackToEmployerRegisterStep1();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView. Initial isCurrentlyOnStep2: " + isCurrentlyOnStep2);
        View view = inflater.inflate(R.layout.fragment_employer_register_container, container, false);
        if (getChildFragmentManager().findFragmentById(R.id.employer_register_fragment_container) == null) {
            Log.d(TAG, "Container empty, loading Step 1.");
            loadStep1Fragment(null);
        } else {
            Log.d(TAG, "Container already has fragment. ChildFM will restore.");
            isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_ON_STEP_2_EMP, isCurrentlyOnStep2);
        Log.d(TAG, "onSaveInstanceState: Saved isCurrentlyOnStep2 as " + isCurrentlyOnStep2);
    }

    private void loadStep1Fragment(@Nullable Bundle initialData) {
        Log.d(TAG, "Loading Employer Step 1 Fragment");
        EmployerRegisterStep1Fragment step1Fragment = new EmployerRegisterStep1Fragment();
        if (initialData != null) {
            step1Fragment.setArguments(initialData);
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.employer_register_fragment_container, step1Fragment, "EMP_STEP1");
        transaction.commit();
        isCurrentlyOnStep2 = false;
        if (getActivity() instanceof EmployerAuthActivity) {
            ((EmployerAuthActivity) getActivity()).onNavigateBackToEmployerRegisterStep1();
        }
    }

    public void navigateToStep2(Bundle dataFromStep1) {
        Log.d(TAG, "Navigating to Employer Step 2");
        EmployerRegisterStep2Fragment step2Fragment = new EmployerRegisterStep2Fragment();
        step2Fragment.setArguments(dataFromStep1); // Pass data if needed

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.employer_register_fragment_container, step2Fragment, "EMP_STEP2");
        transaction.addToBackStack("EMP_STEP1_TO_STEP2");
        transaction.commit();
        isCurrentlyOnStep2 = true;
        if (getActivity() instanceof EmployerAuthActivity) {
            ((EmployerAuthActivity) getActivity()).onNavigateToEmployerRegisterStep2();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
        Log.d(TAG, "onResume. isCurrentlyOnStep2: " + isCurrentlyOnStep2 +
                ", ChildBackStackCount: " + getChildFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
