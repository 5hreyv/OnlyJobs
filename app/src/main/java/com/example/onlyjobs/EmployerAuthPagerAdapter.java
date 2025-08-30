package com.example.onlyjobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EmployerAuthPagerAdapter extends FragmentStateAdapter {

    public EmployerAuthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new EmployerLoginFragment();
        } else {
            // Return the container for multi-step registration
            return new EmployerRegisterContainerFragment(); // MODIFIED
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Login and Register tabs
    }
}
