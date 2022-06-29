package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.retrofit.RetrofitClient;
import com.example.guidedtrainingapplication.utils.MSP;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    // Settings Views
    private BottomNavigationView settings_NAV_bottom;
    private CircleImageView settings_IMG_initials;
    private MaterialTextView settings_LBL_email;
    private MaterialButton settings_BTN_vibrate, settings_BTN_audio, settings_BTN_logout;

    // Settings Flow
    private boolean isVibrate, isAudio;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        readFromSharedPreferences();

        findViews();

        initViews();

    }

    /**
     * This function find all the views.
     */
    private void findViews() {
        settings_NAV_bottom = findViewById(R.id.settings_NAV_bottom);
        settings_IMG_initials = findViewById(R.id.settings_IMG_initials);
        settings_LBL_email = findViewById(R.id.settings_LBL_email);
        settings_BTN_vibrate = findViewById(R.id.settings_BTN_vibrate);
        settings_BTN_audio = findViewById(R.id.settings_BTN_audio);
        settings_BTN_logout = findViewById(R.id.settings_BTN_logout);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        generateInitialsImage();
        settings_LBL_email.setText(mAuth.getCurrentUser().getEmail());
        settings_BTN_vibrate.setIcon(isVibrate ? getDrawable(R.drawable.ic_vibrationon) : getDrawable(R.drawable.ic_vibrationoff));
        settings_BTN_vibrate.setText(isVibrate ? "VIBRATE ON" : "VIBRATE OFF");
        settings_BTN_audio.setIcon(isAudio ? getDrawable(R.drawable.ic_audioon) : getDrawable(R.drawable.ic_audiooff));
        settings_BTN_audio.setText(isAudio ? "AUDIO ON" : "AUDIO OFF");
        settings_BTN_vibrate.setOnClickListener(e -> {
            isVibrate = !isVibrate;
            settings_BTN_vibrate.setText(isVibrate ? "VIBRATE ON" : "VIBRATE OFF");
            settings_BTN_vibrate.setIcon(isVibrate ? getDrawable(R.drawable.ic_vibrationon) : getDrawable(R.drawable.ic_vibrationoff));
            MSP.getMe(this).putBooleanToSP(mAuth.getUid() + "_isVibrate", isVibrate); // save isVibrate value to shared preferences
            vibrateOnce();
        });
        settings_BTN_audio.setOnClickListener(e -> {
            isAudio = !isAudio;
            settings_BTN_audio.setText(isAudio ? "AUDIO ON" : "AUDIO OFF");
            settings_BTN_audio.setIcon(isAudio ? getDrawable(R.drawable.ic_audioon) : getDrawable(R.drawable.ic_audiooff));
            MSP.getMe(this).putBooleanToSP(mAuth.getUid() + "_isAudio", isAudio); // save isAudio value to shared preferences
        });
        settings_BTN_logout.setOnClickListener(e -> logoutDialog());
        initNavigation();
    }

    /**
     * This function init the bottom navigation buttons.
     */
    private void initNavigation() {
        settings_NAV_bottom.setSelectedItemId(R.id.settings);
        // Perform item selected listener
        settings_NAV_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                        finish();
                        return true;

                    case R.id.customize:
                        //move to customize activity
                        startActivity(new Intent(getApplicationContext(), CustomizeActivity.class));
                        finish();
                        return true;

                    case R.id.settings:
                        //stay in this activity
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * This function make a GET Request to the dicebear API and send the user email, in order to get from the API the string bitmap value of a the email initials PNG.
     */
    private void generateInitialsImage() {
        Call<ResponseBody> call = RetrofitClient.getInstance().getMyApi().getInitialsImage(mAuth.getCurrentUser().getEmail() + ".png");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG,100, baos);
                        byte [] b=baos.toByteArray();
                        setInitialsImage(Base64.encodeToString(b, Base64.DEFAULT));
                    } else {
                        settings_IMG_initials.setImageResource(R.drawable.img_placeholder);
                    }
                } else {
                    settings_IMG_initials.setImageResource(R.drawable.img_error);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });
    }

    /**
     * This function convert an encoded string to bitmap value and set the image according to the given bitmap value.
     * @param encodedString - The string to be converted.
     */
    private void setInitialsImage(String encodedString) {
        Bitmap bitmap = null;
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        settings_IMG_initials.setImageBitmap(bitmap);
    }

    /**
     * This function display a confirm dialog about logout from the user.
     */
    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout ?");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setIcon(R.drawable.ic_logout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

    /**
     * This function real the vibration and audio values from shared preferences.
     */
    private void readFromSharedPreferences() {
        isVibrate = MSP.getMe(this).getBooleanFromSP(mAuth.getUid() + "_isVibrate",true);
        isAudio = MSP.getMe(this).getBooleanFromSP(mAuth.getUid() + "_isAudio",true);
    }

    /**
     * This function make the phone vibrate once.
     */
    private void vibrateOnce() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
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
            Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}