package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    // Opens the GameplayActivity when the start button is clicked
    public void onStartClick(View view){
//        Toast.makeText(this, "Start button clicked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, GameplayActivity.class);
        startActivity(intent);
    }

    // Opens the HighScoreActivity when the high score button is clicked
    public void onHighScoreClick(View view){
//        Toast.makeText(this, "High Score button clicked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }


}