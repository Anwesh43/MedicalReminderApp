package com.anwesome.app.medicalpillreminder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.anwesome.app.medicalpillreminder.R;
import com.anwesome.app.medicalpillreminder.RealmModelUtil;
import com.anwesome.app.medicalpillreminder.ValidationController;
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
        int gap = height/8,prev = gap/6;
        if(!isLoaded) {
            for(int i=0;i<getChildCount();i++){
                View view = getChildAt(i);
                if(view instanceof PlusView && plusViewRequired()) {
                    view.layout(7*width/10,7*height/10,7*width/10+height/10,7*height/10+height/10);
                }
                else {
                    view.layout(0, prev, width, prev + gap);
                    prev += gap+gap/6;
                }
            }
            setBackgroundColor(Color.parseColor("#FF6F00"));
            isLoaded = true;
        }
    }
    public void showInputDialog() {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Add New Pill");
        activity.setContentView(R.layout.dialog_add_pill);
        final EditText pillNameText = (EditText) activity.findViewById(R.id.pill_name);
        final EditText pillNosText = (EditText) activity.findViewById(R.id.pill_number);
        final Button add = (Button)activity.findViewById(R.id.add);
        final ValidationController validationController = new ValidationController();
        validationController.addView(pillNameText,null);
        validationController.addView(pillNosText, new ValidationController.ExtraValidator() {
            @Override
            public boolean validateExtra() {
                try {
                    int number = Integer.parseInt(pillNosText.getText().toString());
                    return true;
                }
                catch (Exception ex) {

                }
                return false;
            }

            @Override
            public String getErrorMessage() {
                return "Please enter a number";
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationController.allTextViewsAreValid()) {
                    String pillName = pillNameText.getText().toString();
                    int pillNumber = Integer.parseInt(pillNosText.getText().toString());
                    realmModelUtil.createPill(pillName,pillNumber);
                    actionBar.setTitle("Pill Refill");
                    activity.setContentView(new RefillLayout(context));
                }
            }
        });
    }
    public boolean plusViewRequired() {
        return true;
    }
    public void onMeasure(int wspec,int hspec) {
        Display display = getDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int w = size.x;
        int h = size.y;
        width = w;
        height = h;

        if(!isAdded) {
            addViewsInLayout(w,h);
            isAdded = true;
        }
        for(int i=0;i<getChildCount();i++) {
            View view = getChildAt(i);
            measureChild(view,wspec,hspec);
        }
        setMeasuredDimension(w,Math.max(h,(h/6)*getChildCount()));
    }
    public void addViewsInLayout(int w,int h) {
        RealmResults<Pill> pills = realmModelUtil.getPills();
        for(Pill pill:pills) {
            RefillView refillView = getView(context,pill);
            addView(refillView,new LayoutParams(w,h/8));
        }
        if(plusViewRequired()) {
            PlusView plusView = new PlusView(context);
            addView(plusView, new LayoutParams(h / 9, h / 9));
        }
    }
    public RefillView getView(Context context,Pill pill) {
        return new RefillView(context,pill);
    }
    public class RefillView extends BaseView {
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
                init(w,h);
            }
            int r = Math.max(w,h)/12;
            paint.setColor(Color.parseColor("#3F51B5"));
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRoundRect(new RectF(0,0,w,h),r,r,paint);
            if(pill!=null) {
                paint.setStrokeWidth(6);
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawText(pill.getName(),w/2-paint.measureText(pill.getName())/2,h/3,paint);
                drawControls(canvas,paint,w,h);
            }
            time++;

        }
        public void drawControls(Canvas canvas,Paint paint,int w,int h) {
            add.draw(canvas,paint);
            canvas.drawText(""+n,w/5+3*w/10,2*h/3,paint);
            minus.draw(canvas,paint);
        }
        public void init(int w,int h) {
            add = new Icon(w/5+5*w/10,2*h/3,w/20){
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
            minus = new Icon(w/5+w/10,2*h/3,w/20){
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
        public void handleTap(float x,float y) {
            minus.handleTap(x,y);
            add.handleTap(x,y);
            postInvalidate();
        }
        public class Icon {
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
        private boolean isAnimated = false;
        private float scale = 0.8f,dir = 0;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        public PlusView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            int w = canvas.getWidth(),h = canvas.getHeight();
            paint.setColor(Color.parseColor("#c62828"));
            canvas.save();
            canvas.translate(w/2,h/2);
            canvas.scale(scale,scale);
            canvas.drawCircle(0,0,Math.max(w,h)/2,paint);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(h/10);
            for(int i=0;i<2;i++) {
                canvas.save();
                canvas.rotate(i*90);
                canvas.drawLine(-w/3,0,w/3,0,paint);
                canvas.restore();
            }
            canvas.restore();
            if(isAnimated) {
                scale+=0.1f*dir;
                if(scale>=1f) {
                    dir = -1;
                    scale = 1f;
                }
                if(scale<=0.8f) {
                    dir = 0;
                    scale = 0.8f;
                    isAnimated = false;
                    showInputDialog();
                }
                try {
                    Thread.sleep(50);
                    invalidate();
                }
                catch (Exception ex) {

                }
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN && !isAnimated) {
                dir = 1;
                isAnimated = true;
                postInvalidate();
            }
            return true;
        }
    }
}
