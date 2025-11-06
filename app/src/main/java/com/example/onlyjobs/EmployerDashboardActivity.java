package com.example.onlyjobs;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback; // Import the new required class
import androidx.appcompat.app.AppCompatActivity;

public class EmployerDashboardActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_dashboard);

        webView = findViewById(R.id.employer_webview);

        // IMPORTANT: Enable JavaScript for Firebase to work
        webView.getSettings().setJavaScriptEnabled(true);

        // This prevents links from opening in an external browser
        webView.setWebViewClient(new WebViewClient());

        // Load your local login page from the assets folder.
        webView.loadUrl("file:///android_asset/login.html");

        // --- START: Modern Back Press Handling ---
        // Get the OnBackPressedDispatcher and add a new callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Check if the WebView can go back in its history
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    // If the WebView cannot go back, we want the default behavior (finishing the activity).
                    // To do this, we first disable this callback.
                    setEnabled(false);
                    // Then, we call onBackPressed() again, which will now trigger the default action.
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

}

