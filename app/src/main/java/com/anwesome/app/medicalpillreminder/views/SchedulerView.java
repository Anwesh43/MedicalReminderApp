package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by anweshmishra on 19/02/17.
 */
public class SchedulerView extends View {
    private boolean isAnimated = false;
    private Path path = new Path();
    private int render = 0;
    private GestureDetector gestureDetector;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TimeContainer hourContainer,minuteContainer,periodContainer;
    public SchedulerView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context,new SchedulerGestureListener());
    }

    public void onDraw(Canvas canvas) {
        if(render == 0) {
            init(canvas.getWidth(),canvas.getHeight());
        }
        canvas.clipPath(path);
        hourContainer.draw(canvas,paint);
        minuteContainer.draw(canvas,paint);
        periodContainer.draw(canvas,paint);
        render++;
        if(isAnimated) {
            update(hourContainer,minuteContainer,periodContainer);
            try {
                Thread.sleep(50);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
    private void update(TimeContainer ...timeContainers) {
        for(TimeContainer timeContainer:timeContainers) {
            if(timeContainer!=null) {
                timeContainer.update();
            }
        }
    }
    public void init(int w,int h) {
        hourContainer = new TimeContainer();
        hourContainer.setDimensions(w/2-w/3,w/3,h/3);
        minuteContainer = new TimeContainer();
        minuteContainer.setDimensions(w/2,w/3,h/3);
        periodContainer.setDimensions(w/2+w/3,w/3,h/3);
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
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    private class SchedulerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private TimeContainer container;
        private void handleDown(float x,TimeContainer ...timeContainers) {
            for(TimeContainer timeContainer:timeContainers) {
                if(timeContainer.handleTap(x)) {
                    container = timeContainer;
                    break;
                }
            }
        }
        public boolean onDown(MotionEvent event) {
            handleDown(event.getX(),hourContainer,minuteContainer,periodContainer);
            return true;
        }

        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velx, float vely) {
            if(container!=null) {
                if (e1.getY() < e2.getY()) {
                    container.startMoving(1);
                }
                if (e1.getY() > e2.getY()) {
                    container.startMoving(-1);
                }
                isAnimated = true;
                postInvalidate();
            }
            return true;
        }
    }
    private class TimeContainer {
        private int time  = 0;
        private List<String> labels = new LinkedList<>();
        private List<TimeBox> timeBoxes = new LinkedList<>();
        private float x = 0, w = 100, h = 100, gap;
        private float speed = 0;
        private int index = 0,dir = 0;

        public void setDimensions(float x, float w, float h) {
            this.x = x;
            this.w = w;
            this.h = h;
            this.gap = h / 3;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
            if (labels.size() > 0) {
                index = labels.size() / 2;
            }
            int currIndex = 0;
            for (String label : labels) {
                boolean current = index == currIndex;
                timeBoxes.add(new TimeBox(label, h / 2 - (index - currIndex) * gap, current));
                currIndex++;
            }
        }

        public void draw(Canvas canvas, Paint paint) {
            for (TimeBox timeBox : timeBoxes) {
                timeBox.draw(canvas, paint, x, w);
            }
        }

        public void update() {
            for (TimeBox timeBox:timeBoxes) {
                if(time == 3) {
                    timeBox.setFinalPosition();
                }
            }
            if(time == 3) {
                isAnimated = false;
                time = -1;
                if(dir == -1) {
                    TimeBox timeBox = timeBoxes.remove(0);
                    timeBoxes.add(timeBox);
                }
                else {
                    TimeBox prevTimeBox = timeBoxes.remove(timeBoxes.size()-1);
                    for(int i=0;i<timeBoxes.size();i++) {
                        TimeBox timeBox = null;
                        if(i < timeBoxes.size()) {
                            timeBox = timeBoxes.get(i);
                        }
                        timeBoxes.set(i,prevTimeBox);
                        prevTimeBox = timeBox;
                    }
                }
                dir = 0;
            }
            time++;
        }
        public void startMoving(int dir) {
            this.dir = dir;
            time = 0;
            for(TimeBox timeBox:timeBoxes) {
                timeBox.setSpeed(dir, gap);
            }
        }
        public boolean handleTap(float x) {
            return x >= this.x && x <= this.x;
        }

        public int hashCode() {
            return labels.hashCode();
        }
    }

    private class TimeBox {
        private String label;
        private float y, finalY = 0, speed = 0;
        private boolean current = false;

        public TimeBox(String label, float y, boolean current) {
            this.label = label;
            this.y = y;
            this.current = current;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }

        public void draw(Canvas canvas, Paint paint, float x, float gap) {
            if (current) {
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(Color.GRAY);
            }
            canvas.drawRect(new RectF(x - gap / 2, y - gap / 2, x + gap / 2, y + gap / 2), paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(gap / 4);
            canvas.drawText(label, x - paint.measureText(label) / 2, y, paint);
            y += speed;
        }

        public int hashCode() {
            return label.hashCode() + (int) y;
        }

        public void setSpeed(int dir,float gap) {
            finalY = y+dir*gap;
            speed = (finalY - y) / 3;
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
