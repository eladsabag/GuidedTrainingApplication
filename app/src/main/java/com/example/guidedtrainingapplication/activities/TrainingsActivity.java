package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.views.TrainingView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class TrainingsActivity extends AppCompatActivity {
    private TrainingView trainingView;

    private BottomNavigationView trainings_NAV_bottom;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViews();
        
        initViews();
    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        trainingView = findViewById(R.id.trainingView);
        trainings_NAV_bottom = findViewById(R.id.trainings_NAV_bottom);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        initNavigation();
        trainingView.setOnTrainingClickListener(new TrainingView.OnTrainingClickListener() {
            @Override
            public void onClick(String mode, String muscles, String exercisesAndSets) {
                Intent intent = new Intent(TrainingsActivity.this, ActiveTrainingActivity.class);


                // retrieve muscles, exercises, sets and set length from the given strings and send to the active training intent.
                // the structure of the string is permanent.
                int i = 0;
                for (String s : muscles.split(" ")) {
                    char c1 = s.charAt(s.length()-1), c2 = s.charAt(s.length()-2);
                    if(c1 == ',' || c2 == '.') {
                        if(c1 == ',')
                            intent.putExtra("muscle" + i++,s.substring(0,s.length()-1));
                        else
                            intent.putExtra("muscle" + i++,s.substring(0,s.length()-2));
                    }
                }
                intent.putExtra("latitude",getIntent().getDoubleExtra("latitude",0));
                intent.putExtra("longitude",getIntent().getDoubleExtra("longitude",0));
                intent.putExtra("mode",mode);
                intent.putExtra("exercises", Integer.parseInt(exercisesAndSets.charAt(11)+""));
                intent.putExtra("sets", Integer.parseInt(exercisesAndSets.charAt(20)+""));
                intent.putExtra("setLength", Integer.parseInt(exercisesAndSets.charAt(36) + "" + exercisesAndSets.charAt(37)));

                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * This function init the bottom navigation bar buttons.
     */
    private void initNavigation() {
        trainings_NAV_bottom.setSelectedItemId(R.id.trainings);
        // Perform item selected listener
        trainings_NAV_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quickstart:
                        //move to quickstart activity
                        finish();
                        return true;

                    case R.id.trainings:
                        //stay in this activity
                        return true;

                    case R.id.history:
                        //move to history activity
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        finish();
                        return true;

                    case R.id.customize:
                        //move to customize activity
                        startActivity(new Intent(getApplicationContext(), CustomizeActivity.class));
                        finish();
                        return true;

                    case R.id.settings:
                        //move to settings activity
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        } else if(item.getItemId() == R.id.logout) {
            // Sign out from the firebase account
            AuthUI.getInstance().signOut(this);
            mAuth.signOut();
            Intent intent = new Intent(TrainingsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}