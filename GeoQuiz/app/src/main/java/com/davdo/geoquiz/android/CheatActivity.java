package com.davdo.geoquiz.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.davdo.geoquiz.R;
import com.davdo.geoquiz.src.Question;
import com.davdo.geoquiz.src.Quiz;

public class CheatActivity extends AppCompatActivity {

    private AlertDialog mConfirmCheat;

    private Quiz mQuizObj;

    private boolean displayAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mQuizObj = getIntent().getParcelableExtra(MainActivity.QUIZ_INDEX);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_cheat_inform)
                .setPositiveButton(R.string.dialog_cheat_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.dialog_cheat_deny, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
        });

        mConfirmCheat = builder.create();

        mConfirmCheat.show();
    }

    public void updateDisplay() {

    }
}
