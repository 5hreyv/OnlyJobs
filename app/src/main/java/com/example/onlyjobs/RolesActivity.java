package com.example.onlyjobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class RolesActivity extends AppCompatActivity {

    private MaterialCardView cardStudent, cardEmployer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles); // 🔹 use your actual XML filename

        // Initialize cards
        cardStudent = findViewById(R.id.cardStudent);
        cardEmployer = findViewById(R.id.cardEmployer);

        // Handle Student card click
        cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If StudentActivity exists
                Intent intent = new Intent(RolesActivity.this, StudentRegisterActivity.class);
                startActivity(intent);

                // If not yet created, comment out above and use:
                // Toast.makeText(RolesActivity.this, "Student selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Employer card click
        cardEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If EmployerActivity exists
                Intent intent = new Intent(RolesActivity.this, EmployerRegisterActivity.class);
                startActivity(intent);

                // If not yet created, comment out above and use:
                // Toast.makeText(RolesActivity.this, "Employer selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
