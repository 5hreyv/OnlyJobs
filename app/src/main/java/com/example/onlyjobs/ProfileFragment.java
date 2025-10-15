package com.example.onlyjobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup c, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, c, false);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.profile_view_pager);

        // Set up adapter for nested fragments
        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link tabs with viewpager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Personal Info" : "Jobs Applied");
        }).attach();

        // Load user profile data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvStudentName.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null && getContext() != null) {
                Glide.with(getContext()).load(user.getPhotoUrl()).circleCrop().into(imgProfile);
            }
        }
        return view;
    }
}