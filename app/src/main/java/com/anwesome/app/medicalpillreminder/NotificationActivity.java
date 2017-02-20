package com.anwesome.app.medicalpillreminder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anwesome.app.medicalpillreminder.buttons.BaseButton;
import com.anwesome.app.medicalpillreminder.views.BaseView;

/**
 * Created by anweshmishra on 20/02/17.
 */
public class NotificationActivity extends AppCompatActivity{
    private RealmModelUtil realmModelUtil;
    private String pillName = null,message;
    private int pillId = -1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realmModelUtil = new RealmModelUtil(this);
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            pillName = getIntent().getExtras().getString(AppConstants.NAME_EXTRA);
            pillId = getIntent().getExtras().getInt(AppConstants.ID_EXTRA);
            message = AppConstants.PILL_MESSAGE+pillName;
        }
        NotificationView notificationView = new NotificationView(this);
        setContentView(notificationView);
    }
    private class NotificationView extends BaseView {
        private boolean isAnimated = false;
        private BaseButton yesButton;
        private int render = 0;
        public NotificationView(Context context) {
            super(context);
            yesButton = new BaseButton("Yes");
        }
        public void drawElements(Canvas canvas, Paint paint) {
            int w = canvas.getWidth(),h = canvas.getHeight();
            paint.setColor(Color.WHITE);
            paint.setTextSize(h/30);
            if(render == 0) {
                yesButton.initProperties(w/2,3*h/4,paint);
            }
            canvas.drawText(message,w/2-paint.measureText(message)/2,h/4,paint);
            yesButton.draw(canvas,paint);
            render++;
            if(isAnimated) {
                if(yesButton.isStopExpanding()) {
                    isAnimated = false;
                    realmModelUtil.changePillNumber(pillId,-1);
                    finish();
                    System.exit(0);
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
            if(yesButton.handleClick(x,y) && !isAnimated) {
                isAnimated = true;
                postInvalidate();
            }
        }
    }
}
