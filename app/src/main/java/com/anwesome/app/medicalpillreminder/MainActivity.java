package com.anwesome.app.medicalpillreminder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.anwesome.app.medicalpillreminder.buttons.BaseButton;
import com.anwesome.app.medicalpillreminder.models.Pill;
import com.anwesome.app.medicalpillreminder.views.MainView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    MainView mainView;
    RealmModelUtil realmModelUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView =  new MainView(this);
        List<BaseButton> baseButtons = new ArrayList<BaseButton>() {{
            add(new BaseButton("Pill Reminder"));

        }};
        BaseButton pillRefill = new BaseButton("Pill Refill");
        pillRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RefillActivity.class);
                startActivity(intent);
            }
        });
        baseButtons.add(pillRefill);
        baseButtons.add(new BaseButton("Contact Physician"));
        mainView.setBaseButtonList(baseButtons);
        setContentView(mainView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Main Menu");
    }
}
