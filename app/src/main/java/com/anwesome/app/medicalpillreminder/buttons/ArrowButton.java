package com.anwesome.app.medicalpillreminder.buttons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by anweshmishra on 20/02/17.
 */
public class ArrowButton {
    private float x,y,dir,size;
    public ArrowButton(float x,float y,float dir,float size) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.size = size;
    }
    public void draw(Canvas canvas,Paint paint) {
        paint.setColor(Color.GRAY);
        Path path = new Path();
        path.moveTo(x-size/2,y);
        path.lineTo(x,y+dir*size);
        path.lineTo(x+size/2,y);
        canvas.drawPath(path,paint);
    }

    public boolean handleTap(float x,float y) {
        return (x>=this.x-size && x<=this.x+size && ((dir == 1 && y>=this.y && y<=this.y+dir) || (dir == -1 && y>=this.y-dir && y<=this.y)));
    }
    public int hashCode() {
        return (int)x+(int)y+(int)dir;
    }
}
