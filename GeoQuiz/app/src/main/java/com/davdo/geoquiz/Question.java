package com.davdo.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswer;

    public Question(int textResId, boolean answer){
        mTextResId = textResId;
        mAnswer = answer;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean getAnswer() {
        return mAnswer;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswer(boolean answerTrue) {
        mAnswer = answerTrue;
    }
}
