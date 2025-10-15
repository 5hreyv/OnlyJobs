package com.example.onlyjobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup c, @Nullable Bundle s) {
        View view = inflater.inflate(R.layout.fragment_personal_info, c, false);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvUniversity = view.findViewById(R.id.tvUniversity);

        // Fetch and display user data
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        tvEmail.setText("Email: " + doc.getString("email"));
                        tvUniversity.setText("University: " + doc.getString("university"));
                    }
                });
        return view;
    }
}