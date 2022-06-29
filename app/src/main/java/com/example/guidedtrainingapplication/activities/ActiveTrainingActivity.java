package com.example.guidedtrainingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.callbacks.CallBack_PauseClicked;
import com.example.guidedtrainingapplication.callbacks.CallBack_SkipClicked;
import com.example.guidedtrainingapplication.fragments.Fragment_Details;
import com.example.guidedtrainingapplication.fragments.Fragment_Manage;
import com.example.guidedtrainingapplication.fragments.Fragment_Video;
import com.example.guidedtrainingapplication.utils.MSP;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ActiveTrainingActivity extends AppCompatActivity {
    // Active Training Flow
    private ArrayList<String> muscles = new ArrayList<>();
    private String mode;
    private int exercises = 0, sets = 0, setLength = 0 , setsCounter = 0, currentPosition;
    private boolean isPreview = true, isRest = false, isVibrate, isAudio;
    private double latitude,longitude;
    private MediaPlayer playtowin;

    // Fragments
    private Fragment_Details fragment_details;
    private Fragment_Video fragment_video;
    private Fragment_Manage fragment_manage;

    // Callbacks
    private CallBack_SkipClicked callBack_SkipClicked = new CallBack_SkipClicked() {
        @Override
        public void skipClicked(int exerciseNum) {
            if(exerciseNum < exercises) {
                fragment_manage.changeExercisesDetails();
                fragment_details.changeTrainingDetails(exerciseNum);
                fragment_details.changeSetDetails(0);
                fragment_video.initUri(muscles.get(fragment_manage.getMuscleNum()));
                fragment_video.startVideo();
                setsCounter = 0;
                counter = 0;
            } else {
                finishTraining();
            }
            if(!fragment_video.isPlaying())
                fragment_manage.pauseClicked();
            if(isVibrate)
                vibrateOnce();
            if(isAudio)
                playtowin.seekTo(0);
            if(isAudio && !playtowin.isPlaying()) {
                playtowin.start();
            }
        }
    };

    private CallBack_PauseClicked callBack_PauseClicked = new CallBack_PauseClicked() {
        @Override
        public void pauseClicked(boolean isPause) {
            fragment_video.pauseAndResumeVideo();

            if(isPause) {
                stopTimer();
                timerStatus = TIMER_STATUS.OFF;
            } else {
                startTimer();
                timerStatus = TIMER_STATUS.RUNNING;
            }

            if(isAudio && playtowin.isPlaying()) {
                currentPosition = playtowin.getCurrentPosition();
                playtowin.pause();
            } else {
                playtowin.seekTo(currentPosition);
                playtowin.start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_training);

        // showing the back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        readFromSharedPreferences();

        if(isAudio) {
            playtowin = MediaPlayer.create(ActiveTrainingActivity.this, R.raw.playtowin);
        }

        getIntentExtrasAndSetData();

        fragment_details = new Fragment_Details(muscles,exercises, sets, setLength);
        getSupportFragmentManager().beginTransaction().add(R.id.active_LAY_details, fragment_details).commit();

        fragment_video = new Fragment_Video(this,muscles.get(0));
        getSupportFragmentManager().beginTransaction().add(R.id.active_LAY_video, fragment_video).commit();

        fragment_manage = new Fragment_Manage(exercises,sets,setLength);
        fragment_manage.setCallBack_SkipClicked(callBack_SkipClicked);
        fragment_manage.setCallBack_PauseClicked(callBack_PauseClicked);
        getSupportFragmentManager().beginTransaction().add(R.id.active_LAY_manage, fragment_manage).commit();

        startSetTimer();
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

    /**
     * This function read the vibration and audio value from shared preferences.
     */
    private void readFromSharedPreferences() {
        isVibrate = MSP.getMe(this).getBooleanFromSP("isVibrate",true);
        isAudio = MSP.getMe(this).getBooleanFromSP("isAudio",true);
    }

    /**
     * This function get the intent extras and set variables according to the data given in extras.
     */
    private void getIntentExtrasAndSetData() {
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        mode = getIntent().getStringExtra("mode");
        exercises = getIntent().getIntExtra("exercises",0);
        sets = getIntent().getIntExtra("sets",0);
        setLength = getIntent().getIntExtra("setLength",0);
        for (int i = 0; i < exercises; i++)
            muscles.add(getIntent().getStringExtra("muscle" + i));
    }

    /**
     * This function show preview on the center of screen according to string value.
     * @param t - The string value that will be displayed in the center of the screen.
     */
    private void showPreview(String t) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.preview_layout, findViewById(R.id.previewLayout));
        MaterialTextView text = view.findViewById(R.id.previewText);
        ImageView img = view.findViewById(R.id.previewImage);
        text.setText("");
        img.setImageResource(0);
        text.setText(t);
        Toast toast = new Toast(getApplicationContext());
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * This function handles the necessary fragments changes during the preview time.
     */
    private void preview() {
        fragment_manage.showAndHideElements();
        fragment_details.changeTrainingDetails(0);
        fragment_details.changeSetDetails(0);
        fragment_video.startVideo();
        isPreview = false;
        counter = 0;
        if(isVibrate)
            vibrateOnce();
    }

    /**
     * This function handles the necessary fragment changes during the work time.
     */
    private void work() {
        fragment_manage.setTimeLabel(counter,setsCounter);
        fragment_video.restartVideoIfNeeded();
        if(counter == setLength) {
            if(setsCounter++ == sets - 1) {
                fragment_manage.showAndHideElements();
                fragment_details.showAndHideElements();
                fragment_details.changeTrainingDetails(-1);
                fragment_video.initUri("Rest");
                fragment_video.startVideo();
                setsCounter = 0;
                isRest = true;
            }
            if(isVibrate)
                vibrateOnce();

            if(sets-setsCounter != sets)
                Toast.makeText(this, "Good Job, " + (sets - setsCounter) + " to go!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "You are awesome!\nNow you can take a break.", Toast.LENGTH_SHORT).show();

            fragment_details.changeSetDetails(setsCounter);
            counter = 0;
        }
        if(isAudio && !playtowin.isPlaying() && fragment_video.isPlaying()) { // renew the music if needed
            playtowin.seekTo(0);
            playtowin.start();
        }
    }

    /**
     * This function handles the necessary fragment changes during the rest time.
     */
    private void rest() {
        fragment_manage.setTimeLabel(counter,setsCounter);
        fragment_video.restartVideoIfNeeded();
        if(counter == setLength) {
            fragment_manage.changeExercisesDetails();
            fragment_details.showAndHideElements();
            fragment_manage.showAndHideElements();
            fragment_details.changeTrainingDetails(fragment_manage.getMuscleNum());
            fragment_video.initUri(muscles.get(fragment_manage.getMuscleNum()));
            fragment_video.startVideo();
            isRest = false;
            counter = 0;
        }
        if(isAudio && playtowin.isPlaying())
            playtowin.pause();
    }

    /**
     * This function ends the training and everything related, such as audio played, and open the history activity.
     */
    private void finishTraining() {
        if(isVibrate)
            vibrateOnce();
        if(isAudio && playtowin.isPlaying())
            playtowin.stop();
        timer.cancel();
        Intent intent = new Intent(ActiveTrainingActivity.this, HistoryActivity.class);
        intent.putExtra("mode",mode);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("trainingEnd","trainingEnd");
        startActivity(intent);
        finish();
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(isAudio && playtowin.isPlaying())
                    playtowin.stop();
                isAudio = false; // disable audio at exit but don't change the actual settings(
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isAudio && playtowin.isPlaying())
            playtowin.stop();
        isAudio = false; // disable audio at exit but don't change the actual settings
        this.finish();
    }

    // ---------- ---------- TIMER ---------- ----------

    private final int DELAY = 1000;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    private Timer timer;
    private int counter = 0;

    private void startSetTimer() {
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.OFF;
        } else {
            startTimer();
        }
    }

    private void tick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(counter == 0 && isPreview) {
                    showPreview("Ready?");
                    showPreview("Set...");
                    showPreview("Go!");
                } else if(isPreview) {
                    if(counter == 7) {
                        preview();
                    }
                } else if(isRest) {
                    rest();
                } else {
                    work();
                }
                counter++;
                if(fragment_manage.getMuscleNum() == exercises)
                    finishTraining();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isAudio && fragment_video.isPlaying()) // if necessary pause the training and everything related to it.
            fragment_manage.pauseClicked();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerStatus == TIMER_STATUS.PAUSE)
            startTimer();
    }

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, DELAY);
    }

    private void stopTimer() { timer.cancel(); }
}