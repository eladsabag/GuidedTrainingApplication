package com.example.guidedtrainingapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.callbacks.CallBack_PauseClicked;
import com.example.guidedtrainingapplication.callbacks.CallBack_SkipClicked;
import com.google.android.material.button.MaterialButton;

public class Fragment_Manage extends Fragment {
    // Fragment Manage Views
    private TextView active_LBL_time,active_LBL_exercises;
    private ImageView active_IMG_pause;
    private MaterialButton active_BTN_skip;

    // Fragment Manage Flow
    private int exercises, sets,  setLength, muscleNum = 0;
    private boolean isPause = true;

    // Callbacks
    private CallBack_SkipClicked callBack_SkipClicked;
    private CallBack_PauseClicked callBack_PauseClicked;

    public Fragment_Manage(int exercises, int sets, int setLength) {
        this.exercises = exercises;
        this.sets = sets;
        this.setLength = setLength;
    }

    public void setCallBack_SkipClicked(CallBack_SkipClicked callBack_SkipClicked) {
        this.callBack_SkipClicked = callBack_SkipClicked;
    }

    public void setCallBack_PauseClicked(CallBack_PauseClicked callBack_PauseClicked) {
        this.callBack_PauseClicked = callBack_PauseClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        findViews(view);
        initViews();
        return view;
    }

    /**
     * This function init all the views according to a given view.
     * @param view - The view needed to find all the views.
     */
    private void findViews(View view) {
        active_LBL_time = view.findViewById(R.id.active_LBL_time);
        active_LBL_exercises = view.findViewById(R.id.active_LBL_exercises);
        active_IMG_pause = view.findViewById(R.id.active_IMG_pause);
        active_BTN_skip = view.findViewById(R.id.active_BTN_skip);
    }

    /**
     * This function init all the views.
     */
    private void initViews() {
        active_LBL_exercises.setText((muscleNum+1) + "/" + exercises);
        active_BTN_skip.setOnClickListener(e -> {
            muscleNum++;
            if(callBack_SkipClicked != null)
                callBack_SkipClicked.skipClicked(muscleNum);
        });
        active_IMG_pause.setOnClickListener(e -> { pauseClicked(); });
    }

    /**
     * This function handle all the changes needed when pause is clicked.
     */
    public void pauseClicked() {
        if(isPause)
            active_IMG_pause.setImageResource(R.drawable.ic_playbutton);
        else
            active_IMG_pause.setImageResource(R.drawable.ic_pause);
        if(callBack_PauseClicked != null)
            callBack_PauseClicked.pauseClicked(isPause);
        isPause = !isPause;
    }

    /**
     * This function changes the time label according to a given counter and sets counter.
     * @param counter
     * @param setsCounter
     */
    public void setTimeLabel(int counter, int setsCounter) {
        if(setLength - counter == 60)
            active_LBL_time.setText("01:00");
        else if(setLength - counter < 10)
            active_LBL_time.setText("00:0" + (setLength - counter));
        else
            active_LBL_time.setText("00:" + (setLength - counter));

        if(setLength - counter == 0 && setsCounter == sets - 1)
            muscleNum++;
    }

    public int getMuscleNum() { return muscleNum; }

    /**
     * This function show and hide the details on the manage fragment.
     */
    public void showAndHideElements() {
        active_LBL_exercises.setVisibility(active_LBL_exercises.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
        active_IMG_pause.setVisibility(active_IMG_pause.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
        active_BTN_skip.setVisibility(active_BTN_skip.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * This function changes the label of the completed exercises.
     */
    public void changeExercisesDetails() { active_LBL_exercises.setText((muscleNum+1) + "/" + exercises); }
}
