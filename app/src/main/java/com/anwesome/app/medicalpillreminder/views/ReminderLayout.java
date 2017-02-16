package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

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
        public ReminderView(Context context, Pill pill) {
            super(context,pill);
        }
        public void init(int w,int h) {

        }
        public void drawControls(Canvas canvas, Paint paint,int w,int h) {

        }
    }
}
