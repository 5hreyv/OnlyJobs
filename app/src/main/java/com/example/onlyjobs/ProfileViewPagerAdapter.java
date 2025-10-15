package com.example.onlyjobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileViewPagerAdapter extends FragmentStateAdapter {

    public ProfileViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the correct fragment based on the tab position
        if (position == 1) {
            return new JobsAppliedFragment();
        }
        // Default to PersonalInfoFragment for position 0
        return new PersonalInfoFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}