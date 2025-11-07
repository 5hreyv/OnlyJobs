package com.example.onlyjobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class StudentLoginFragment extends Fragment {

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private EditText emailInput;
    private Button sendLinkButton, googleSignInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_login, container, false);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailInput = view.findViewById(R.id.emailInput);
        sendLinkButton = view.findViewById(R.id.sendLinkBtn);
        googleSignInButton = view.findViewById(R.id.btnGoogleSignIn);

        // Button listeners
        sendLinkButton.setOnClickListener(v -> sendSignInLink());
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        // Setup Google Sign-In
        setupGoogleSignIn();

        return view;
    }

    // ---------------------- GOOGLE SIGN-IN SECTION ----------------------

    private void setupGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // defined in strings.xml
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            firebaseAuthWithGoogle(account.getIdToken());
                        }
                    } catch (ApiException e) {
                        Log.e("GoogleSignIn", "Sign-in failed", e);
                        Toast.makeText(getContext(), "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Signed in with Google!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), StudentDashboardActivity.class));
                            requireActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ---------------------- EMAIL LINK SECTION ----------------------

    private void sendSignInLink() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Please enter an email.", Toast.LENGTH_SHORT).show();
            return;
        }

        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://onlyjobs.firebaseapp.com/__/auth/links")
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.example.onlyjobs", true, "12")
                .build();

        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Sign-in link sent! Check your email.", Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("emailForSignIn", email).apply();
                    } else {
                        Toast.makeText(getContext(), "Failed to send email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
