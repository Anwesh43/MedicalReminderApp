package com.anwesome.app.medicalpillreminder;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.anwesome.app.medicalpillreminder.views.ReminderLayout;

/**
 * Created by anweshmishra on 17/02/17.
 */
public class ReminderActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ReminderLayout reminderLayout = new ReminderLayout(this);
        setContentView(reminderLayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Pill Reminder");
    }
}
