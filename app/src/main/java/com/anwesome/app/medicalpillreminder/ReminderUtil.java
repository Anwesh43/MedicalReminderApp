package com.anwesome.app.medicalpillreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationIntent = PendingIntent.getActivity(activity,0,intent,0);
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        int hours = 0;
        if(period == "PM") {
            hours = 12;
        }
        hours = hours+Integer.parseInt(hour);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hours);
        calendar.set(Calendar.MINUTE,Integer.parseInt(minute));
        alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),5*60*1000,notificationIntent);
    }
    class ReminderService extends Service {
        public void onCreate() {

        }
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
