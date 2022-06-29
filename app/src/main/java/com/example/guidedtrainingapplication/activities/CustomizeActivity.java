package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.firebase.DatabaseHelper;
import com.example.guidedtrainingapplication.objects.Training;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomizeActivity extends AppCompatActivity {
    // Customize Views
    private BottomNavigationView customize_NAV_bottom;
    private MaterialButton customize_BTN_setsminus,customize_BTN_lengthminus,customize_BTN_setsplus,customize_BTN_lengthplus,customize_BTN_edit;
    private MaterialTextView customize_LBL_setsamount, customize_LBL_lengthamount;
    private Spinner customize_SPN_mode;
    private CheckBox customize_BOX_chest, customize_BOX_legs, customize_BOX_back, customize_BOX_shoulders, customize_BOX_biceps, customize_BOX_triceps, customize_BOX_abs;

    // Customize Flow
    private String[] modes = {"Customized 1","Customized 2"};
    private List<String> muscles = new ArrayList<>();

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database helper
        databaseHelper = new DatabaseHelper(mAuth.getUid());

        findViews();

        initViews();
    }

    /**
     * This function find all views.
     */
    private void findViews() {
        customize_NAV_bottom = findViewById(R.id.customize_NAV_bottom);
        customize_SPN_mode = findViewById(R.id.customize_SPN_mode);
        customize_BTN_edit = findViewById(R.id.customize_BTN_edit);
        customize_BTN_setsminus = findViewById(R.id.customize_BTN_setsminus);
        customize_BTN_lengthminus = findViewById(R.id.customize_BTN_lengthminus);
        customize_BTN_setsplus = findViewById(R.id.customize_BTN_setsplus);
        customize_BTN_lengthplus = findViewById(R.id.customize_BTN_lengthplus);
        customize_LBL_setsamount = findViewById(R.id.customize_LBL_setsamount);
        customize_LBL_lengthamount = findViewById(R.id.customize_LBL_lengthamount);
        customize_BOX_chest = findViewById(R.id.customize_BOX_chest);
        customize_BOX_legs = findViewById(R.id.customize_BOX_legs);
        customize_BOX_back = findViewById(R.id.customize_BOX_back);
        customize_BOX_shoulders = findViewById(R.id.customize_BOX_shoulders);
        customize_BOX_biceps = findViewById(R.id.customize_BOX_biceps);
        customize_BOX_triceps = findViewById(R.id.customize_BOX_triceps);
        customize_BOX_abs = findViewById(R.id.customize_BOX_abs);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        initNavigation();
        setSpinner(customize_SPN_mode,modes);
        customize_BTN_setsminus.setOnClickListener(e -> {
            String setsAmount = customize_LBL_setsamount.getText().toString();
            if(!setsAmount.equalsIgnoreCase("0"))
                customize_LBL_setsamount.setText((Integer.parseInt(setsAmount) - 1) + "");
        });
        customize_BTN_lengthminus.setOnClickListener(e -> {
            String lengthAmount = customize_LBL_lengthamount.getText().toString();
            if(!lengthAmount.equalsIgnoreCase("0"))
                customize_LBL_lengthamount.setText((Integer.parseInt(lengthAmount) - 15) + "");
        });
        customize_BTN_setsplus.setOnClickListener(e -> {
            String setsAmount = customize_LBL_setsamount.getText().toString();
            if(!setsAmount.equalsIgnoreCase("5"))
                customize_LBL_setsamount.setText((Integer.parseInt(setsAmount) + 1) + "");
        });
        customize_BTN_lengthplus.setOnClickListener(e -> {
            String lengthAmount = customize_LBL_lengthamount.getText().toString();
            if(!lengthAmount.equalsIgnoreCase("60"))
                customize_LBL_lengthamount.setText((Integer.parseInt(lengthAmount) + 15) + "");
        });
        customize_BTN_edit.setOnClickListener(e -> {
            String mode = customize_SPN_mode.getSelectedItem().toString();
            String setsAmount = customize_LBL_setsamount.getText().toString();
            String lengthAmount = customize_LBL_lengthamount.getText().toString();

            if(muscles.size() > 0 && !setsAmount.equalsIgnoreCase("0") && !lengthAmount.equalsIgnoreCase("0")) {
                databaseHelper.writeTrainingToDB(new Training()
                        .setMode(mode)
                        .setExercises(muscles.size())
                        .setMusclesMap(muscles)
                        .setSets(Integer.parseInt(setsAmount))
                        .setSetLength(Integer.parseInt(lengthAmount)));
                Toast.makeText(this, "Successfully Edited. Your training is now available on 'My Trainings'.", Toast.LENGTH_SHORT).show();

                clearSelections();
            } else {
                Toast.makeText(this, "Action not completed, make sure to fill all fields properly... ", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * This function init the bottom navigation bar buttons.
     */
    private void initNavigation() {
        customize_NAV_bottom.setSelectedItemId(R.id.customize);
        // Perform item selected listener
        customize_NAV_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.quickstart:
                        //move to quickstart activity
                        finish();
                        return true;

                    case R.id.trainings:
                        //move to trainings activity
                        Intent i2 = new Intent(getApplicationContext(), TrainingsActivity.class);
                        i2.putExtra("latitude",getIntent().getDoubleExtra("latitude",0));
                        i2.putExtra("longitude",getIntent().getDoubleExtra("longitude",0));
                        startActivity(i2);
                        finish();
                        return true;

                    case R.id.history:
                        //move to history activity
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        finish();
                        return true;

                    case R.id.customize:
                        //stay in this activity
                        return true;

                    case R.id.settings:
                        //move to settings activity
                        return true;
                }
                return false;
            }
        });

    }


    /**
     * This function set the spinner view according to given values.
     * @param spn - The spinner that need to be set.
     * @param arr - The values that need to be inserted in the spinner.
     */
    private void setSpinner(Spinner spn, String[] arr) {
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
    }

    /**
     * This function check which view out of the check boxes has clicked and change whats needed according to it.
     * @param view - The view that has been clicked.
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.customize_BOX_chest:
                if (checked) {
                    muscles.add("Chest");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Chest");
                }
                break;
            case R.id.customize_BOX_legs:
                if (checked) {
                    muscles.add("Legs");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Legs");
                }
                break;
            case R.id.customize_BOX_back:
                if (checked) {
                    muscles.add("Back");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Back");
                }
                break;
            case R.id.customize_BOX_shoulders:
                if (checked) {
                    muscles.add("Shoulders");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Shoulders");
                }
                break;
            case R.id.customize_BOX_biceps:
                if (checked) {
                    muscles.add("Biceps");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Biceps");
                }
                break;
            case R.id.customize_BOX_triceps:
                if (checked) {
                    muscles.add("Triceps");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Triceps");
                }
                break;
            case R.id.customize_BOX_abs:
                if (checked) {
                    muscles.add("Abs");
                    view.setBackgroundColor(Color.parseColor("#8FD9FFFB"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                    muscles.remove("Abs");
                }
                break;
            default: break;
        }
    }

    /**
     * This function clear all the selection of the user on the customize activity.
     */
    private void clearSelections() {
        muscles.clear();
        customize_SPN_mode.setSelection(0,true);
        customize_LBL_setsamount.setText("0");
        customize_LBL_lengthamount.setText("0");
        customize_BOX_chest.setChecked(false);
        customize_BOX_legs.setChecked(false);
        customize_BOX_back.setChecked(false);
        customize_BOX_shoulders.setChecked(false);
        customize_BOX_biceps.setChecked(false);
        customize_BOX_triceps.setChecked(false);
        customize_BOX_abs.setChecked(false);
        customize_BOX_chest.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_legs.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_back.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_shoulders.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_biceps.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_triceps.setBackgroundColor(Color.parseColor("#00000000"));
        customize_BOX_abs.setBackgroundColor(Color.parseColor("#00000000"));
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
            Intent intent = new Intent(CustomizeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}