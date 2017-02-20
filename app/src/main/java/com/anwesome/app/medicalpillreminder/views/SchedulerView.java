package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.*;
import android.view.*;

import com.anwesome.app.medicalpillreminder.buttons.ArrowButton;

import java.util.*;

/**
 * Created by anweshmishra on 19/02/17.
 */
public class SchedulerView extends View {
    private boolean isAnimated = false;
    private Path path = new Path();
    private TimeContainer container;
    private int render = 0;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TimeContainer hourContainer,minuteContainer,periodContainer;
    public SchedulerView(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        if(render == 0) {
            init(canvas.getWidth(),canvas.getHeight());
        }
        canvas.drawColor(Color.parseColor("#FF6F00"));
        //canvas.clipPath(path);
        hourContainer.draw(canvas,paint);
        minuteContainer.draw(canvas,paint);
        periodContainer.draw(canvas,paint);
        render++;
        if(isAnimated) {
            update();
            try {
                Thread.sleep(50);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
    private void update() {

        container.update();
    }
    public void init(int w,int h) {
        hourContainer = new TimeContainer();
        hourContainer.setDimensions(w/2-w/3,w/3,h/3,h/4);
        minuteContainer = new TimeContainer();
        minuteContainer.setDimensions(w/2,w/3,h/3,h/4);
        periodContainer = new TimeContainer();
        periodContainer.setDimensions(w/2+w/3,w/3,h/3,h/4);
        periodContainer.setLabels(new LinkedList<String>(){{
            add("AM");
            add("PM");
        }});
        List<String> hours = new LinkedList<>(),minutes = new LinkedList<>();
        for(int i = 0;i<60;i++) {
            String newTime = ""+i;
            if(i<10) {
                newTime = "0"+i;
            }
            if(i<12) {
                hours.add(newTime);
            }
            minutes.add(newTime);
        }
        hourContainer.setLabels(hours);
        minuteContainer.setLabels(minutes);
        path.addRect(new RectF(w/4,h-h/6,3*w/4,h+h/6), Path.Direction.CCW);
    }
    private void handleTap(float x,float y) {
        if(hourContainer.handleTap(x,y)) {
            container = hourContainer;
            return;
        }
        if(minuteContainer.handleTap(x,y)) {
            container = minuteContainer;
            return;
        }
        if (periodContainer.handleTap(x,y)) {
            container = periodContainer;
            return;
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && !isAnimated) {
            handleTap(event.getX(),event.getY());
            if(container !=null) {
                isAnimated = true;
                postInvalidate();
            }
        }
        return true;
    }
    private class TimeContainer {
        private int time  = 0;
        private ArrowButton upArrow,downArrow;
        private List<String> labels = new LinkedList<>();
        private List<TimeBox> timeBoxes = new LinkedList<>();
        private float x = 0, w = 100, h = 100, gap,y;
        private int index = 0,dir = 0,prevIndex = 0,nextIndex;
        private Path path = new Path();
        public void setDimensions(float x, float w, float h,float y) {
            this.x = x;
            this.w = w;
            this.h = h;
            this.y = y;
            this.gap = h / 3;
        }
        private void setUpBoxes() {
            prevIndex = index-1;
            if(prevIndex<0) {
                prevIndex = labels.size()-1;
            }
            nextIndex = index+1;
            if(nextIndex >= labels.size()) {
                nextIndex = 0;
            }
            timeBoxes.add(new TimeBox(labels.get(prevIndex),h/2-gap));
            timeBoxes.add(new TimeBox(labels.get(index),h/2));
            timeBoxes.add(new TimeBox(labels.get(nextIndex),h/2+gap));
        }
        public void setLabels(List<String> labels) {
            this.labels = labels;
            if (labels.size() > 0) {
                index = labels.size() / 2;
            }
            setUpBoxes();
            upArrow = new ArrowButton(x,h/2-gap/2,-1,w/3);
            downArrow = new ArrowButton(x,h/2+gap/2,1,w/3);
            path.moveTo(x-gap/2,h/2-gap/2);
            path.addRect(new RectF(x-gap/2,h/2-gap/2,x+gap/2,h/2+gap/2), Path.Direction.CCW);
        }

        public void draw(Canvas canvas, Paint paint) {
            canvas.save();
            canvas.translate(0,y);
            canvas.save();
            canvas.clipPath(path);
            for (TimeBox timeBox : timeBoxes) {
                timeBox.draw(canvas, paint, x, w);
            }
            canvas.restore();
            upArrow.draw(canvas,paint);
            downArrow.draw(canvas,paint);
            canvas.restore();
        }

        public void update() {
            for (TimeBox timeBox:timeBoxes) {
                if(time == 5) {
                    timeBox.setFinalPosition();
                }
            }
            if(time == 5 && timeBoxes.size()>0) {
                isAnimated = false;
                time = -1;
                container = null;
                index -= dir;
                if(index>=labels.size()) {
                    index = 0;
                }
                if(index<0) {
                    index = labels.size()-1;
                }
                setUpBoxes();
                dir = 0;
            }
            time++;
        }
        public void startMoving() {
            time = 0;
            for(TimeBox timeBox:timeBoxes) {
                timeBox.setSpeed(dir, gap);
            }
        }
        public boolean handleTap(float x,float y) {
            y -= this.y;
            boolean condition = upArrow.handleTap(x,y);
            if(condition) {
                dir = -1;
            }
            else {
                condition = downArrow.handleTap(x, y);
                if (condition) {
                    dir = 1;
                }
            }
            startMoving();
            return condition;
        }

        public int hashCode() {
            return labels.hashCode();
        }
    }

    private class TimeBox {
        private String label;
        private float y, finalY = 0, speed = 0;
        public TimeBox(String label, float y) {
            this.label = label;
            this.y = y;

        }
        public void draw(Canvas canvas, Paint paint, float x, float gap) {
            paint.setColor(Color.WHITE);
            canvas.drawRect(new RectF(x - gap / 2, y - gap / 2, x + gap / 2, y + gap / 2), paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(gap / 4);
            canvas.drawText(label, x - paint.measureText(label) / 2, y, paint);
            y += speed;
        }
        public void setY(float y) {
            this.y = y;
        }
        public float getY() {
            return y;
        }
        public int hashCode() {
            return label.hashCode() + (int) y;
        }

        public void setSpeed(int dir,float gap) {
            finalY = y+dir*gap;
            speed = (finalY - y) / 5;
        }

        public void setFinalPosition() {
            y = finalY;
            speed = 0;
        }

        public String getLabel() {
            return label;
        }
    }
}
