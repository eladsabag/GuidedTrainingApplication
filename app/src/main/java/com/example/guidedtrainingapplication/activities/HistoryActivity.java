package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.callbacks.CallBack_RecordClicked;
import com.example.guidedtrainingapplication.firebase.DatabaseHelper;
import com.example.guidedtrainingapplication.fragments.Fragment_Map;
import com.example.guidedtrainingapplication.fragments.Fragment_Records;
import com.example.guidedtrainingapplication.objects.TrainingRecord;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    // History Views
    private BottomNavigationView history_NAV_bottom;

    // Fragments
    private Fragment_Records fragment_records;
    private Fragment_Map fragment_map;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    // Callbacks
    private CallBack_RecordClicked callBack_RecordClicked = new CallBack_RecordClicked() {
        @Override
        public void recordClicked(String title, double latitude, double longitude) {
            fragment_map.setRecordLocationOnMap(title,latitude,longitude);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database helper
        databaseHelper = new DatabaseHelper(mAuth.getUid());

        findViews();

        initViews();

        fragment_records = new Fragment_Records(HistoryActivity.this);
        fragment_records.setCallBack_RecordClicked(callBack_RecordClicked);
        getSupportFragmentManager().beginTransaction().add(R.id.history_LAY_records, fragment_records).commit();

        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.history_LAY_map, fragment_map).commit();
        
        if(getIntent().hasExtra("trainingEnd")) {
            saveTrainingDialog();
        }
    }

    /**
     * This function find all the views.
     */
    private void findViews() { history_NAV_bottom = findViewById(R.id.history_NAV_bottom); }

    /**
     * This function init all the views.
     */
    private void initViews() {
        initNavigation();
    }

    /**
     * This function init the bottom navigation bar buttons.
     */
    private void initNavigation() {
        history_NAV_bottom.setSelectedItemId(R.id.history);
        // Perform item selected listener
        history_NAV_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.quickstart:
                        //move to quickstart activity
                        finish();
                        return true;

                    case R.id.trainings:
                        //stay in this activity
                        Intent i2 = new Intent(getApplicationContext(), TrainingsActivity.class);
                        i2.putExtra("latitude",getIntent().getDoubleExtra("latitude",0));
                        i2.putExtra("longitude",getIntent().getDoubleExtra("longitude",0));
                        startActivity(i2);
                        finish();
                        return true;

                    case R.id.history:
                        //stay in this activity
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

    /**
     * This function display a confirm dialog about saving the last training record.
     */
    private void saveTrainingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Last Training ?");
        builder.setMessage("Would you like to save last training record ?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tid = "R" + fragment_records.getRecordsSize();
                String mode = getIntent().getStringExtra("mode");
                double latitude = getIntent().getDoubleExtra("latitude",0);
                double longitude = getIntent().getDoubleExtra("longitude",0);
                databaseHelper.writeTrainingRecordToDB(tid,new TrainingRecord(mode,latitude,longitude));

                // refresh data in recycler view
                getSupportFragmentManager().beginTransaction().remove(fragment_records).commit();
                fragment_records = new Fragment_Records(HistoryActivity.this);
                fragment_records.setCallBack_RecordClicked(callBack_RecordClicked);
                getSupportFragmentManager().beginTransaction().add(R.id.history_LAY_records, fragment_records).commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // nothing happens
            }
        });
        builder.create().show();
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
            Intent intent = new Intent(HistoryActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}