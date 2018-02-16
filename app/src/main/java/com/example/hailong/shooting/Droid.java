package com.example.hailong.shooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by hailong on 2017/12/14.
 */

public class Droid extends BaseObject{
    private final Paint paint = new Paint();
    public final Bitmap bitmap;
    public final Rect rect;
    public Droid(Bitmap bitmap,int width,int height){
        this.bitmap =bitmap;
        int left =(width-bitmap.getWidth())/2;
        int top =height-bitmap.getHeight();
        int right = left+bitmap.getWidth();
        int bottom = top+bitmap.getHeight();
        rect = new Rect(left,top,right,bottom);

        yPosition=rect.centerY();
        xPosition =rect.centerX();

    }
    @Override
    public void draw(Canvas canvas){


        if(status==STATUS_NOMARL){
        canvas.drawBitmap(bitmap,rect.left,rect.top,paint);

    }}

    @Override
    public Type getType() {
        return Type.Droid;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType() !=Type.Missile){
        return false;}
        return rect.contains(Math.round(object.xPosition),Math.round(object.yPosition));
    }


    public int hit(int m) {
        m+=1;
        return m;

    }

    @Override
    public boolean isAvailable(int width, int height) {
        return true;
    }

    @Override
    public void move() {

    }
}
