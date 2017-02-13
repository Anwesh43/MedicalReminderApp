package com.anwesome.app.medicalpillreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anwesome.app.medicalpillreminder.views.RefillLayout;

/**
 * Created by anweshmishra on 13/02/17.
 */
public class RefillActivity extends AppCompatActivity {
    RefillLayout refillLayout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refillLayout = new RefillLayout(this);
        setContentView(refillLayout);

    }
}
