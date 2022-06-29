package com.example.guidedtrainingapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.guidedtrainingapplication.R;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class Fragment_Details extends Fragment {
    // Fragment Details Views
    private MaterialTextView active_LBL_details, active_LBL_muscle;

    // Fragment Details Flow
    private ArrayList<String> muscles;
    private int exercises, sets, setLength;

    public Fragment_Details(ArrayList<String> muscles, int exercises, int sets, int setLength) {
        this.muscles = muscles;
        this.exercises = exercises;
        this.sets = sets;
        this.setLength = setLength;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        findViews(view);
        initViews();
        return view;
    }

    /**
     * This function init all the views according to a given view.
     * @param view - The view needed to find all the views.
     */
    private void findViews(View view) {
        active_LBL_details = view.findViewById(R.id.active_LBL_details);
        active_LBL_muscle = view.findViewById(R.id.active_LBL_muscle);
    }

    /**
     * This function init all the views.
     */
    private void initViews() { active_LBL_details.setText("Exercises: " + exercises + "\nSets: " + sets + "\nSet Length: " + setLength + "sec\n"); }

    /**
     * This function change training details according to the necessary muscle num.
     * @param exerciseNum - The muscle number that need to be shown in the details fragment.
     */
    public void changeTrainingDetails(int exerciseNum) {
        if(exerciseNum == -1)
            active_LBL_muscle.setText("Rest Time...");
        else
            active_LBL_muscle.setText("Current Muscle - " + muscles.get(exerciseNum));
    }

    /**
     * This function changes the set details on details fragment.
     * @param setNum - The set number that need to be shown.
     */
    public void changeSetDetails(int setNum) { active_LBL_details.setText("Set " + (setNum+1) + " Out of " + sets); }

    /**
     * This function show and hide the details on the details fragment.
     */
    public void showAndHideElements() { active_LBL_details.setVisibility(active_LBL_details.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE); }
}
