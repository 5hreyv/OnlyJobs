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

public class EmployerAuthActivity extends AppCompatActivity {

    private static final String TAG = "EmployerAuthActivity";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EmployerAuthPagerAdapter pagerAdapter; // Renamed from adapter for clarity

    private boolean isEmpRegisterFlowOnStep2ByContainer = false;
    private OnBackPressedCallback activityOnBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_auth); // Ensure this has employerTabLayout and employerViewPager
        Log.d(TAG, "onCreate");

        tabLayout = findViewById(R.id.employerTabLayout); // ID from your XML
        viewPager = findViewById(R.id.employerViewPager); // ID from your XML

        pagerAdapter = new EmployerAuthPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Login" : "Register")
        ).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "Tab selected: " + tab.getPosition());
                if (tab.getPosition() != 1 && isEmpRegisterFlowOnStep2ByContainer) {
                    Log.d(TAG, "Navigated away from Employer Register tab. Resetting global step 2 state.");
                    isEmpRegisterFlowOnStep2ByContainer = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        activityOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Activity OnBackPressedCallback. Current Tab: " + viewPager.getCurrentItem() +
                        ", Container on Step 2 (global flag): " + isEmpRegisterFlowOnStep2ByContainer);

                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragmentInViewPager = fm.findFragmentByTag("f" + viewPager.getCurrentItem());

                if (viewPager.getCurrentItem() == 1 && currentFragmentInViewPager instanceof EmployerRegisterContainerFragment) {
                    EmployerRegisterContainerFragment container = (EmployerRegisterContainerFragment) currentFragmentInViewPager;
                    Log.d(TAG, "On Employer Register Tab. Container actual step 2 state: " + container.isCurrentlyOnStep2());

                    if (container.isCurrentlyOnStep2()) {
                        Log.d(TAG, "Employer container on Step 2. Its OnBackPressedCallback should handle pop to Step 1.");
                        // Rely on container's callback. If it handles it, this Activity's callback might not need to do more for this event.
                        return; // Let dispatcher route to container's enabled callback.
                    } else {
                        Log.d(TAG, "Employer container on Step 1. Proceeding to activity's default back press.");
                        fallbackToDefaultBackPress();
                    }
                } else {
                    Log.d(TAG, "Not on Employer Register Tab or not container. Proceeding to default.");
                    fallbackToDefaultBackPress();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, activityOnBackPressedCallback);
    }

    private void fallbackToDefaultBackPress() {
        Log.d(TAG, "Executing fallbackToDefaultBackPress for Employer.");
        if (activityOnBackPressedCallback != null) {
            activityOnBackPressedCallback.setEnabled(false);
        }
        super.onBackPressed();
        if (activityOnBackPressedCallback != null) {
            activityOnBackPressedCallback.setEnabled(true);
        }
    }

    public void onNavigateToEmployerRegisterStep2() {
        Log.d(TAG, "Activity notified: Employer Navigated to Step 2");
        isEmpRegisterFlowOnStep2ByContainer = true;
    }

    public void onNavigateBackToEmployerRegisterStep1() {
        Log.d(TAG, "Activity notified: Employer Navigated back to Step 1");
        isEmpRegisterFlowOnStep2ByContainer = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragmentInViewPager = getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 1 && currentFragmentInViewPager instanceof EmployerRegisterContainerFragment) {
            EmployerRegisterContainerFragment container = (EmployerRegisterContainerFragment) currentFragmentInViewPager;
            isEmpRegisterFlowOnStep2ByContainer = container.isCurrentlyOnStep2();
        } else {
            isEmpRegisterFlowOnStep2ByContainer = false;
        }
        Log.d(TAG, "onResume. Global Employer isStep2ByContainer flag: " + isEmpRegisterFlowOnStep2ByContainer);
    }

    // Add other lifecycle logs if needed (onStart, onPause, onStop, onDestroy)
}
