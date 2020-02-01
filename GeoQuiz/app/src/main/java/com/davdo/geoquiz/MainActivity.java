package com.davdo.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;

    private Button mSkipButton;
    private Button mBackButton;

    private TextView mQuestionDisplay;
    private TextView mAnswerResult;

    private TextView mScoreDisplay;

    private boolean disableButtons = false;

    private Quiz mQuizObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrueButton = findViewById(R.id.button_true);
        mFalseButton = findViewById(R.id.button_false);
        mSkipButton = findViewById(R.id.button_skip);
        mBackButton = findViewById(R.id.button_back);

        //mTrueButton.setBackgroundColor(Color.GREEN);
        //mFalseButton.setBackgroundColor(Color.RED);

        mQuestionDisplay = findViewById(R.id.text_view_question);
        mAnswerResult = findViewById(R.id.text_view_result);
        mScoreDisplay = findViewById(R.id.text_view_score);

        mQuizObj = new Quiz();

        updateQuestionDisplay();
        updateScore();

        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(disableButtons) return;

                onChoice(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(disableButtons) return;

                onChoice(false);
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(disableButtons) return;

                mQuizObj.progressQuestion();
                updateQuestionDisplay();

                Toast.makeText(MainActivity.this, R.string.toast_skip, Toast.LENGTH_SHORT).show();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(disableButtons) return;

                mQuizObj.progressQuestion(true);
                updateQuestionDisplay();

                Toast.makeText(MainActivity.this, R.string.toast_back, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onChoice(boolean choice) {

        boolean result = mQuizObj.selectAnswer(choice);
        displayResult(result);

        pauseButtonInput();
    }

    public void updateQuestionDisplay() {

        int index = mQuizObj.getQuestionIndex();
        Question currentQ = mQuizObj.getCurrentQuestion();

        mQuestionDisplay.setText(String.format(getString(R.string.text_question), index+1, getString(currentQ.getTextResId())));
    }

    public void clearResult() {
        mAnswerResult.setText("");
    }

    public void updateScore() {

        int colorRef;

        if(mQuizObj.getScore() == 0)
            colorRef = Color.BLACK;
        else if (mQuizObj.getScore() < 0)
            colorRef = Color.RED;
        else
            colorRef = Color.GREEN;

        mScoreDisplay.setText(String.format(getString(R.string.text_score), mQuizObj.getScore()));
        mScoreDisplay.setTextColor(colorRef);
    }

    public void displayResult(boolean wasCorrect) {

        int toastRef;
        int answerResponseColor;

        if(wasCorrect) {
            toastRef = R.string.text_correct;
            answerResponseColor = Color.GREEN;
        }
        else {
            toastRef = R.string.text_incorrect;
            answerResponseColor = Color.RED;
        }

        mAnswerResult.setText(toastRef);
        mAnswerResult.setTextColor(answerResponseColor);

        updateScore();

        pauseButtonInput();
    }

    protected void pauseButtonInput() {

        new CountDownTimer(3000, 1000) {
            public void onFinish() {

                clearResult();
                updateQuestionDisplay();
                disableButtons = false;
            }

            public void onTick(long millisUntilFinished) {}
        }.start();

        disableButtons = true;
    }
}
