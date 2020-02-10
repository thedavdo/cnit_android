package com.davdo.geoquiz;


import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Parcelable {

    private int mQuestionIndex;

    private int mScoreValue;

    private Question[] mQuestionList = {
        new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true)
    };

    private int[] mUserAnswers;

    public Quiz() {
        mQuestionIndex = 0;
        mScoreValue = 0;
    }

    private Quiz(Parcel in) {

        mQuestionIndex = in.readInt();
        mScoreValue = in.readInt();
        mQuestionList = (Question[]) in.readArray((Question[].class.getClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(mQuestionIndex);
        out.writeInt(mScoreValue);
        out.writeArray(mQuestionList);
    }


    public void startQuiz() {
        mUserAnswers = new int[mQuestionList.length];
        mQuestionIndex = 0;
        mScoreValue = 0;
    }

    public void setQuestion(int index) {

        if(index < 0) return;
        if(index > mQuestionList.length - 1) return;

        mQuestionIndex = index;
    }

    public int getScore() {
        return mScoreValue;
    }

    public int getQuestionIndex() {
        return mQuestionIndex;
    }

    public Question getCurrentQuestion() {
        return mQuestionList[mQuestionIndex];
    }

    public Question[] getQuestionList() {
        return mQuestionList;
    }

    public void progressQuestion() {
        progressQuestion(false);
    }

    public void progressQuestion(boolean backward) {

        if(backward)
            mQuestionIndex--;
        else
            mQuestionIndex++;

        if(mQuestionIndex < 0)
            mQuestionIndex = mQuestionList.length - 1;

        if(mQuestionIndex >= mQuestionList.length)
            mQuestionIndex = 0;

        setQuestion(mQuestionIndex);
    }

    public boolean isCurrentQuestionAnswered() {

        return isQuestionAnswered(mQuestionIndex);
    }

    public boolean isQuestionAnswered(int index) {

        return mUserAnswers[index] != 0;
    }

    public int getUserAnswer(int index) {

        return mUserAnswers[index];
    }

    protected boolean selectAnswer(boolean answer) {

        boolean correct = (getCurrentQuestion().getCorrectAnswer() == answer);

        if(correct) {
            mUserAnswers[mQuestionIndex] = 1;

            if(mScoreValue < 0)
                mScoreValue = 0;
            else
                mScoreValue++;
        }
        else {
            mUserAnswers[mQuestionIndex] = -1;
            if(mScoreValue <= 0)
                mScoreValue--;
            else
                mScoreValue = 0;
        }

        progressQuestion();

        return correct;
    }


    public static final Parcelable.Creator<Quiz> CREATOR = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
}
