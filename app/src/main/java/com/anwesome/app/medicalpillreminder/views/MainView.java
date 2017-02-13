package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import java.util.*;

import com.anwesome.app.medicalpillreminder.AppConstants;
import com.anwesome.app.medicalpillreminder.buttons.BaseButton;

/**
 * Created by anweshmishra on 10/02/17.
 */
public class MainView extends BaseView {
    private List<BaseButton> baseButtonList = new ArrayList();
    private BaseButton currentButton;
    private boolean isAnimated = false;
    private int time = 0;
    public MainView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public MainView(Context context) {
        super(context);
    }
    public void drawElements(Canvas canvas, Paint paint) {
        paint.setTextSize(AppConstants.FONT_SIZE);
        if(time == 0) {
            float totalApproxH = (paint.getTextSize()+20)*baseButtonList.size();
            float y = canvas.getHeight()/5,x = canvas.getWidth()/2;
            float gap = canvas.getHeight()/8;
            if(baseButtonList.size() != 0) {
                gap = (canvas.getHeight() - y - totalApproxH) / baseButtonList.size();
            }
            for(BaseButton baseButton:baseButtonList) {
                baseButton.initProperties(x,y,paint);
                y+=gap;
            }
        }
        for(BaseButton baseButton:baseButtonList) {
            baseButton.draw(canvas,paint);
        }
        if(isAnimated) {
            if (currentButton != null && currentButton.isStopExpanding()) {
                isAnimated = false;
                currentButton.click(this);
            }
            try {
                Thread.sleep(50);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
    public void handleTap(float x,float y) {
        for(BaseButton baseButton:baseButtonList) {
            if(baseButton.handleClick(x,y)) {
                currentButton = baseButton;
                isAnimated = true;
                break;
            }
        }
        if(currentButton!=null) {
            postInvalidate();
        }
    }
    public void setBaseButtonList(List<BaseButton> baseButtonList) {
        this.baseButtonList = baseButtonList;
    }
}
