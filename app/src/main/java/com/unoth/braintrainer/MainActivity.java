package com.unoth.braintrainer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        addedOptions();
        playNext();
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