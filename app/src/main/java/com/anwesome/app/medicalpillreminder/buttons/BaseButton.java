package com.anwesome.app.medicalpillreminder.buttons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by anweshmishra on 10/02/17.
 */
public class BaseButton {
    private float x,y,w,h;
    private String text;
    private View.OnClickListener onClickListener;
    private float scale = 0,speed = 0f;
    private int backColor = Color.parseColor("#99004D40"),strokeColor = Color.WHITE;
    private boolean stopExpanding = false;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setW(float w) {
        this.w = w;
    }
    public float getH() {
        return h;
    }
    public void initProperties(float x,float y,Paint paint) {
        this.h = paint.getTextSize()+60;
        this.w = paint.measureText(text)+80;
        this.x = x-this.w/2;
        this.y = y;
    }
    public void setH(float h) {
        this.h = h;
    }

    public BaseButton(String text) {
        this.text = text;
    }
    public void draw(Canvas canvas,Paint paint) {
        paint.setStrokeWidth(10);
        float r = h/4;
        if(w>h) {
            r = w/4;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawRoundRect(new RectF(x,y,x+w,y+h),r,r,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,x+w/2-paint.measureText(text)/2,y+h/2+paint.getTextSize()/2,paint);
        paint.setColor(backColor);
        canvas.save();
        canvas.translate(x+w/2,y+h/2);
        canvas.scale(scale,scale);
        canvas.drawRoundRect(new RectF(-w/2,-h/2,w/2,h/2),r,r,paint);
        canvas.restore();
        scale+=speed;
        if(scale >= 1.2f) {
            scale = 0;
            speed = 0;
            stopExpanding = true;

        }
    }
    public void click(View view) {
        if(onClickListener!=null) {
            onClickListener.onClick(view);
        }
    }
    public boolean isStopExpanding() {
        return stopExpanding;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public boolean handleClick(float x,float y) {
        boolean touched =  (x>=this.x && x<=this.x+w && y>=this.y && y<=this.y+h);
        if(touched) {
            stopExpanding = false;
            speed = 0.15f;
        }
        return touched;
    }
    public int hashCode() {
        return (int)x+(int)y+text.hashCode()+(int)h;
    }
}
