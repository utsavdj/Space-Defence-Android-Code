package com.example.assignmentseven;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Player extends Ball {
    private float xVelocity;
    private float yVelocity;
    private int screenWidth;
    private int screenHeight;
    private int xDirection;
    private int yDirection;
    private boolean isFlung;
//    private Paint strokePaint;

    Player(int xPosition, int yPosition, Resources resources)
    {
        //Setting the width and height of the screen
        screenWidth = xPosition;
        screenHeight = yPosition;

//        // Instantiating the Paint and setting its color
//        paint = new Paint();
//        paint.setColor(color);
//
//        // Setting the stroke for the player to make it distinct
//        strokePaint = new Paint();
//        strokePaint.setStyle(Paint.Style.STROKE);
//        strokePaint.setStrokeWidth(Helper.PLAYER_STROKE_WIDTH);
//        strokePaint.setColor(strokeColor);

        //instantiating the bitmap
        image = BitmapFactory.decodeResource(resources, R.drawable.player);
        radius = image.getWidth()/2;

        // Setting the values for the player
        init();
    }

    public void init(){
        isFlung = false;

        final int X_POSITION_DIVISOR = 2;
        final int Y_POSITION_OFFSET = 20;

        //Setting the initial direction
        xDirection = Helper.INITIAL_DIRECTION;
        yDirection = Helper.INITIAL_DIRECTION;

        // Setting the initial horizontal position
        this.xPosition = screenWidth / X_POSITION_DIVISOR;
        // Setting the initial vertical position
        this.yPosition = screenHeight - radius - Y_POSITION_OFFSET;

        // Setting the initial velocity of the player
        xVelocity = Helper.INITIAL_PLAYER_VELOCITY;
        yVelocity = Helper.INITIAL_PLAYER_VELOCITY;
    }

//    @Override
//    public void draw(Canvas canvas) {
//        move();
//        canvas.drawCircle(xPosition, yPosition, RADIUS, paint);
//        canvas.drawCircle(xPosition, yPosition, RADIUS, strokePaint);
//    }

    public boolean hasSelected(float x, float y) {
        //if the location x,y is within the radius of the Ball object return true else false
        return Math.pow(radius, 2) > Math.pow((xPosition - x), 2) + (Math.pow((yPosition - y), 2));
    }

    public int getXDirection() {
        return xDirection;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void changeYDirection(){
        this.yDirection *= -Helper.INITIAL_DIRECTION;
    }

    private void changeXDirection(){
        this.xDirection *= -Helper.INITIAL_DIRECTION;
    }

    public boolean getIsFlung(){
        return isFlung;
    }

    public void setIsFlung(boolean isFlung){
        this.isFlung = isFlung;
    }

    private void setDirection(){
        final int NEW_Y_VELOCITY = -5;
        if(xPosition <= radius || xPosition >= screenWidth - radius) {
            changeXDirection();
            if(xPosition <= radius){
                xPosition = radius;
            }
            if (xPosition >= screenWidth - radius){
                xPosition = screenWidth - radius;
            }
            yVelocity = NEW_Y_VELOCITY;
        }
    }

    @Override
    public void move() {
        setDirection();
        xPosition += (xVelocity * xDirection);
        yPosition += (yVelocity * yDirection);
    }
}
