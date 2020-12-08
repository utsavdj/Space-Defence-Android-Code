package com.example.assignmentseven;

import android.content.res.Resources;
import android.media.Image;

import java.util.Random;

public class Helper {
    public static final int RADIUS = 50;
    public static final int DEFAULT_ARRAY_SIZE = 20;
    public static final int OBSTACLE_VELOCITY_OFFSET = 1;
    public static final int INITIAL_TARGET_CREATION_INTERVAL = 100;
    public static final int INITIAL_OBSTACLE_CREATION_INTERVAL = 125;
    public static final int INITIAL_COUNTER = 0;
    public static final int INITIAL_DIRECTION = 1;
    public static final int INITIAL_INDEX = 0;
    public static final int INITIAL_SCORE = 0;
    public static final int TOAST_OFFSET = 0;
    public static final int INITIAL_TARGET_VELOCITY = 2;
    public static final int INITIAL_PLAYER_VELOCITY = 0;
    public static final int STROKE_WIDTH = 8;
    public static final int PLAYER_STROKE_WIDTH = 15;
    public static final String SCORE_INTENT = "score";
    public static final int SCORE_INTENT_DEFAULT_VALUE = -1;
    public static final int TOTAL_NUMBER_OF_HIGH_SCORES = 5;
    public static final String HIGH_SCORES_SHARED_PREFERENCES = "HighScoresSharedPref";


    private int[] targetScores = {2,5,10};
    private int[] targetColors = {R.color.colorTargetOne, R.color.colorTargetTwo, R.color.colorTargetThree,
            R.color.colorTargetFour, R.color.colorTargetFive, R.color.colorTargetSix};

    // Setting up the ObstacleTypes enum into an array
    private ObstacleActionTypes[] obstacleTypes = {ObstacleActionTypes.SPEED_UP, ObstacleActionTypes.SLOW_DOWN,
            ObstacleActionTypes.BOUNCE_BACK, ObstacleActionTypes.DESTROY, ObstacleActionTypes.GIVE_POINTS,
            ObstacleActionTypes.TAKE_POINTS, ObstacleActionTypes.GAME_OVER};
    private int[] obstacleColors = {R.color.colorSpeedUp, R.color.colorSlowDown, R.color.colorBounceBack,
            R.color.colorDestroy, R.color.colorGivePoints, R.color.colorTakePoints, R.color.colorGameOver};

    public enum ObstacleActionTypes {
        SPEED_UP,
        SLOW_DOWN,
        BOUNCE_BACK,
        DESTROY,
        GIVE_POINTS,
        TAKE_POINTS,
        GAME_OVER,
    }

    // Generate a random integer between minNumber (inclusive) and maxNumber (inclusive);
    public static int generateRandomNumber(int minNumber, int maxNumber){
        // Added 1 to make maximum number inclusive
        return new Random().nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    public ObstacleActionTypes[] getObstacleTypes() {
        return obstacleTypes;
    }

    public int[] getObstacleColors() {
        return obstacleColors;
    }

    public int[] getTargetScores() {
        return targetScores;
    }

    public int[] getTargetColors() {
        return targetColors;
    }
}
