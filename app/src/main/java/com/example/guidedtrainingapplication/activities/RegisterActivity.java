package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.utils.FormManager;
import com.example.guidedtrainingapplication.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    // Register Views
    private TextInputLayout register_LAY_email, register_LAY_password;
    private MaterialButton register_BTN_signup;
    private MaterialTextView register_LBL_error;

    // Register Flow
    private FormManager fm;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fm = new FormManager();

        findViews();

        initViews();
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        register_LAY_email = findViewById(R.id.register_LAY_email);
        register_LAY_password = findViewById(R.id.register_LAY_password);
        register_BTN_signup = findViewById(R.id.register_BTN_signup);
        register_LBL_error = findViewById(R.id.register_LBL_error);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        Validator.Builder.make(register_LAY_email)
                .attachToFormManager(fm)
                .addRole(new Validator.Rule_Email("Please enter valid email input."))
                .build();
        Validator.Builder.make(register_LAY_password)
                .attachToFormManager(fm)
                .addRole(new Validator.Rule_Password("Please enter at least 8 number and/or letters."))
                .build();

        register_BTN_signup.setOnClickListener(e -> {
            if(fm.allGood())
                signUp();
            else
                fm.validateAll();
        });
    }

    /**
     * This function validate the email and password at firebase and sign up the user if all the details are ok.
     */
    private void signUp() {
        String email = register_LAY_email.getEditText().getText().toString();
        String password = register_LAY_password.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("pttt", "createUserWithEmail:success");
                            Intent intent = new Intent(RegisterActivity.this,QuickStartActivity.class);
                            intent.putExtra("firstLogin",true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, ""+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            register_LBL_error.setText(task.getException().getMessage());
                            register_LBL_error.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}