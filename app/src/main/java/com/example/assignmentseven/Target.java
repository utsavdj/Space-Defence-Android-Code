package com.example.assignmentseven;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.widget.Toast;

public class Target extends Ball{
    private int score;

    public Target(int xPosition, int velocity, int[] scores, Resources resources){
        // Generating a random target color
        int randomIndex = Helper.generateRandomNumber(0, scores.length - 1);
//        int color = context.getColor(colors[randomIndex]);
        score = scores[randomIndex];

        //loading different sized image for each value of points
        switch (randomIndex)
        {
            case 0:
                image = BitmapFactory.decodeResource(resources, R.drawable.target);
                break;
            case 1:
                image = BitmapFactory.decodeResource(resources, R.drawable.target1);
                break;
            default:
                image = BitmapFactory.decodeResource(resources, R.drawable.target2);
                break;
        }

        //target image retrieved from

        //setting radius based on image size since different pixel densities will have different sized images
        radius = image.getWidth()/2;

        // Setting a random horizontal position
        this.xPosition = Helper.generateRandomNumber(radius, xPosition - radius);

        //Setting the initial vertical position
        this.yPosition = -radius;




//        // Instantiating the Paint and setting its color
//        paint = new Paint();
//        paint.setColor(color);

        // Setting the velocity of the target
        this.velocity = velocity;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void move() {
        yPosition += velocity;
    }
}
