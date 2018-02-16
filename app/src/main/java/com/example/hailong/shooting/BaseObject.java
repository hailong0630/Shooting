package com.example.hailong.shooting;

import android.graphics.Canvas;

/**
 * Created by hailong on 2017/12/14.
 */

public abstract class BaseObject {
    static final int STATUS_NOMARL=0;
    static final int STATUS_DESTROYED =1;
    int status=STATUS_NOMARL;
    static final float MOVE_WEIGHT =10.0f;
    float yPosition;
    float xPosition;
    public abstract void draw(Canvas canvas);

    enum Type{
        Droid,
        Bullet,
        Missile,
    }
    public abstract Type getType();

    public abstract boolean isHit(BaseObject object);
    static double calcdistance(BaseObject object1,BaseObject object2){
        float disX = object1.xPosition-object2.xPosition;
        float disY = object1.yPosition-object2.yPosition;
        return Math.sqrt(Math.pow(disX,2)+Math.pow(disY,2));
    }

    public void hit(){
        status=STATUS_DESTROYED;
    }
    public boolean isAvailable(int width,int height){
        if (yPosition<0||xPosition<0||yPosition>height||xPosition>width){
            return false;
        }
        return true;
    }
    public abstract void move();
}
