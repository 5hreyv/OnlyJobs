package com.example.onlyjobs;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        // The web app's own script will handle navigating to index.html after login.
        webView.loadUrl("file:///android_asset/login.html");
    }

    // Handle the back button to go back in web history
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
