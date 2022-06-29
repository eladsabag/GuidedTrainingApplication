package com.example.guidedtrainingapplication.firebase;

import com.example.guidedtrainingapplication.objects.Training;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {

    /**
     * This function generated the built in trainings of new user,
     * @return - Trainings Array List with built in data.
     */
    public static ArrayList<Training> generateData() {
        ArrayList<Training> trainings = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            trainings.add(generateTraining(i));
        }

        return trainings;
    }

    /**
     * This function generate training according to its specification.
     * 0,1,2 means Beginner, Medium, Advanced modes.
     * 3,4 means Customized 1, Customized 2 modes.
     * @param i - The training specification.
     * @return - The training created.
     */
    public static Training generateTraining(int i) {
        if(i == 0) {
            return new Training()
                    .setMode("Beginner")
                    .setMusclesMap(Arrays.asList("Chest","Legs","Biceps","Abs"))
                    .setSets(3)
                    .setExercises(4)
                    .setSetLength(30);
        } else if(i == 1) {
            return new Training()
                    .setMode("Medium")
                    .setMusclesMap(Arrays.asList("Back", "Shoulders", "Triceps","Abs"))
                    .setSets(4)
                    .setExercises(4)
                    .setSetLength(45);
        } else if(i == 2) {
            return new Training()
                    .setMode("Advanced")
                    .setMusclesMap(Arrays.asList("Chest", "Legs", "Biceps", "Back", "Shoulders", "Triceps","Abs"))
                    .setSets(4)
                    .setExercises(7)
                    .setSetLength(30);
        }
        return new Training() // the user will be able to update this mode manually
                .setMode("Customized " + (i == 3 ? 1 : 2))
                .setSets(0)
                .setExercises(0)
                .setSetLength(0);
    }
}
