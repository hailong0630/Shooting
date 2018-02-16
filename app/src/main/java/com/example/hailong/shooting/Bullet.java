package com.example.hailong.shooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by hailong on 2017/12/15.
 */

public class Bullet extends BaseObject {
    private final float SIZE=40f;
    private final Paint paint =new Paint();
    public final float alignX;
    public Bitmap bitmap;
    public final Rect rect;

    public Bullet(Bitmap bitmap, int width, int height, float alignX){
        this.bitmap =bitmap;
        int left =(width-bitmap.getWidth())/2;
        int top =height-bitmap.getHeight();
        int right = left+bitmap.getWidth();
        int bottom = top+bitmap.getHeight();
        rect = new Rect(left,top,right,bottom);

        this.alignX=alignX;


        yPosition=rect.centerY();
        xPosition =rect.centerX();}

    @Override
    public void draw(Canvas canvas) {
        if (status==STATUS_NOMARL){
            canvas.drawBitmap(bitmap,xPosition,yPosition,paint);
    }
    }

    @Override
    public void move() {
        yPosition -=3*MOVE_WEIGHT;
        xPosition +=3*alignX*MOVE_WEIGHT;

    }

    @Override
    public Type getType() {
        return Type.Bullet;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType()!= Type.Missile){
            return false;
        }
        return (calcdistance(this,object)<SIZE);
    }
}
