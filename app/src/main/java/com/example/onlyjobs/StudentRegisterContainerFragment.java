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

public class StudentRegisterContainerFragment extends Fragment {

    private static final String TAG = "StdRegContainerFrag";
    private static final String STATE_IS_ON_STEP_2 = "isCurrentlyOnStep2State";

    private boolean isCurrentlyOnStep2 = false; // Private field
    private OnBackPressedCallback containerOnBackPressedCallback;

    // --- PUBLIC GETTER METHOD ---
    public boolean isCurrentlyOnStep2() {
        return isCurrentlyOnStep2;
    }
    // ----------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        if (savedInstanceState != null) {
            isCurrentlyOnStep2 = savedInstanceState.getBoolean(STATE_IS_ON_STEP_2, false);
            Log.d(TAG, "Restored isCurrentlyOnStep2 from savedInstanceState to: " + isCurrentlyOnStep2);
        }

        containerOnBackPressedCallback = new OnBackPressedCallback(true /* enabled initially */) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Container OnBackPressedCallback invoked. isCurrentlyOnStep2: " + isCurrentlyOnStep2 +
                        ", ChildBackStackCount: " + getChildFragmentManager().getBackStackEntryCount());

                if (isCurrentlyOnStep2 && getChildFragmentManager().getBackStackEntryCount() > 0) {
                    Log.d(TAG, "Container is on Step 2. Popping child back stack (to Step 1).");
                    getChildFragmentManager().popBackStack();
                    // State (isCurrentlyOnStep2) will be updated in onResume or by direct call after pop
                    // Notifying activity will also happen as the backstack change listener or direct call occurs
                    // No need to disable this callback as it successfully handled the event.
                } else {
                    Log.d(TAG, "Container is on Step 1 (or no child to pop). Disabling self and re-triggering back on Activity's dispatcher.");
                    setEnabled(false); // Disable this callback
                    if (getActivity() != null) {
                        // Re-trigger the back press. Since this callback is now disabled,
                        // the Activity's callback (or system default) will take over.
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                    // It's important to re-enable this callback if the user might navigate back
                    // into a state where this container should handle back presses again (e.g., if they
                    // navigate to step 2 again). This can be managed by enabling it when navigating to step 2.
                    // For now, let's re-enable it here for simplicity, assuming it might be needed again.
                    // A more robust solution might tie its enabled state more directly to isCurrentlyOnStep2.
                    setEnabled(true);
                }
            }
        };
        // Add callback to this fragment's dispatcher, tied to its lifecycle (important!)
        requireActivity().getOnBackPressedDispatcher().addCallback(this, containerOnBackPressedCallback);

        // Listen to child back stack changes to update isCurrentlyOnStep2 and notify activity
        getChildFragmentManager().addOnBackStackChangedListener(() -> {
            boolean wasOnStep2 = isCurrentlyOnStep2;
            isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
            Log.d(TAG, "ChildBackStack changed. isCurrentlyOnStep2 is now: " + isCurrentlyOnStep2);
            if (getActivity() instanceof StudentAuthActivity) {
                if (isCurrentlyOnStep2) {
                    // This might be redundant if navigateToStep2 already called it.
                    // ((StudentAuthActivity) getActivity()).onNavigateToStudentRegisterStep2();
                } else if (wasOnStep2 && !isCurrentlyOnStep2) { // Only notify if it *was* step 2 and now it's not
                    ((StudentAuthActivity) getActivity()).onNavigateBackToStudentRegisterStep1();
                }
            }
            // Update the enabled state of the callback
            // containerOnBackPressedCallback.setEnabled(isCurrentlyOnStep2); // More precise enabling
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView. Initial isCurrentlyOnStep2: " + isCurrentlyOnStep2);
        View view = inflater.inflate(R.layout.fragment_student_register_container, container, false);

        if (getChildFragmentManager().findFragmentById(R.id.student_register_fragment_container) == null) {
            // Only load step 1 if the container is empty (e.g., initial creation or after full clear)
            // This check prevents reloading step1 if childFragmentManager already restored its state.
            Log.d(TAG, "Container is empty, loading Step 1 Fragment.");
            loadStep1Fragment(null);
        } else {
            Log.d(TAG, "Container already has a fragment. ChildFragmentManager will restore.");
            // Ensure our state flag is consistent with the restored child fragment manager state
            isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_ON_STEP_2, isCurrentlyOnStep2);
        Log.d(TAG, "onSaveInstanceState: Saved isCurrentlyOnStep2 as " + isCurrentlyOnStep2);
    }

    private void loadStep1Fragment(@Nullable Bundle initialData) {
        Log.d(TAG, "Executing loadStep1Fragment");
        StudentRegisterStep1Fragment step1Fragment = new StudentRegisterStep1Fragment();
        if (initialData != null) {
            step1Fragment.setArguments(initialData);
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.student_register_fragment_container, step1Fragment, "STEP1");
        // Do NOT add the initial fragment to the backstack if you want "back" from step 1
        // to be handled by the parent (Activity or ViewPager).
        transaction.commit();

        isCurrentlyOnStep2 = false; // Explicitly set state
        if (getActivity() instanceof StudentAuthActivity) {
            ((StudentAuthActivity) getActivity()).onNavigateBackToStudentRegisterStep1();
        }
        // containerOnBackPressedCallback.setEnabled(false); // Enable only when on step 2
    }

    public void navigateToStep2(Bundle dataFromStep1) {
        Log.d(TAG, "Executing navigateToStep2");
        StudentRegisterStep2Fragment step2Fragment = new StudentRegisterStep2Fragment();
        step2Fragment.setArguments(dataFromStep1);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.student_register_fragment_container, step2Fragment, "STEP2");
        transaction.addToBackStack("STEP1_TO_STEP2"); // Tag for clarity
        transaction.commit();

        isCurrentlyOnStep2 = true; // Explicitly set state
        if (getActivity() instanceof StudentAuthActivity) {
            ((StudentAuthActivity) getActivity()).onNavigateToStudentRegisterStep2();
        }
        // containerOnBackPressedCallback.setEnabled(true); // Enable callback as we are on step 2
    }


    @Override
    public void onResume() {
        super.onResume();
        // Sync state, especially after configuration changes where child FM might restore.
        isCurrentlyOnStep2 = getChildFragmentManager().getBackStackEntryCount() > 0;
        Log.d(TAG, "onResume. isCurrentlyOnStep2: " + isCurrentlyOnStep2 +
                ", ChildBackStackCount: " + getChildFragmentManager().getBackStackEntryCount());
        // containerOnBackPressedCallback.setEnabled(isCurrentlyOnStep2); // Update enabled state precisely
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
