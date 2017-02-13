package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by anweshmishra on 10/02/17.
 */
public class BaseView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public BaseView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public BaseView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#D84315"));
        drawElements(canvas,paint);
    }
    public void drawElements(Canvas canvas,Paint paint) {

    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            handleTap(event.getX(),event.getY());
        }
        return true;
    }
    public void handleTap(float x,float y) {

    }

}
