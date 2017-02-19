package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.anwesome.app.medicalpillreminder.models.Pill;

/**
 * Created by anweshmishra on 17/02/17.
 */
public class ReminderLayout extends RefillLayout {
    public ReminderLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public ReminderLayout(Context context) {
        super(context);
    }
    public boolean plusViewRequired() {
        return false;
    }
    public RefillView getView(Context context,Pill pill) {
        return new ReminderView(context,pill);
    }
    public class ReminderView extends RefillView {
        private Pill pill;
        private Icon plus;
        public ReminderView(Context context, Pill pill) {
            super(context,pill);
        }
        public void init(int w,int h) {

        }
        public void drawControls(Canvas canvas, Paint paint,int w,int h) {
            String notificationTimes = pill.getNotificationTimes()+"8:30,9:30";
            canvas.drawText(notificationTimes,w/10,h/6,paint);
        }
        public void handleTap(float x,float y) {

        }
    }
}
