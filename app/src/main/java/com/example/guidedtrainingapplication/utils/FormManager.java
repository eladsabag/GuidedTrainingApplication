package com.example.guidedtrainingapplication.utils;

import java.util.ArrayList;

public class FormManager {
    private ArrayList<Validator> validators = new ArrayList<>();

    public void addValidators(Validator validator) {
        validators.add(validator);
    }

    public void validateAll() {
        for (Validator validator : validators) {
            validator.validate();
        }
    }

    public boolean allGood() {
        for (Validator validator : validators) {
            if(!validator.isValid())
                return false;
        }
        return true;
    }

    public String getError() {
        // first error if exist
        for (Validator validator : validators) {
            if(!validator.isValid())
                return validator.getHint() + " - " + validator.check();
        }
        return "";
    }
}
