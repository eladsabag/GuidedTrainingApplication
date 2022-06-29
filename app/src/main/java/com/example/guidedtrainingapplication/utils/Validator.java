package com.example.guidedtrainingapplication.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class Validator {
    private TextInputLayout textInputLayout;
    private ArrayList<Rule> rules = new ArrayList<>();

    public Validator(TextInputLayout textInputLayout, ArrayList<Rule> rules) {
        this.textInputLayout = textInputLayout;
        this.rules = rules;

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                textInputLayout.setError(check(input));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void validate() {
        textInputLayout.setError(check());
    }

    public boolean isValid() {
        return check().equals("");
    }

    public String check() {
        String input = textInputLayout.getEditText().getText().toString();
        return check(input);
    }

    private String check(String input) {
        for(Rule rule : rules) {
            if(!rule.selfCheck(input)) {
                return rule.error;
            }
        }
        return "";
    }

    public String getHint() {
        return textInputLayout.getHint().toString();
    }

    public static class Builder {
        private TextInputLayout textInputLayout;
        private ArrayList<Rule> rules = new ArrayList<>();
        private FormManager formManager;

        public Builder(@NonNull TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        public static Builder make(@NonNull TextInputLayout textInputLayout) {
            return new Builder(textInputLayout);
        }

        public Builder attachToFormManager(FormManager formManager) {
            this.formManager = formManager;
            return this;
        }

        public Builder addRole(@NonNull Rule rule) {
            rules.add(rule);
            return this;
        }

        public Validator build() {
            Validator v = new Validator(textInputLayout, rules);
            formManager.addValidators(v);
            return v;
        }
    }

    public abstract static class Rule {
        private String error = "ERROR";

        public Rule(String error) {
            this.error = error;
        }

        public abstract boolean selfCheck(String input);
    }

    public static class Rule_Empty extends Rule {

        public Rule_Empty(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            if(input == null || input.equals("")) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_Integer extends Rule {

        public Rule_Integer(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            try {
                int num = Integer.valueOf(input);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_Positive extends Rule {

        public Rule_Positive(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            try {
                int num = Integer.valueOf(input);
                if(num < 0) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_Negative extends Rule {
        public Rule_Negative(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            try {
                int num = Integer.valueOf(input);
                if(num > 0) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_LessThan extends Rule {
        private double maxNumber = Double.MAX_VALUE;

        public Rule_LessThan(String error, double maxNumber) {
            super(error);
            this.maxNumber = maxNumber;
        }

        @Override
        public boolean selfCheck(String input) {
            try {
                Double num = Double.valueOf(input);

                if(num > maxNumber) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_Exist extends Rule {
        private char c = 9999;

        public Rule_Exist(String error, char c) {
            super(error);
            this.c = c;
        }

        @Override
        public boolean selfCheck(String input) {
            if(!input.toLowerCase(Locale.US).contains((c + "").toLowerCase(Locale.US))) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_MaxOfChars extends Rule {
        private char c = 9999;
        private int max = Integer.MAX_VALUE;

        public Rule_MaxOfChars(String error, char c, int max) {
            super(error);
            this.c = c;
            this.max = max;
        }

        @Override
        public boolean selfCheck(String input) {
            String searchString = Pattern.quote(c + "");
            String temp = input.replaceAll(searchString,"");

            if(input.length() - temp.length() > max) {
                return false;
            }
            return true;
        }
    }

    public static class Rule_Email extends Rule {

        public Rule_Email(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
        }
    }

    public static class Rule_Password extends Rule {

        public Rule_Password(String error) {
            super(error);
        }

        @Override
        public boolean selfCheck(String input) {
            Pattern PASSWORD_PATTERN
                    = Pattern.compile(
                    "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");
            return !TextUtils.isEmpty(input) && PASSWORD_PATTERN.matcher(input).matches();
        }
    }
}
