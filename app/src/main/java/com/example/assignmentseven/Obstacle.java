package com.example.assignmentseven;

import android.content.Context;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import java.util.ArrayList;

public class Obstacle extends Ball{
    private int xDirection;
    private Helper.ObstacleActionTypes action;

    //ObstacleType type;
    Obstacle(int screenWidth, int yPosition, int velocity, ArrayList<Obstacle> obstacles,
             Helper.ObstacleActionTypes[] obstacleTypes, Resources resources){

        // Generating a random obstacle action and color
        int randomIndex = Helper.generateRandomNumber(0, obstacleTypes.length - 1);
        action = obstacleTypes[randomIndex];
        // Setting bitmap depending on ActionTypes

        switch (action){
            case DESTROY:
                image = BitmapFactory.decodeResource(resources, R.drawable.kepler69c);
                break;
            case SPEED_UP:
                image = BitmapFactory.decodeResource(resources, R.drawable.jup);
                break;
            case GAME_OVER:
                image = BitmapFactory.decodeResource(resources, R.drawable.kepler);
                break;
            case SLOW_DOWN:
                image = BitmapFactory.decodeResource(resources, R.drawable.mars);
                break;
            case BOUNCE_BACK:
                image = BitmapFactory.decodeResource(resources, R.drawable.neptune);
                break;
            case TAKE_POINTS:
                image = BitmapFactory.decodeResource(resources, R.drawable.merc);
                break;
            case GIVE_POINTS:
                image = BitmapFactory.decodeResource(resources, R.drawable.venus);
                break;
            default:
                break;
        }
        //jup image retrieved from https://www.nasa.gov/feature/goddard/2020/hubble-captures-crisp-new-portrait-of-jupiters-storms/
        //mars image retrieved from https://solarsystem.nasa.gov/resources/683/valles-marineris-the-grand-canyon-of-mars/?category=planets_mars
        //neptune image retrieved from https://www.nasa.gov/sites/default/files/thumbnails/image/pia01492-main.jpg
        //venus image retrieved from https://solarsystem.nasa.gov/resources/775/venus-computer-simulated-global-view-of-the-northern-hemisphere/?category=planets_venus
        //kepler and kepler69c image retrieved from https://exoplanets.nasa.gov/news/207/finding-another-earth/
        //merc image retrieved from https://solarsystem.nasa.gov/resources/771/colors-of-the-innermost-planet-view-1/?category=planets_mercury

        //depending on device size image size will change so calculate radius
        radius = image.getWidth()/2;

        // Setting initial direction
        xDirection = Helper.INITIAL_DIRECTION;

        // Setting a random horizontal position and direction
        int randomNumber = Helper.generateRandomNumber(0, 1);
        if (randomNumber == 0) {
            xPosition = -radius;
        } else{
            xPosition = screenWidth + radius;
            xDirection *= -Helper.INITIAL_DIRECTION;
        }

        // Setting a unique random initial vertical position
        generateUniqueYPosition(yPosition, obstacles);



//        int color = context.getColor(colors[randomIndex]);

//        // Instantiating the Paint and setting its color
//        paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(Helper.STROKE_WIDTH);
//        paint.setColor(color);

        // Setting the velocity of the obstacle
        this.velocity = velocity;
    }

    public Helper.ObstacleActionTypes getAction() {
        return action;
    }

    private void generateUniqueYPosition(int yPosition, ArrayList<Obstacle> obstacles){
        final int MAX_Y_POSITION_OFFSET = 4;
        // Generating the obstacle at least two balls above the bottom of the screen
        this.yPosition = Helper.generateRandomNumber(radius, yPosition - MAX_Y_POSITION_OFFSET * radius);
        boolean isUnique = true;
        for(int i = 0; i < obstacles.size(); i++) {
            if(!isYPositionUnique(obstacles.get(i))){
                isUnique = false;
            }
        }
        if (!isUnique){
            generateUniqueYPosition(yPosition, obstacles);
        }
    }

    public boolean isYPositionUnique(Ball otherBall){
        final int Y_POSITION_OFFSET = 20;
        int dY = Math.abs(yPosition - otherBall.yPosition);
        return dY > radius + otherBall.radius + Y_POSITION_OFFSET;

    }


    @Override
    public void move() {
        xPosition = xPosition + (int)velocity * xDirection;
    }
}
