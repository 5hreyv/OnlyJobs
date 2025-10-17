package com.example.onlyjobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class RolesActivity extends AppCompatActivity {

    MaterialCardView cardStudent, cardEmployer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles);

        // Link UI
        cardStudent = findViewById(R.id.cardStudent);
        cardEmployer = findViewById(R.id.cardEmployer);

        // Student Card Click → Go to Student Auth
        cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RolesActivity.this, StudentAuthActivity.class);
                startActivity(intent);
            }
        });

        // Employer Card Click → Go to Employer Auth
        cardEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RolesActivity.this, EmployerAuthActivity.class);
                startActivity(intent);
            }
        });
        
    }
}
