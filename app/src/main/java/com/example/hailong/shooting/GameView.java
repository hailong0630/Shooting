package com.example.hailong.shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by hailong on 2017/12/14.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final long VIBRATION_LENGTH_HIT_MISSILE =100;
    private static final long VIBRATION_LENGTH_HIT_DROID =1000;
    private final Vibrator vibrator;
    private static final long FPS= 60;
    private static final int MISSILE_LAUNCH_WEIGHT =50;
    private static final float SCORE_TEXT_SIZE=80.0f;
    private final Paint paintScore=new Paint();
    private long score;
    public int m;

    private final Random rand=new Random();
    private final List<BaseObject> missileList =new ArrayList<>();
    private final List<BaseObject> bulletList =new ArrayList<>();
    private Droid droid;
    private Missile missile;

    public interface Callback{
        public void onGameOver(long score);
    }
    private Callback callback;
    public void setCallback(Callback callback){
        this.callback=callback;
    }
    private Handler handler;

    public GameView(Context context){
        super(context);
        handler=new Handler();

        vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(SCORE_TEXT_SIZE);
        paintScore.setAntiAlias(true);
        getHolder().addCallback(this);
    }
    private class DrawThread extends Thread{
        boolean isFinished;

        @Override
        public void run() {

            SurfaceHolder holder =getHolder();
            while (!isFinished){
                Canvas canvas=holder.lockCanvas();
                if (canvas!=null){
                    drawObject(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    sleep(600/FPS);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private DrawThread drawThread;
    public void startDrawThread(){
        stopDrawThread();
        drawThread = new DrawThread();
        drawThread.start();
    }
    public boolean stopDrawThread(){
        if (drawThread==null){
            return false;
        }
        drawThread.isFinished=true;
        drawThread=null;
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();

    }


    protected void drawObject(Canvas canvas) {



        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Bitmap ground =BitmapFactory.decodeResource(getResources(),R.drawable.sea);
        Paint p =new Paint();
        canvas.drawBitmap(ground,0,0,p);
        if (droid ==null){
            Bitmap droidBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flay);
            droid=new Droid(droidBitmap,width,height);}
            drawObjectList(canvas,missileList,width,height);
            drawObjectList(canvas,bulletList,width,height);
            int fromX=rand.nextInt(getWidth());
            int toX= rand.nextInt(getWidth());
            float alignX =(toX-fromX)/(float)getHeight();

            for (int i=0;i<missileList.size();i++){
                BaseObject missile= missileList.get(i);
                for (int j=0;j<bulletList.size();j++){
                    BaseObject bullet=bulletList.get(j);
                    if (bullet.isHit(missile)){
                        missile.hit();
                        missileList.remove(i);
                        bullet.hit();
                        vibrator.vibrate(VIBRATION_LENGTH_HIT_MISSILE);
                        score+=10;
                    }
                }
            }

            for (int i=0;i<missileList.size();i++){
                BaseObject missile = missileList.get(i);
                if (droid.isHit(missile)){
                    missile.hit();

                    vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID);

                    m+=1;
                    if (m>3){

                    handler.post(new Runnable(){
                        @Override
                        public void run() {

                            callback.onGameOver(score);}

                    });
                    break;
                }
            }}

            if (rand.nextInt(MISSILE_LAUNCH_WEIGHT)==0){
               launchMissile();


        }


        droid.draw(canvas);
        canvas.drawText("Score:"+score,0,SCORE_TEXT_SIZE,paintScore);



    }
    private void launchMissile(){
        Bitmap missileBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.missil1);
        int fromX=rand.nextInt(getWidth());
        int toX= rand.nextInt(getWidth());
        float alignX =(toX-fromX)/(float)getHeight();
        Missile missile =new Missile(missileBitmap,fromX,toX,alignX);
        missileList.add(missile);
    }
    private static void drawObjectList(Canvas canvas,List<BaseObject> objectList,
                                   int width,int height){
        for (int i=0;i<objectList.size();i++){
            BaseObject object =objectList.get(i);
            if (object.isAvailable(width,height)){
                object.move();
                object.draw(canvas);
            }else {
                objectList.remove(object);
                i--;

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                fire(event.getY(),event.getX());
                break;
        }
        return super.onTouchEvent(event);
    }
    private void fire(float y,float x){
        float alignX =(x-droid.rect.centerX())/Math.abs(y-droid.rect.centerY());


        Bitmap bulletBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bullt);
        Bullet bullet =new Bullet(bulletBitmap,(int)droid.xPosition+500,(int)droid.yPosition,alignX);
        bulletList.add(0,bullet);
    }
}
