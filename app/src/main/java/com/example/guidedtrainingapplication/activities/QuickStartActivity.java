package com.example.guidedtrainingapplication.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.guidedtrainingapplication.firebase.DatabaseHelper;
import com.example.guidedtrainingapplication.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class QuickStartActivity extends AppCompatActivity {
    // Quick Start Views
    private MaterialButton quick_BTN_start;
    private MaterialTextView quick_LBL_customize;
    private BottomNavigationView quick_NAV_bottom;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    // Location
    private double latitude,longitude;
    private LocationManager mLocationManager;
    private int LOCATION_REFRESH_TIME = 1000; // 1 seconds to update
    private int LOCATION_REFRESH_DISTANCE = 100; // 100 meters to update
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);

        // Location Settings
        setLocationSettings();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database helper
        databaseHelper = new DatabaseHelper(mAuth.getUid());

        // If arrived from register page
        if(getIntent().hasExtra("firstLogin") && getIntent().getBooleanExtra("firstLogin",false))
            databaseHelper.initializeUserTrainings();

        findViews();

        initViews();
    }

    /**
     * This function sets the location settings of the user at the current game.
     */
    private void setLocationSettings() {
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        @SuppressLint("MissingPermission") ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                // Then update all the time and at every meters change.
                                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                        0, mLocationListener);
                                String providerName = mLocationManager.getBestProvider(locationCritera, true);
                                Location location  = mLocationManager.getLastKnownLocation(providerName);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                // Then update every limited time and at every limited meters change.
                                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                                        LOCATION_REFRESH_DISTANCE, mLocationListener);
                                String providerName = mLocationManager.getBestProvider(locationCritera, true);
                                Location location  = mLocationManager.getLastKnownLocation(providerName);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else {
                                // No location access granted.
                                // Then we can't use the location track and the 1st score location functionality is disabled.
                                // That's why we have set the default location to Ness Ziona(could be anywhere else just need default).
                            }
                        }
                );
        // check whether the app already has the permissions,
        // and whether the app needs to show a permission rationale dialog.
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void initViews() {
        SpannableString ss = new SpannableString(quick_LBL_customize.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(QuickStartActivity.this, CustomizeActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        quick_LBL_customize.setText(ss);
        quick_LBL_customize.setMovementMethod(LinkMovementMethod.getInstance());
        quick_LBL_customize.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss1=new SpannableString(quick_LBL_customize.getText());
        ForegroundColorSpan fgcs=new ForegroundColorSpan(Color.RED);
        ss1.setSpan(fgcs,0,9, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        quick_LBL_customize.setText(ss1);

        initNavigation();

        quick_BTN_start.setOnClickListener(e -> databaseHelper.readRandomTrainingAndStartIntent(this,latitude,longitude));
    }

    private void findViews() {
        quick_BTN_start = findViewById(R.id.quick_BTN_start);
        quick_LBL_customize = findViewById(R.id.quick_LBL_customize);
        quick_NAV_bottom = findViewById(R.id.quick_NAV_bottom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            // Sign out from the firebase account
            AuthUI.getInstance().signOut(this);
            mAuth.signOut();
            Intent intent = new Intent(QuickStartActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigation() {
        quick_NAV_bottom.setSelectedItemId(R.id.quickstart);
        // Perform item selected listener
        quick_NAV_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.quickstart:
                        //stay in this activity
                        return true;

                    case R.id.trainings:
                        //move to trainings activity
                        Intent i2 = new Intent(getApplicationContext(), TrainingsActivity.class);
                        i2.putExtra("latitude",latitude);
                        i2.putExtra("longitude",longitude);
                        startActivity(i2);
                        return true;

                    case R.id.history:
                        //move to history activity
                        Intent i3 = new Intent(getApplicationContext(), HistoryActivity.class);
                        i3.putExtra("latitude",latitude);
                        i3.putExtra("longitude",longitude);
                        startActivity(i3);
                        return true;

                    case R.id.customize:
                        //move to customize activity
                        Intent i4 = new Intent(getApplicationContext(), CustomizeActivity.class);
                        i4.putExtra("latitude",latitude);
                        i4.putExtra("longitude",longitude);
                        startActivity(i4);
                        return true;

                    case R.id.settings:
                        //move to settings activity
                        Intent i5 = new Intent(getApplicationContext(), SettingsActivity.class);
                        i5.putExtra("latitude",latitude);
                        i5.putExtra("longitude",longitude);
                        startActivity(i5);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        quick_NAV_bottom.setSelectedItemId(R.id.quickstart);
    }
}