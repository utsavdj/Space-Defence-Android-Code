package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameplayActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView actionTextView;
    private TextView scoreTextView;
    private Toast scoreToast;
    private BallGraphicsView ballGraphicsView;

    public class BallGraphicsView extends View {
        private final int DEFAULT_ARRAY_SIZE = Helper.DEFAULT_ARRAY_SIZE;
        private final int OBSTACLE_VELOCITY_OFFSET = Helper.OBSTACLE_VELOCITY_OFFSET;
        private boolean isInit;
        private int width;
        private int height;
        private Player player;
        private ArrayList<Target> targets;
        private int targetVelocity;
        private int targetCounter;
        private int targetCreationInterval;
        private ArrayList<Obstacle> obstacles;
        private int obstacleVelocity;
        private int obstacleCounter;
        private int obstacleCreationInterval;
        private ArrayList<Obstacle> obstaclesToBeRemoved;
        private ArrayList<Target> targetsToBeRemoved;
        private GestureDetector onFlingDetector;
        private int score;
        private Helper.ObstacleActionTypes[] obstacleTypes;
        private int[] obstacleColors;
        private int[] targetScores;
        private int[] targetColors;
        private boolean isGameOver;
        private Helper helper;
        private Handler handler;

        // Subclassing the view
        public BallGraphicsView(Context context) {
            super(context);
            helper = new Helper();
            handler = new Handler(Looper.getMainLooper());
            obstacleTypes = helper.getObstacleTypes();
            obstacleColors = helper.getObstacleColors();
            targetScores = helper.getTargetScores();
            targetColors = helper.getTargetColors();
            init();
        }

        // Initializes all the default values
        public void init(){
            setStickyImmersiveFullScreenMode();
            isGameOver = false;
            isInit = false;
            score = Helper.INITIAL_SCORE;
            targets = new ArrayList<Target>(DEFAULT_ARRAY_SIZE);
            targetVelocity = Helper.INITIAL_TARGET_VELOCITY;
            targetCounter = Helper.INITIAL_COUNTER;
            targetCreationInterval = Helper.INITIAL_TARGET_CREATION_INTERVAL;
            obstacles = new ArrayList<Obstacle>(DEFAULT_ARRAY_SIZE);
            obstacleVelocity = targetVelocity + OBSTACLE_VELOCITY_OFFSET;
            obstacleCounter = Helper.INITIAL_COUNTER;
            obstacleCreationInterval = Helper.INITIAL_OBSTACLE_CREATION_INTERVAL;
            obstaclesToBeRemoved = new ArrayList<Obstacle>(DEFAULT_ARRAY_SIZE);
            targetsToBeRemoved = new ArrayList<Target>(DEFAULT_ARRAY_SIZE);
            onFlingDetector = new GestureDetector(this.getContext(), new MyOnFlingListener());
            setScoreTextViewText();
        }

        // Creates a new player
        private void createPlayer(){
            player = new Player(width, height, getResources());
        }

        // Creates a new target
        private void createTarget(){
            Target target = new Target(width, targetVelocity, targetScores, getResources());
            targets.add(target);
        }

        // Creates a new obstacle
        private void createObstacle(){
            Obstacle obstacle = new Obstacle(width, height, obstacleVelocity, obstacles,
                    obstacleTypes, getResources());
            obstacles.add(obstacle);
        }

        // Moves all the obstacles on the screen
        private void moveObstacles(Canvas canvas){
            if(obstacleCounter == obstacleCreationInterval){
                createObstacle();
                obstacleCounter = Helper.INITIAL_COUNTER;
            }

            for(int i = Helper.INITIAL_INDEX; i < obstacles.size(); i++){
                Obstacle obstacle = obstacles.get(i);
                // Detecting if the obstacle has collided with the player or if it is out of the boundary of the screen
                boolean hasCollided = obstacle.hasCollided(player);
                if(!hasCollided && !obstacle.isOutOfBoundary(width, height)){
                    obstacle.draw(canvas);
                }else{
                    if(hasCollided) {
                        setObstacleAction(obstacle.getAction());
                    }
                    obstaclesToBeRemoved.add(obstacle);
                }
            }

            // Removing the obstacle if it has collided with the player or if it is out of the boundary of the screen
            for(int i = Helper.INITIAL_INDEX; i < obstaclesToBeRemoved.size(); i++){
                obstacles.remove(obstaclesToBeRemoved.get(i));
            }

            obstacleCounter++;
        }

        // Sets the action of the obstacle upon the player when the player collides with the obstacle
        private void setObstacleAction(Helper.ObstacleActionTypes action){
            final int VELOCITY_OFFSET = 3;
            final int POINTS = 10;
            switch (action){
                case SPEED_UP:
                    player.setYVelocity(player.getYVelocity() * VELOCITY_OFFSET);
                    actionTextView.setText(getString(R.string.speedUp));
                    break;
                case SLOW_DOWN:
                    player.setYVelocity(player.getYVelocity() / VELOCITY_OFFSET);
                    actionTextView.setText(getString(R.string.slowDown));
                    break;
                case BOUNCE_BACK:
                    player.changeYDirection();
                    actionTextView.setText(getString(R.string.bounceBack));
                    break;
                case DESTROY:
                    player.init();
                    actionTextView.setText(getString(R.string.destroy));
                    break;
                case GIVE_POINTS:
                    score += POINTS;
                    actionTextView.setText(getString(R.string.givePoints));
                    setScoreTextViewText();
                    break;
                case TAKE_POINTS:
                    score -= POINTS;
                    actionTextView.setText(getString(R.string.takePoints));
                    setScoreTextViewText();
                    break;
                case GAME_OVER:
                    isGameOver = true;
                    actionTextView.setText(getString(R.string.gameOver));
                    break;
                default:
                    break;
            }

            displayActionTextView();
        }

        private void displayActionTextView(){
            final int TEXT_DISPLAY_DELAY = 600;

            actionTextView.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide your View after 3 seconds
                    actionTextView.setVisibility(View.INVISIBLE);
                }
            }, TEXT_DISPLAY_DELAY);
        }

        private void setScoreTextViewText(){
            scoreTextView.setText(getString(R.string.gameplay_score, String.valueOf(score)));
        }

        // Moves all the targets on the screen
        private void moveTargets(Canvas canvas){
            if(targetCounter == targetCreationInterval){
                createTarget();
                targetCounter = Helper.INITIAL_COUNTER;
            }

            for(int i = Helper.INITIAL_INDEX; i < targets.size(); i++){
                Target target = targets.get(i);
                // Detecting if the target has collided with the player or if it is out of the boundary of the screen
                if(!target.hasCollided(player) && !target.isOutOfBoundary(width, height)){
                    target.draw(canvas);
                }else{
                    if(target.hasCollided(player)) {
                        score += target.getScore();
                        scoreToast.setText(getString(R.string.gameplay_add_point,
                                String.valueOf(target.getScore())));
                        scoreToast.show();
                        setScoreTextViewText();
                    }

                    if(target.isOutOfBoundary(width, height)){
                        isGameOver = true;
                    }

                    targetsToBeRemoved.add(target);
                }
            }

            // Removing the target if it has collided with the player or if it is out of the boundary of the screen
            for(int i = Helper.INITIAL_INDEX; i < targetsToBeRemoved.size(); i++){
                targets.remove(targetsToBeRemoved.get(i));
            }

            targetCounter++;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (!isInit){
                // Setting the dimensions of the canvas on initialization
                width = getWidth();
                height = getHeight();
                createPlayer();
                // Initial target
                createTarget();
                isInit = true;
            }

            moveTargets(canvas);
            moveObstacles(canvas);
            if(!player.isOutOfBoundary(width, height)) {
                player.draw(canvas);
            }else {
                // Recreate the player when the player is out of the vertical boundary of the screen
                player.init();
            }

            // Stop redrawing the view if the the game is over
            if(!isGameOver) {
                invalidate();
            }else {
                setGameOverToast();
            }
        }

        private void setGameOverToast(){
            final int HIGH_SCORES_SCREEN_LAUNCH_DELAY = 1200;
            scoreToast.setText(getString(R.string.game_over));
            scoreToast.setDuration(Toast.LENGTH_SHORT);
            scoreToast.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Opens the high scores screen
                    openHighScoresScreen();
                }
            }, HIGH_SCORES_SCREEN_LAUNCH_DELAY);
        }

        private void openHighScoresScreen(){
            Intent intent = new Intent(getContext(), HighScoreActivity.class);
            intent.putExtra(Helper.SCORE_INTENT, score);
            startActivity(intent);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(onFlingDetector.onTouchEvent(event)){
                return true;
            }
            return super.onTouchEvent(event);
        }

        class  MyOnFlingListener extends GestureDetector.SimpleOnGestureListener {

            private final int MAX_VELOCITY_CONSTRAINT_POSITIVE = 10;
            private final int MAX_VELOCITY_CONSTRAINT_NEGATIVE = -10;
            private final int SLOW_DOWN_FACTOR = 200;

            @Override
            public boolean onDown(MotionEvent e) {
                //if the onDown x, y location is within the player radius do the onFling event
                return (player.hasSelected(e.getX(), e.getY()));
            }



            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float xVelocity, float yVelocity){

                // Constraint to prevent the player to be flung more than once
                if(!player.getIsFlung()) {
                    //onFling xVelocity and yVelocity are too fast and need to be slowed
                    xVelocity = xVelocity / SLOW_DOWN_FACTOR;
                    yVelocity = yVelocity / SLOW_DOWN_FACTOR;

                    // Constraint to prevent the player to be flung downwards on the screen
                    if (yVelocity < 0) {
                        //if xVelocity or yVelocity is < FLING_MAX_VELOCITY_CONSTRAINT constrain it
                        if (xVelocity > MAX_VELOCITY_CONSTRAINT_POSITIVE) {
                            xVelocity = MAX_VELOCITY_CONSTRAINT_POSITIVE;
                        }
                        if (xVelocity < MAX_VELOCITY_CONSTRAINT_NEGATIVE) {
                            xVelocity = MAX_VELOCITY_CONSTRAINT_NEGATIVE;
                        }
                        if (yVelocity > MAX_VELOCITY_CONSTRAINT_POSITIVE) {
                            yVelocity = MAX_VELOCITY_CONSTRAINT_POSITIVE;
                        }
                        if (yVelocity < MAX_VELOCITY_CONSTRAINT_NEGATIVE) {
                            yVelocity = MAX_VELOCITY_CONSTRAINT_NEGATIVE;
                        }

                        //set player velocities to the x, y velocities from onFling event
                        player.setXVelocity(xVelocity);
                        player.setYVelocity(yVelocity);
                        player.setIsFlung(true);
                    }
                }

                return true;
            }
        }
    }

    @Override
    public void onRestart()
    { //re initialises ballGraphics view after restart button is pressed on highscores
        super.onRestart();
        ballGraphicsView.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        addTextToScreen();
        ballGraphicsView = new BallGraphicsView(this);

        // Retrieving the constraint layout from the gameplay XML file using its id
        ConstraintLayout constraintLayoutGameplay = (ConstraintLayout) findViewById(R.id.cl_gameplay);
        // Adding the BallGraphicsView to the constraint layout
        constraintLayoutGameplay.addView(ballGraphicsView);

        setAccelerometer();
    }

    private void addTextToScreen(){
        actionTextView = (TextView) findViewById(R.id.tv_gameplay_action);
        scoreTextView = (TextView) findViewById(R.id.tv_gameplay_score);
        scoreToast = Toast.makeText(getApplicationContext(), R.string.emptyString, Toast.LENGTH_SHORT);
        scoreToast.setGravity(Gravity.TOP, Helper.TOAST_OFFSET, Helper.TOAST_OFFSET);
    }

    private void setStickyImmersiveFullScreenMode(){
        // Hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Enabling sticky immersive mode and enabling drawing over the navigation bar
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    private void setAccelerometer(){
        // Setting the default accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null){
            Toast.makeText(this,
                    R.string.accelerometerNotAvailable,
                    Toast.LENGTH_SHORT).show();
        }
    }

    // An event listener to receive information from the accelerometer
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            final float SENSOR_X_CONSTRAINT = 2;
            float sensorX = sensorEvent.values[0];

            // Moving the player horizontally as per the direction of the screen
            if(ballGraphicsView.isInit && Math.abs(sensorX) > SENSOR_X_CONSTRAINT
                    && ballGraphicsView.player.getIsFlung()) {
                if(ballGraphicsView.player.getXDirection() == -1) {
                    ballGraphicsView.player.setXVelocity(sensorX);
                }else {
                    ballGraphicsView.player.setXVelocity(-sensorX);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Registering the accelerometer to a sensor event listener
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Removing the accelerometer from the sensor event listener to prevent battery drainage when the app is not in use
        sensorManager.unregisterListener(sensorEventListener, accelerometer);
    }


}