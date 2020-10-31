package com.davdo.geoquiz.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davdo.geoquiz.src.Question;
import com.davdo.geoquiz.src.Quiz;
import com.davdo.geoquiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int CHEAT_REQUEST_CODE = 0;

    public static final String QUESTION_INDEX = "question_obj";

    private final String TAG = "MainActivity";
    public final String QUIZ_INDEX = "quiz_obj";

    private Button mTrueButton;
    private Button mFalseButton;

    private FloatingActionButton mSkipButton;
    private FloatingActionButton mBackButton;

    private AlertDialog mConfirmReset;

    private TextView mQuestionDisplay;
    private TextView mAnswerResult;

    private TextView mScoreDisplay;

    private boolean disableButtons = false;
    private boolean disableAnswerButtons = false;

    private Quiz mQuizObj;

    private Intent mCheatDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        NavigationUI.setupWithNavController(toolbar, navController);

        mTrueButton = findViewById(R.id.button_true);
        mFalseButton = findViewById(R.id.button_false);

        mSkipButton = findViewById(R.id.button_skip);
        mBackButton = findViewById(R.id.button_back);

        mQuestionDisplay = findViewById(R.id.text_view_question);
        mAnswerResult = findViewById(R.id.text_view_result);
        mScoreDisplay = findViewById(R.id.text_view_score);

        mCheatDisplay = new Intent(MainActivity.this, CheatActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_reset_inform)
            .setPositiveButton(R.string.dialog_reset_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    mQuizObj.resetQuiz();
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
            case R.id.menu_button_cheat:

                mCheatDisplay.putExtra(QUESTION_INDEX, mQuizObj.getCurrentQuestion());
                startActivityForResult(mCheatDisplay, CHEAT_REQUEST_CODE);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHEAT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                byte test = data.getByteExtra(CheatActivity.ANSWER_INDEX, (byte) -1);
                if(test == 1) {
                    mQuizObj.getCurrentQuestion().setUserCheated(true);
                }
            }
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

        if(mQuizObj.getCurrentQuestion().hasUserCheated()) {
            Toast.makeText(getApplicationContext(), "Cheating is Wrong.", Toast.LENGTH_SHORT).show();
        }

        mQuizObj.selectAnswer(choice);
        displayResult();

        //Log.d(TAG, "User Cheat Status: " + mQuizObj.getCurrentQuestion().hasUserCheated());

        pauseButtonInput();
    }

    public void updateQuestionDisplay() {

        int index = mQuizObj.getQuestionIndex();
        Question currentQ = mQuizObj.getCurrentQuestion();

        mQuestionDisplay.setText(String.format(getString(R.string.text_question), index+1, getString(currentQ.getTextResId())));

        updateAnswerDisplay();
        updateScore();
    }

    public void updateAnswerDisplay() {

        Question currentQ = mQuizObj.getCurrentQuestion();

        if(currentQ.hasUserAnswered()) {

            int resultID;
            int answerResponseColor;

            if(currentQ.hasUserCheated()) {
                resultID = R.string.text_cheated;
                answerResponseColor = Color.RED;
            }
            else {
                if(currentQ.isUserCorrect()) {
                    resultID = R.string.text_correct;
                    answerResponseColor = Color.GREEN;
                }
                else {
                    resultID = R.string.text_incorrect;
                    answerResponseColor = ResourcesCompat.getColor(getResources(), R.color.base_wrong, null);
                }
            }

            int correctAnswer = currentQ.getCorrectAnswer() ? R.string.button_true : R.string.button_false;

            String result = String.format(getString(R.string.text_result), getString(resultID), getString(correctAnswer));

            mAnswerResult.setText(result);
            mAnswerResult.setTextColor(answerResponseColor);

            disableAnswerButtons = true;
        }
        else {
            mAnswerResult.setText("");
            disableAnswerButtons = false;
        }
    }

    public void updateScore() {

        int colorRef;

        if(mQuizObj.getScore() == 0)
            colorRef = Color.BLACK;
        else if(mQuizObj.getScore() < 0)
            colorRef = ResourcesCompat.getColor(getResources(), R.color.base_wrong, null);
        else
            colorRef = Color.GREEN;

        mScoreDisplay.setText(String.format(getString(R.string.text_score), mQuizObj.getScore()));
        mScoreDisplay.setTextColor(colorRef);
    }

    public void clearResult() {
        mAnswerResult.setText("");
    }

    public void displayResult() {

        updateAnswerDisplay();
        updateScore();
    }

    protected void pauseButtonInput() {

        new CountDownTimer(2000, 1000) {
            public void onFinish() {

                mQuizObj.progressQuestion();
                clearResult();
                updateQuestionDisplay();

                disableButtons = false;
            }

            public void onTick(long millisUntilFinished) {}
        }.start();

        disableButtons = true;
    }
}
