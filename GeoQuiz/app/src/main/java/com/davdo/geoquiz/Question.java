package com.davdo.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mCorrectAnswer;

    public Question(int textResId, boolean answer) {
        mTextResId = textResId;
        mCorrectAnswer = answer;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setCorrectAnswer(boolean answerTrue) {
        mCorrectAnswer = answerTrue;
    }
}
