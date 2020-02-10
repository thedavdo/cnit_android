package com.davdo.geoquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String QUIZ_INDEX = "quiz";

    private Button mTrueButton;
    private Button mFalseButton;

    private FloatingActionButton mSkipButton;
    private FloatingActionButton mBackButton;

    AlertDialog mConfirmReset;

    private TextView mQuestionDisplay;
    private TextView mAnswerResult;

    private TextView mScoreDisplay;

    private boolean disableButtons = false;
    private boolean disableAnswerButtons = false;

    private Quiz mQuizObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrueButton = findViewById(R.id.button_true);
        mFalseButton = findViewById(R.id.button_false);

        mSkipButton = findViewById(R.id.button_skip);
        mBackButton = findViewById(R.id.button_back);

        mQuestionDisplay = findViewById(R.id.text_view_question);
        mAnswerResult = findViewById(R.id.text_view_result);
        mScoreDisplay = findViewById(R.id.text_view_score);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_reset_inform)
            .setPositiveButton(R.string.dialog_reset_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    mQuizObj.startQuiz();
                    updateQuestionDisplay();
                }
            }).setNegativeButton(R.string.dialog_reset_deny, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });


        mConfirmReset = builder.create();

        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            if(disableButtons) return;
            if(disableAnswerButtons) return;

            onChoice(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            if(disableButtons) return;
            if(disableAnswerButtons) return;

            onChoice(false);
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            if(disableButtons) return;

            mQuizObj.progressQuestion();
            updateQuestionDisplay();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            if(disableButtons) return;

            mQuizObj.progressQuestion(true);
            updateQuestionDisplay();
            }
        });

        if (savedInstanceState != null){
            mQuizObj = savedInstanceState.getParcelable(QUIZ_INDEX);
            Log.d(TAG, "Loading Parcelable!!!");
        }
        else {
            mQuizObj = new Quiz();
            mQuizObj.startQuiz();
        }

        updateQuestionDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_button_reset:
                mConfirmReset.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putParcelable(QUIZ_INDEX, mQuizObj);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }


    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    public void onChoice(boolean choice) {

        boolean result = mQuizObj.selectAnswer(choice);
        displayResult(result);

        pauseButtonInput();
    }

    public void clearResult() {
        mAnswerResult.setText("");
    }

    public void updateQuestionDisplay() {

        int index = mQuizObj.getQuestionIndex();
        Question currentQ = mQuizObj.getCurrentQuestion();

        mQuestionDisplay.setText(String.format(getString(R.string.text_question), index+1, getString(currentQ.getTextResId())));

        if(mQuizObj.isCurrentQuestionAnswered()) {

            boolean wasCorrect = mQuizObj.getUserAnswer(mQuizObj.getQuestionIndex()) == 1;

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

            disableAnswerButtons = true;
        }
        else {
            mAnswerResult.setText("");
            disableAnswerButtons = false;
        }

        updateScore();
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

        new CountDownTimer(2000, 1000) {
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
