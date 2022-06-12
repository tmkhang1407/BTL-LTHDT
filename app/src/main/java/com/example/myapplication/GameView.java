package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GameView extends View {
    private Bitmap bmGrass1, bmGrass2, bmSnake;
    public static int sizeOfMap = 75*Constants.SCREEN_WIDTH/1080 ;
    private int h = 28, w = 14;
    private ArrayList<Grass> arrGrass = new ArrayList<>();
    private Snake snake;
    private boolean move=false;
    private float mx, my;
    private Handler handler;
    private Runnable r;
    public GameView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.grass);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeOfMap, sizeOfMap, true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.grass01);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeOfMap, sizeOfMap, true);
        bmSnake = BitmapFactory.decodeResource(this.getResources(),R.drawable.snake1);
        bmSnake = Bitmap.createScaledBitmap(bmSnake, 14*sizeOfMap, sizeOfMap, true);
        for(int i=-1; i<h; i++){
            for(int j=0; j<w; j++){
                if ((i+j)%2==0){
                    arrGrass.add(new Grass(bmGrass1, j*sizeOfMap+Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                }
                else{
                    arrGrass.add(new Grass(bmGrass2, j*sizeOfMap+Constants.SCREEN_WIDTH/2-(w/2)*sizeOfMap,
                            i*sizeOfMap+100*Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                }
            }
        }
        snake = new Snake(bmSnake, arrGrass.get(126).getX(), arrGrass.get(126).getY(), 10);
        handler = new Handler();
        r=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a=event.getActionMasked();
        switch (a){
            case MotionEvent.ACTION_MOVE:{
                if(move==false){
                    mx=event.getX();
                    my=event.getY();
                    move=true;
                }else{
                    if(mx-event.getX()>100*Constants.SCREEN_WIDTH/1080 && !snake.isMove_right()){
                        mx=event.getX();
                        my=event.getY();
                        snake.setMove_left(true);
                    }else if(event.getX()-mx>100*Constants.SCREEN_WIDTH/1080 && !snake.isMove_left()){
                        mx=event.getX();
                        my=event.getY();
                        snake.setMove_right(true);
                    }else if(my-event.getY()>100*Constants.SCREEN_WIDTH/1080 && !snake.isMove_bottom()){
                        mx=event.getX();
                        my=event.getY();
                        snake.setMove_top(true);
                    }else if(event.getY()-my>100*Constants.SCREEN_WIDTH/1080 && !snake.isMove_top()){
                        mx=event.getX();
                        my=event.getY();
                        snake.setMove_bottom(true);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                mx=0;
                my=0;
                move=false;
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(0xFF023000);
        for(int i = 0; i < arrGrass.size(); i++){
            canvas.drawBitmap(arrGrass.get(i).getBm(), arrGrass.get(i).getX(), arrGrass.get(i).getY(), null);
        }
        snake.update();
        snake.draw(canvas);
        handler.postDelayed(r, 200);
    }
}
