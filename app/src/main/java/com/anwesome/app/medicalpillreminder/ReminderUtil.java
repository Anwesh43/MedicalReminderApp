package com.anwesome.app.medicalpillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import com.anwesome.app.medicalpillreminder.models.Pill;

import java.util.Calendar;

/**
 * Created by anweshmishra on 20/02/17.
 */
public class ReminderUtil {
    public static void createReminderForPill(Activity activity,Pill pill, String hour, String minute, String period) {
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(activity,NotificationActivity.class);
        intent.putExtra(AppConstants.NAME_EXTRA,pill.getName());
        intent.putExtra(AppConstants.ID_EXTRA,pill.getId());
        PendingIntent notificationIntent = PendingIntent.getActivity(activity,0,intent,0);
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        int hours = 0;
        if(period == "PM") {
            hours = 12;
        }
        hours = hours+Integer.parseInt(hour);
        calendar.set(Calendar.HOUR_OF_DAY,hours);
        calendar.set(Calendar.MINUTE,Integer.parseInt(minute));
        alarmManager.setRepeating(AlarmManager.RTC,AlarmManager.INTERVAL_DAY,calendar.getTimeInMillis(),notificationIntent);
    }
}
