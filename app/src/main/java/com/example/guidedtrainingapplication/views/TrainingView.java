package com.example.guidedtrainingapplication.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.firebase.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class TrainingView extends FrameLayout {
    public interface OnTrainingClickListener {
        void onClick(String mode, String muscles, String exercisesAndSets);
    }

    private OnTrainingClickListener onTrainingClickListener;

    public void setOnTrainingClickListener(OnTrainingClickListener onTrainingClickListener) {
        this.onTrainingClickListener = onTrainingClickListener;
    }

    public TrainingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TrainingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // necessary in order to read trainings
        DatabaseHelper databaseHelper = new DatabaseHelper(FirebaseAuth.getInstance().getUid());

        LinearLayout mainLay = new LinearLayout(context);
        mainLay.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mainLay.setLayoutParams(params);

        ScrollView scrollView = new ScrollView(context);
        params.setMargins(0,50,0,200);
        scrollView.setLayoutParams(params);

        // maximum 5 trainings - beginner, medium, advanced, customized 1, customized 2
        for (int i = 0; i < 5; i++) {
            LinearLayout lay = new LinearLayout(context);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setBackgroundResource(R.drawable.shape);

            params = new LinearLayout.LayoutParams(550,LayoutParams.WRAP_CONTENT);

            if(i == 0)
                params.setMargins(0,75,0,45);
            else
                params.setMargins(0,45,0,75);

            params.gravity = Gravity.CENTER;
            lay.setLayoutParams(params);
            lay.setPadding(45,45,45,45);

            // elements creation
            MaterialTextView mode = new MaterialTextView(context);
            MaterialTextView muscles = new MaterialTextView(context);
            MaterialTextView exercisesAndSets = new MaterialTextView(context);
            MaterialButton start = new MaterialButton(context);

            // read training and set the elements
            databaseHelper.readTrainingFromDB("T" + i, mode, muscles, exercisesAndSets, start);

            // set variables text
            mode.setText("Loading...");
            start.setText("Start");

            // set variables size
            mode.setTextSize(25);
            muscles.setTextSize(15);
            exercisesAndSets.setTextSize(15);
            start.setTextSize(16);

            // set variables color
            mode.setTextColor(Color.BLACK);
            muscles.setTextColor(Color.BLACK);
            exercisesAndSets.setTextColor(Color.BLACK);

            // set variables gravity
            mode.setGravity(Gravity.CENTER);

            // set variables params
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,0);
            params.gravity = Gravity.CENTER;
            start.setLayoutParams(params);
            start.setGravity(Gravity.CENTER);
            start.setIconSize(40);
            start.setIconTint(null);

            // set variables visibility
            muscles.setVisibility(View.GONE);
            exercisesAndSets.setVisibility(View.GONE);
            start.setVisibility(View.GONE);

            start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onTrainingClickListener != null) {
                        onTrainingClickListener.onClick(mode.getText().toString(),muscles.getText().toString(),exercisesAndSets.getText().toString());
                    }
                }
            });

            lay.addView(mode);
            lay.addView(muscles);
            lay.addView(exercisesAndSets);
            lay.addView(start);

            mainLay.addView(lay);
        }
        scrollView.addView(mainLay);
        addView(scrollView);

    }



}
