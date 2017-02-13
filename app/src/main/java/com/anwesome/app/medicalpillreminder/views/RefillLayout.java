package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anwesome.app.medicalpillreminder.RealmModelUtil;
import com.anwesome.app.medicalpillreminder.models.Pill;

import io.realm.RealmResults;

/**
 * Created by anweshmishra on 13/02/17.
 */
public class RefillLayout extends ViewGroup{
    private RealmModelUtil realmModelUtil;
    private int width,height;
    private DisplayManager displayManager;
    private boolean isAdded = false,isLoaded;
    private Context context;
    public RefillLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        realmModelUtil = new RealmModelUtil(context);
        this.context = context;
    }
    public RefillLayout(Context context) {
        super(context);
        realmModelUtil = new RealmModelUtil(context);
        this.context = context;
    }

    public void onLayout(boolean changed,int a,int b,int w,int h) {
        int prev = 0,gap = height/6;
        if(!isLoaded) {
            for(int i=0;i<getChildCount();i++){
                View view = getChildAt(i);
                if(view instanceof PlusView) {
                    view.layout(7*width/10,8*height/10,7*width/10+height/10,8*height/10+height/10);
                }
                else {
                    view.layout(0, prev, width, prev + gap);
                    prev += gap;
                }
            }
            isLoaded = true;
        }
    }
    public void onMeasure(int wspec,int hspec) {
        Display display = getDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int w = size.x;
        int h = size.y;
        width = w;
        height = h;
        RealmResults<Pill> pills = realmModelUtil.getPills();
        if(!isAdded) {

            for(Pill pill:pills) {
                RefillView refillView = new RefillView(context,pill);
                addView(refillView,new LayoutParams(w,h/6));
            }
            PlusView plusView = new PlusView(context);
            addView(plusView,new LayoutParams(h/10,h/10));
            isAdded = true;
        }
        for(int i=0;i<getChildCount();i++) {
            View view = getChildAt(i);
            measureChild(view,wspec,hspec);
        }
        setMeasuredDimension(w,Math.max(h,(h/6)*pills.size()));
    }
    class RefillView extends BaseView {
        private Pill pill;
        private Icon add,minus;
        private int time = 0,n;
        public RefillView(Context context, Pill pill) {
            super(context);
            this.pill = pill;
            n = pill.getPillsNumber();
        }
        public void drawElements(final Canvas canvas, Paint paint) {
            int w = canvas.getWidth(),h = canvas.getHeight();
            if(time == 0) {
                add = new Icon(5*w/10,2*h/3,w/20){
                    public void draw(Canvas c,Paint p) {
                        super.draw(c,p);
                        float gap = (this.r*4)/5;
                        c.drawLine(this.x-gap,this.y,this.x+gap,this.y,p);
                        c.drawLine(this.x,this.y-gap,this.x,this.y+gap,p);

                    }
                    public void handleRealm() {
                        n++;
                        realmModelUtil.changePillNumber(pill.getId(),1);
                    }
                };
                minus = new Icon(w/10,2*h/3,w/20){
                    public void draw(Canvas c,Paint p) {
                        super.draw(c,p);
                        float gap = (this.r*4)/5;
                        c.drawLine(this.x-gap,this.y,this.x+gap,this.y,p);
                    }
                    public void handleRealm() {
                        if(n>0) {
                            n--;
                            realmModelUtil.changePillNumber(pill.getId(), -1);
                        }
                    }
                };
            }
            int r = Math.max(w,h)/8;
            paint.setColor(Color.parseColor("#00BCD4"));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(new RectF(0,0,w,h),r,r,paint);
            if(pill!=null) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                canvas.drawText(pill.getName(),w/10,h/3,paint);
                add.draw(canvas,paint);
                canvas.drawText(""+n,3*w/10,2*h/3,paint);
                minus.draw(canvas,paint);
            }
            time++;

        }
        public void handleTap(float x,float y) {
            minus.handleTap(x,y);
            add.handleTap(x,y);
            postInvalidate();
        }
        private class Icon {
            public float x,y,r;
            public Icon(float x,float y,float r) {
                this.x = x;
                this.y = y;
                this.r = r;
            }
            public void draw(Canvas canvas,Paint paint) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(x,y,r,paint);
            }
            private void handleTap(float x,float y) {
                boolean condition =  x>=this.x-this.r && x<=this.x+this.r && y>=this.y-this.r && y<=this.y+this.r;
                if(condition) {
                    handleRealm();
                }
            }
            public void handleRealm() {

            }
        }
    }
    class PlusView extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        public PlusView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            int w = canvas.getWidth()/2,h = canvas.getHeight()/2;
            paint.setColor(Color.parseColor("#3F51B5"));
            canvas.drawCircle(w/2,h/2,Math.min(w,h)/2,paint);
            paint.setColor(Color.WHITE);
            for(int i=0;i<2;i++) {
                canvas.save();
                canvas.translate(w/2,h/2);
                canvas.rotate(i*90);
                canvas.drawLine(-w/3,0,w/3,0,paint);
                canvas.restore();
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }
}
