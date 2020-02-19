package com.davdo.geoquiz.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.davdo.geoquiz.R;
import com.davdo.geoquiz.src.Question;
import com.davdo.geoquiz.src.Quiz;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";

    public static final String ANSWER_INDEX = "show_answer";

    private AlertDialog mConfirmCheat;

    private Question mQuestion;

    private TextView mQuestionDisplay;
    private TextView mAnswerResult;

    private boolean showAnswer;

    private Intent dataCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        dataCallback = new Intent();

        if(savedInstanceState != null) {

            mQuestion = savedInstanceState.getParcelable(MainActivity.QUESTION_INDEX);
            showAnswer = (savedInstanceState.getByte(ANSWER_INDEX) != 0);
            updateIntent();
        }
        else {
            mQuestion = getIntent().getParcelableExtra(MainActivity.QUESTION_INDEX);
        }

        mQuestionDisplay = findViewById(R.id.text_view_question);
        mAnswerResult = findViewById(R.id.text_view_answer);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_cheat_inform)
                .setPositiveButton(R.string.dialog_cheat_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showAnswer = true;

                        updateIntent();
                        updateDisplay();
                    }
                })
                .setNegativeButton(R.string.dialog_cheat_deny, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateIntent();
                        finish();
                    }
        })
        .setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!showAnswer) {
                    updateIntent();
                    finish();
                }
            }
        });

        mConfirmCheat = builder.create();

        if(showAnswer) {
            updateDisplay();
        }
        else {
            mConfirmCheat.show();
        }
    }

    public void updateIntent() {
        dataCallback.putExtra(ANSWER_INDEX, (byte) (showAnswer ? 1 : 0));
        setResult(RESULT_OK, dataCallback);
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putParcelable(MainActivity.QUESTION_INDEX, mQuestion);
        savedInstanceState.putByte(ANSWER_INDEX, (byte) (showAnswer ? 1 : 0));
    }

    public void updateDisplay() {

        mQuestionDisplay.setText(mQuestion.getTextResId());

        int answerID = R.string.button_false;
        int answerColor = Color.RED;

        if(mQuestion.getCorrectAnswer()) {
            answerID = R.string.button_true;
            answerColor = Color.GREEN;
        }

        mAnswerResult.setText(answerID);
        mAnswerResult.setTextColor(answerColor);
    }
}
