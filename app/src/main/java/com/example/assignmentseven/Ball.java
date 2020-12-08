package com.example.assignmentseven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.Log;

public abstract class Ball {
    protected float velocity;
    protected int xPosition;
    protected int yPosition;
    protected  int radius;
//    protected Paint paint;
    protected Bitmap image;

    abstract protected void move();

    public void draw(Canvas canvas) {
        move();
        canvas.drawBitmap(image, xPosition - radius, yPosition - radius, null);
    }

    public boolean hasCollided(Ball otherBall){
        int dX = xPosition - otherBall.xPosition;
        int dY = yPosition - otherBall.yPosition;
        double dXSquare = Math.pow(dX, 2);
        double dYSquare = Math.pow(dY, 2);
        double distance = Math.sqrt(dXSquare + dYSquare);

        return (distance < radius + otherBall.radius);
    }

    public boolean isOutOfBoundary(int screenWidth, int screenHeight) {
        return (xPosition < -radius || xPosition > screenWidth + radius) ||
                (yPosition < -radius || yPosition > screenHeight + radius);
    }

}
