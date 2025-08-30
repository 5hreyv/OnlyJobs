package com.example.onlyjobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StudentAuthPagerAdapter extends FragmentStateAdapter {

    public StudentAuthPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StudentLoginFragment(); // Login fragment
            case 1:
                // The "Register" tab now shows the container fragment
                return new StudentRegisterContainerFragment(); // MODIFIED
            default:
                // This case should ideally not be reached if getItemCount() is correct
                return new StudentLoginFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Only two tabs: Login and Register
    }
}
