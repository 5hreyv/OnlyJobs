package com.example.onlyjobs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGetStarted;
    private TextView txtLearnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // link with your XML

        // Initialize UI components
        btnGetStarted = findViewById(R.id.btnGetStarted);
        txtLearnMore = findViewById(R.id.txtLearnMore);

        // Handle "Get Started" button click
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example: navigate to another activity
                Intent intent = new Intent(MainActivity.this, RolesActivity.class);
                startActivity(intent);
            }
        });

        // Handle "Learn More" text click
        txtLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLearnMorePage(v);
            }
        });
    }

    // This matches your XML attribute: android:onClick="openLearnMorePage"
    public void openLearnMorePage(View view) {
        // Example: open a webpage
        String url = "https://yourwebsite.com/learnmore"; // replace with your link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
