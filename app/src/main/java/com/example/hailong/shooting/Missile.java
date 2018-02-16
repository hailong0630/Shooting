package com.example.hailong.shooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by hailong on 2017/12/14.
 */

public class Missile extends BaseObject {
    private final Paint paint = new Paint();
    private static final float SIZE = 80f;
    public final float alignX;
    public Bitmap bitmap;
    public final Rect rect;
    public Missile(Bitmap bitmap,int width,int height,float alignX){
        this.bitmap =bitmap;
        int left =(width-bitmap.getWidth())/2;
        int top =height-bitmap.getHeight();
        int right = left+bitmap.getWidth();
        int bottom = top+bitmap.getHeight();
        rect = new Rect(left,top,right,bottom);

        this.alignX=alignX;


        yPosition=rect.centerY();
        xPosition =rect.centerX();

    }

    @Override
    public void move() {
        yPosition +=1*MOVE_WEIGHT;
        xPosition +=alignX*MOVE_WEIGHT;
    }

    @Override
    public void draw(Canvas canvas) {
       if (status==STATUS_NOMARL){


           canvas.drawBitmap(bitmap,xPosition,yPosition,paint);

       }

    }

    @Override
    public Type getType() {
        return Type.Missile;
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType()==Type.Missile){
            return false;
        }
        return (calcdistance(this,object)<SIZE);
    }
}
