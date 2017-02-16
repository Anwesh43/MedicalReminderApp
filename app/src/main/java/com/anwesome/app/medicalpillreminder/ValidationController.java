package com.anwesome.app.medicalpillreminder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.*;

/**
 * Created by anweshmishra on 16/02/17.
 */
public class ValidationController {
    private List<EditText> textViews = new ArrayList<>();
    private Map<TextView,ExtraValidator> validatorMap = new HashMap<>();
    public void addView(EditText view,ExtraValidator validator) {
        textViews.add(view);
        validatorMap.put(view,validator);
    }
    public boolean allTextViewsAreValid(){
        boolean allValid = true;
        for(TextView textView:textViews) {
            ExtraValidator validator = validatorMap.get(textView);
            if(textView.getText().toString().trim().equals("")) {
                textView.setError("Please enter value");
                allValid = false;
            }
            else if(validator!=null && !validator.validateExtra()) {
                textView.setError(validator.getErrorMessage());
                allValid = false;
            }
        }
        return allValid;
    }
    public interface ExtraValidator {
        boolean validateExtra();
        String getErrorMessage();
    }
}
