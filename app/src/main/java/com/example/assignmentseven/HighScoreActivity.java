package com.example.assignmentseven;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class HighScoreActivity extends AppCompatActivity {
    private String[] highScoresTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Retrieving the current score
        Intent intent = getIntent();
        int currentScore = intent.getIntExtra(Helper.SCORE_INTENT, Helper.SCORE_INTENT_DEFAULT_VALUE);

        // Showing the restart button only when the game is over
        setRestartButtonVisibility(currentScore);

        String[] highScoresTitles = getResources().getStringArray(R.array.highscores_array);
        highScoresTexts = new String[highScoresTitles.length];

        // Fetching and setting high scores from shared preferences
        fetchAndSetHighScores(highScoresTitles, currentScore);

        // Setting the high scores in the high scores list board
        setHighScoresListBoard();
    }

    private void setRestartButtonVisibility(int currentScore){
        Button onRestartClickButton = (Button) findViewById(R.id.button_restart);
        if(currentScore == Helper.SCORE_INTENT_DEFAULT_VALUE){
            onRestartClickButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setHighScoresListBoard(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                highScoresTexts){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(getColor(R.color.colorAccent));
                return textView;
            }
        };
        ListView highScoresView = (ListView) findViewById(R.id.lv_high_scores);
        highScoresView.setAdapter(adapter);
    }

    private void fetchAndSetHighScores(String[] highScoresTitles, int currentScore){
        boolean isHighScoreSet = false;
        boolean isEqual = false;

        SharedPreferences sharedPreferences = getSharedPreferences(Helper.HIGH_SCORES_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int totalNumOfHighScores = highScoresTitles.length;
        for (int i = Helper.INITIAL_INDEX; i < totalNumOfHighScores; i++){
            int highScore = sharedPreferences.getInt(highScoresTitles[i], Helper.INITIAL_SCORE);

            // If the current score already exists in the high score board show all the previous high scores
            if(highScore == currentScore){
                isEqual = true;
            }

            if(highScore < currentScore && !isHighScoreSet && !isEqual){
                int secondLastIndex = totalNumOfHighScores - 2;

                // Shifting the high scores to add the new high score
                for(int j = secondLastIndex; j >= i; j--) {
                    int nextIndex = j + 1;
                    editor.putInt(highScoresTitles[nextIndex],
                            sharedPreferences.getInt(highScoresTitles[j], Helper.INITIAL_SCORE));
                }

                // Setting the new high score
                editor.putInt(highScoresTitles[i], currentScore);
                highScoresTexts[i] = String.format(getString(R.string.new_high_score),
                        highScoresTitles[i], String.valueOf(currentScore));
                editor.apply();
                isHighScoreSet = true;
            }else {
                // If the high score is empty
                if (highScore == Helper.INITIAL_SCORE){
                    highScoresTexts[i] = String.format(getString(R.string.empty_high_score),
                            highScoresTitles[i]);
                }else {
                    highScoresTexts[i] = String.format(getString(R.string.high_score),
                            highScoresTitles[i], String.valueOf(highScore));
                }
            }
        }
    }

    public void onRestartClick(View view) {
        //Toast.makeText(this, "restart button clicked!", Toast.LENGTH_SHORT).show();
        finish();
    }
}