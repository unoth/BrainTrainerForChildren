package com.unoth.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;
    private TextView textViewOption0;
    private TextView textViewOption1;
    private TextView textViewOption2;
    private TextView textViewOption3;
    private ArrayList<TextView> options = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;

    private int countOfQuestions = 0;
    private int countOfRightQuestions = 0;
    private Boolean gameOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        addedOptions();
        playNext();

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilEnd) {
                textViewTimer.setText(getTime(millisUntilEnd));
                if (millisUntilEnd < 5000) {
                    textViewTimer.setTextColor(ContextCompat.getColor(
                                    MainActivity.this,
                                    android.R.color.holo_red_light
                            )
                    );
                }
            }

            @Override
            public void onFinish() {
                SharedPreferences preferences = getApplication()
                        .getSharedPreferences("scoreMax", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                int scoreMax = preferences.getInt("scoreMax", 0);
                if (countOfRightQuestions > scoreMax) {
                    editor.putInt("scoreMax", countOfRightQuestions).apply();
                }
                gameOver = true;
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("result", countOfRightQuestions);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void addedOptions() {
        options.add(textViewOption0);
        options.add(textViewOption1);
        options.add(textViewOption2);
        options.add(textViewOption3);
    }

    private void playNext() {
        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(Integer.toString(rightAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countOfQuestions, countOfRightQuestions);
        textViewScore.setText(score);
    }

    private void initViews() {
        textViewOption0 = findViewById(R.id.textViewOption0);
        textViewOption1 = findViewById(R.id.textViewOption1);
        textViewOption2 = findViewById(R.id.textViewOption2);
        textViewOption3 = findViewById(R.id.textViewOption3);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        rightAnswerPosition = (int) (Math.random() * 4);
        textViewQuestion.setText(question);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int selectedAnswer = Integer.parseInt(answer);
            if (selectedAnswer == rightAnswer) {
                countOfRightQuestions++;
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }

    private String getTime(long millis) {
        int sec = (int) (millis / 1000);
        int minutes = sec / 60;
        sec = sec % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, sec);
    }


}