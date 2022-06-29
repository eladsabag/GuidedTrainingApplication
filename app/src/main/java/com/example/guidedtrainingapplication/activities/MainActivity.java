package com.example.guidedtrainingapplication.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.guidedtrainingapplication.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // Main Views
    private MaterialButton main_BTN_join, main_BTN_login, main_BTN_google, main_BTN_phone, main_BTN_email;
    private RelativeLayout main_LAY_rel;
    private LinearLayout main_LAY_lin;
    private LottieAnimationView main_LOT_anim;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            Intent intent = new Intent(this,QuickStartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            findViews();

            initViews();
        }
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        main_LAY_rel = findViewById(R.id.main_LAY_rel);
        main_LAY_lin = findViewById(R.id.main_LAY_lin);
        main_LOT_anim = findViewById(R.id.main_LOT_anim);
        main_BTN_join = findViewById(R.id.main_BTN_join);
        main_BTN_login = findViewById(R.id.main_BTN_login);
        main_BTN_google = findViewById(R.id.main_BTN_google);
        main_BTN_phone = findViewById(R.id.main_BTN_phone);
        main_BTN_email = findViewById(R.id.main_BTN_email);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        main_LAY_rel.setBackground(getDrawable(R.drawable.ic_girl));
        main_LAY_lin.setVisibility(View.VISIBLE);
        main_LOT_anim.setVisibility(View.GONE);
        main_BTN_login.setOnClickListener(e -> {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        });
        main_BTN_join.setOnClickListener(e -> {
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        });
        main_BTN_google.setOnClickListener(e -> signIn());
        main_BTN_phone.setOnClickListener(e -> signIn());
        main_BTN_email.setOnClickListener(e -> signIn());
    }

    /**
     * This function pop up intent with the Firebase UI Authentication.
     */
    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_weightlifter)
                .setTheme(com.firebase.ui.auth.R.style.Theme_MaterialComponents_Light_DarkActionBar)
                .setTosAndPrivacyPolicyUrls("https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0", "https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0")
                .build();
        signInLauncher.launch(signInIntent);
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if(result.getResultCode() == RESULT_OK) {
            Intent intent = new Intent(this,QuickStartActivity.class);
            intent.putExtra("firstLogin",true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Something went wront, Please try again.",Toast.LENGTH_SHORT).show();
        }
    }
}