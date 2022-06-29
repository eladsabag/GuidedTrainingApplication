package com.example.guidedtrainingapplication.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.callbacks.CallBack_RecordClicked;
import com.example.guidedtrainingapplication.firebase.DatabaseHelper;
import com.example.guidedtrainingapplication.utils.RecordAdapter;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class Fragment_Records extends Fragment {
    // Fragment Records Views
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter = new RecordAdapter();
    private ImageView records_IMG_empty;
    private MaterialTextView records_LBL_nodata;
    private LottieAnimationView animationView;

    // Fragment Records Flow
    private Activity activity;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;

    // Callbacks
    private CallBack_RecordClicked callBack_RecordClicked;

    public Fragment_Records(Activity activity) {
        this.activity = activity;
    }

    public void setCallBack_RecordClicked(CallBack_RecordClicked callBack_RecordClicked) {
        this.callBack_RecordClicked = callBack_RecordClicked;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database helper
        databaseHelper = new DatabaseHelper(mAuth.getUid());

        findViews(view);
        initViews();
        return view;
    }

    /**
     * This function init all the views according to a given view.
     * @param view - The view needed to find all the views.
     */
    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        records_IMG_empty = view.findViewById(R.id.records_IMG_empty);
        records_LBL_nodata = view.findViewById(R.id.records_LBL_nodata);
        animationView = view.findViewById(R.id.animationView);
    }

    /**
     * This function init all the views.
     */
    private void initViews() { databaseHelper.readAllTrainingRecordsFromDB(recyclerView,recordAdapter,records_IMG_empty,records_LBL_nodata,animationView,activity,callBack_RecordClicked); }

    public int getRecordsSize() { return recordAdapter.getItemCount(); }
}
