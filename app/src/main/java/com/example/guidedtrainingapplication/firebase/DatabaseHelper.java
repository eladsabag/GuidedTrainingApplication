package com.example.guidedtrainingapplication.firebase;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.guidedtrainingapplication.R;
import com.example.guidedtrainingapplication.activities.ActiveTrainingActivity;
import com.example.guidedtrainingapplication.callbacks.CallBack_RecordClicked;
import com.example.guidedtrainingapplication.objects.Training;
import com.example.guidedtrainingapplication.objects.TrainingRecord;
import com.example.guidedtrainingapplication.utils.RecordAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String userID;

    public DatabaseHelper(String userID) {
        this.userID = userID;
    }

    /**
     * This function initial trainings for the user.
     * Each user has 3 built in trainings of different modes - Beginner, Medium, Advanced.
     * The user also has 2 trainings to customize, which makes it a total of 5 trainings.
     */
    public void initializeUserTrainings() {
        myRef = database.getReference("Trainings").child(userID);
        ArrayList<Training> ts = DataManager.generateData();
        HashMap<String,Training> hm = new HashMap<>();
        for (int i = 0; i < ts.size(); i++) {
            hm.put("T" + i, ts.get(i));
        }
        myRef.setValue(hm);
    }

    /**
     * This function read specific training from database and set values according to the training that has been read.
     * @param tid - The id of the training that need to be read.
     * @param mode - The material text view that needs to be updated according to the training mode value.
     * @param muscles - The material text view that needs to be updated according to the training muscles value.
     * @param exercisesAndSets - The material text view that needs to be updated according to the training exercises and sets values.
     * @param start - The Material Button that need to be functioned and designed according to the training value.
     */
    public void readTrainingFromDB(String tid, MaterialTextView mode, MaterialTextView muscles,
                                   MaterialTextView exercisesAndSets, MaterialButton start) {
        myRef = database.getReference("Trainings")
                .child(userID)
                .child(tid);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Training training = dataSnapshot.getValue(Training.class);
                mode.setText(training.getMode());
                int i = 0;
                for(String m : training.getMusclesMap().values()) {
                    if(i++ > 0)
                        muscles.setText(muscles.getText()  + ", "+ m );
                    else
                        muscles.setText("Muscles: " + m);
                }
                muscles.setText(muscles.getText() + ".\n");
                exercisesAndSets.setText("Exercises: " + training.getExercises() + ", Sets: " + training.getSets() + ".\n\nSet Length: " + training.getSetLength() + "s.");
                if(training.getMode().equalsIgnoreCase("Beginner"))
                    start.setIconResource(R.drawable.ic_starone);
                else if(training.getMode().equalsIgnoreCase("Medium"))
                    start.setIconResource(R.drawable.ic_startwo);
                else if(training.getMode().equalsIgnoreCase("Advanced"))
                    start.setIconResource(R.drawable.ic_starthree);
                else
                    start.setIconResource(R.drawable.ic_star);

                exercisesAndSets.setVisibility(View.VISIBLE);
                if(training.getMusclesMap().size() == 0) {
                    exercisesAndSets.setText("Build your training under 'Customize'.");
                } else {
                    muscles.setVisibility(View.VISIBLE);
                    start.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * This function write a training to the database.
     * @param training - The training that need to be written. This training will always be Customized type.
     */
    public void writeTrainingToDB(Training training) {
        String tid = training.getMode().equalsIgnoreCase("Customized 1") ? "T3" : "T4";
        myRef = database.getReference("Trainings")
                .child(userID)
                .child(tid);

        myRef.child("exercises").setValue(training.getExercises());
        myRef.child("musclesMap").setValue(training.getMusclesMap());
        myRef.child("setLength").setValue(training.getSetLength());
        myRef.child("sets").setValue(training.getSets());
    }

    /**
     * This function reads all the training records from the database and sets variables according to this values.
     * @param recyclerView - The element that will hold all the values
     * @param recordAdapter - The element that will be inserted inside the recycler view.
     * @param records_IMG_empty - The Image View that will be changed according to the Training Records values.
     * @param records_LBL_nodata - The Material Text View that will be changed according to the Training Records values.
     * @param activity - The activity that the values that we read will be shown on.
     * @param callBack_RecordClicked - A callback that will be set according to the Training Records values.
     */
    public void readAllTrainingRecordsFromDB(RecyclerView recyclerView, RecordAdapter recordAdapter, ImageView records_IMG_empty,
                                             MaterialTextView records_LBL_nodata,
                                             LottieAnimationView animationView, Activity activity,
                                             CallBack_RecordClicked callBack_RecordClicked) {
        myRef = database.getReference("TrainingRecords").child(userID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<TrainingRecord> lst = new ArrayList<>();
                for(DataSnapshot sp : snapshot.getChildren()) {
                    TrainingRecord record = sp.getValue(TrainingRecord.class);
                    lst.add(record);
                }

                if(lst.size() == 0) {
                    records_IMG_empty.setVisibility(View.VISIBLE);
                    records_LBL_nodata.setText("No Records...");
                } else
                    records_LBL_nodata.setVisibility(View.GONE);

                recordAdapter.setActivity(activity)
                        .setContext(activity.getApplicationContext())
                        .setAllRecords(lst)
                        .setCallBack_RecordClicked(callBack_RecordClicked);
                recyclerView.setAdapter(recordAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));

                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * This function write Training Record to the database.
     * @param rid - The training Record id that will be written to the database.
     * @param trainingRecord - The training record value that will be written to the database.
     */
    public void writeTrainingRecordToDB(String rid, TrainingRecord trainingRecord) {
        myRef = database.getReference("TrainingRecords")
                .child(userID)
                .child(rid);
        myRef.setValue(trainingRecord);
    }

    /**
     * This function belongs to the quick start activity.
     * It reads a random training out of the 3 built in modes - Beginner, Medium, Advanced,
     * And starts the active training intent with all the values needed.
     * @param activity - The quick start activity that will start another activity.
     * @param latitude - This is the latitude value of the user location that need to be send to the active training activity.
     * @param longitude - This is the longitude value of the user location that need to be send to the active training activity.
     */
    public void readRandomTrainingAndStartIntent(Activity activity,double latitude, double longitude) {
        myRef = database.getReference("Trainings")
                .child(userID)
                .child("T" + (int)(Math.random() * 3)); // random number 0-2 for beginner/medium/advanced mode

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Training training = snapshot.getValue(Training.class);

                Intent intent = new Intent(activity, ActiveTrainingActivity.class);

                int i = 0;
                for (String muscle : training.getMusclesMap().values())
                    intent.putExtra("muscle" + i++, muscle);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("mode",training.getMode());
                intent.putExtra("exercises", training.getExercises());
                intent.putExtra("sets", training.getSets());
                intent.putExtra("setLength",training.getSetLength());

                activity.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
