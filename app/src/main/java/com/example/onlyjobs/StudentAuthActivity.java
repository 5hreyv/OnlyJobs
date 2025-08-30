package com.example.onlyjobs;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentAuthActivity extends AppCompatActivity {

    private static final String TAG = "StudentAuthActivity";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StudentAuthPagerAdapter adapter;

    private boolean isRegisterFlowOnStep2ByContainer = false;
    private OnBackPressedCallback activityOnBackPressedCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_auth);
        Log.d(TAG, "onCreate");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapter = new StudentAuthPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Login");
                    } else if (position == 1) {
                        tab.setText("Register");
                    }
                }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "Tab selected: " + tab.getPosition());
                if (tab.getPosition() != 1 && isRegisterFlowOnStep2ByContainer) {
                    Log.d(TAG, "Navigated away from Register tab while container was on Step 2. Resetting global state.");
                    isRegisterFlowOnStep2ByContainer = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        activityOnBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Activity OnBackPressedCallback invoked. Current Tab: " + viewPager.getCurrentItem() +
                        ", Container on Step 2 (global flag): " + isRegisterFlowOnStep2ByContainer);

                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragmentInViewPager = fm.findFragmentByTag("f" + viewPager.getCurrentItem());

                if (viewPager.getCurrentItem() == 1 && currentFragmentInViewPager instanceof StudentRegisterContainerFragment) {
                    StudentRegisterContainerFragment container = (StudentRegisterContainerFragment) currentFragmentInViewPager;
                    Log.d(TAG, "On Register Tab. Container actual step 2 state: " + container.isCurrentlyOnStep2());

                    // The StudentRegisterContainerFragment's OnBackPressedCallback (also registered with this
                    // activity's dispatcher) is expected to be evaluated.
                    // If the container is on Step 2, its callback will pop its child to Step 1.
                    // If the container is on Step 1, its callback will disable itself and re-trigger
                    // the back press on the activity's dispatcher. This will cause this
                    // Activity's callback to run again.

                    if (container.isCurrentlyOnStep2()) {
                        Log.d(TAG, "Container reports it's on Step 2. Its OnBackPressedCallback should handle navigation to Step 1.");
                        // We rely on the container's callback to manage this.
                        // If its callback handles the pop, the event might be consumed or it might re-trigger.
                        // If it re-triggers after popping, then this method is called again, and
                        // container.isCurrentlyOnStep2() would be false, leading to the else block.
                        // For now, if the container *says* it's on step 2, we assume its dispatcher callback will act.
                        // If it doesn't consume the event fully, or if this callback is structured with higher precedence
                        // in a way that it gets called first, then this logic might need refinement.
                        // However, with both added to the same dispatcher, the most recently added enabled one typically fires.
                        // Let's assume the container's callback (if enabled) will fire.
                        // No direct action here; dispatcher will route to the appropriate enabled callback.
                        // If the container's callback handles it, this one might not even run for that specific event.
                        // If the container's callback *doesn't* handle it and re-triggers, then this will run.
                        return; // Allow the dispatcher to process the container's callback if it's enabled and handles it.
                    } else {
                        // Container is on Step 1 (or reports it is).
                        Log.d(TAG, "Container is on Step 1. Proceeding to activity's default back press (e.g., switch tab or finish).");
                        fallbackToDefaultBackPress();
                    }
                } else {
                    // Not on the Register tab, or the current fragment isn't the expected container
                    Log.d(TAG, "Not on Register Tab or not the correct container. Proceeding to activity's default back press.");
                    fallbackToDefaultBackPress();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, activityOnBackPressedCallback);
    }

    private void fallbackToDefaultBackPress() {
        Log.d(TAG, "Executing fallbackToDefaultBackPress.");
        if (activityOnBackPressedCallback != null) {
            activityOnBackPressedCallback.setEnabled(false);
        }
        super.onBackPressed(); // Standard way to invoke default behavior
        if (activityOnBackPressedCallback != null) {
            activityOnBackPressedCallback.setEnabled(true);
        }
    }

    public void onNavigateToStudentRegisterStep2() {
        Log.d(TAG, "Activity notified by container: Navigated to Student Register Step 2");
        isRegisterFlowOnStep2ByContainer = true;
    }

    public void onNavigateBackToStudentRegisterStep1() {
        Log.d(TAG, "Activity notified by container: Navigated back to Student Register Step 1");
        isRegisterFlowOnStep2ByContainer = false;
    }

    @Override
    protected void onStart() { super.onStart(); Log.d(TAG, "onStart"); }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragmentInViewPager = getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 1 && currentFragmentInViewPager instanceof StudentRegisterContainerFragment) {
            StudentRegisterContainerFragment container = (StudentRegisterContainerFragment) currentFragmentInViewPager;
            isRegisterFlowOnStep2ByContainer = container.isCurrentlyOnStep2();
        } else {
            isRegisterFlowOnStep2ByContainer = false;
        }
        Log.d(TAG, "onResume. Global isStep2ByContainer flag: " + isRegisterFlowOnStep2ByContainer);
    }

    @Override
    protected void onPause() { super.onPause(); Log.d(TAG, "onPause"); }

    @Override
    protected void onStop() { super.onStop(); Log.d(TAG, "onStop"); }

    @Override
    protected void onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy"); }
}
