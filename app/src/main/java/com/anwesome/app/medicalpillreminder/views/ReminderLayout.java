package com.anwesome.app.medicalpillreminder.views;

import android.app.Activity;
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
    private SchedulerView schedulerView;
    private Activity activity;
    public ReminderLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        schedulerView = new SchedulerView(context);
        activity = (Activity)context;
    }
    public ReminderLayout(Context context) {
        super(context);
        schedulerView = new SchedulerView(context);
        activity = (Activity)context;
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
            this.pill = pill;
        }
        public void init(int w,int h) {
            plus = new Icon(9*w/10,2*h/3,w/20){
                public void draw(Canvas c,Paint p) {
                    super.draw(c,p);
                    float gap = (this.r*4)/5;
                    c.drawLine(this.x-gap,this.y,this.x+gap,this.y,p);
                    c.drawLine(this.x,this.y-gap,this.x,this.y+gap,p);

                }
                public void handleRealm() {
                    activity.setContentView(schedulerView);
                }
            };
        }
        public void drawControls(Canvas canvas, Paint paint,int w,int h) {
            String notificationTimes = pill.getNotificationTimes()==null?" ":pill.getNotificationTimes()+"8:30,9:30";
            canvas.drawText(notificationTimes,w/10,2*h/3,paint);
            plus.draw(canvas,paint);
        }
        public void handleTap(float x,float y) {
            plus.handleTap(x,y);
        }
    }
}
