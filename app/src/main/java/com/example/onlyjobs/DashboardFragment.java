package com.example.onlyjobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView jobsRecyclerView;
    private JobsAdapter adapter;
    private ArrayList<Job> jobList;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        jobsRecyclerView = view.findViewById(R.id.jobsRecyclerView);
        jobsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        jobList = new ArrayList<>();
        adapter = new JobsAdapter(jobList);
        jobsRecyclerView.setAdapter(adapter);

        // Get a reference to the "jobs" node in your Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("jobs");

        // Add a listener to fetch the data in real-time
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Job job = dataSnapshot.getValue(Job.class);
                    jobList.add(job);
                }
                adapter.notifyDataSetChanged(); // Refresh the list in the UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // You can add a Toast here to show if there's an error
            }
        });

        return view;
    }
}