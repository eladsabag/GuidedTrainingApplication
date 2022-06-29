package com.example.guidedtrainingapplication.objects;

import java.util.HashMap;
import java.util.List;

public class Training {
    private String mode; // beginner, medium, advanced, customized
    private HashMap<String,String> musclesMap = new HashMap<>(); // chest, back, legs, shoulders, triceps, biceps, abs
    private int exercises; // 4-7
    private int sets; // 3-5
    private int setLength; // 30 45 seconds

    public Training() {}

    public int getSetLength() {
        return setLength;
    }

    public Training setSetLength(int setLength) {
        this.setLength = setLength;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public Training setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public HashMap<String, String> getMusclesMap() {
        return musclesMap;
    }

    public Training setMusclesMap(List<String> muscles) {
        for (int i = 0; i < muscles.size(); i++) {
            this.musclesMap.put("A"+i,muscles.get(i));
        }
        return this;
    }

    public int getExercises() {
        return exercises;
    }

    public Training setExercises(int exercises) {
        this.exercises = exercises;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public Training setSets(int sets) {
        this.sets = sets;
        return this;
    }
}
