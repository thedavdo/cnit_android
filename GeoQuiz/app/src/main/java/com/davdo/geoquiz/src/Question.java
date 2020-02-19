package com.davdo.geoquiz.src;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private int mTextResId;
    private boolean mCorrectAnswer;

    private int mUserAnswer;
    private boolean mUserCheated;

    public Question(int textResId, boolean answer) {
        mTextResId = textResId;
        mCorrectAnswer = answer;
        mUserAnswer = 0;
    }

    protected Question(Parcel in) {
        mTextResId = in.readInt();
        mCorrectAnswer = (in.readByte() != 0);
        mUserAnswer = in.readInt();
        mUserCheated = (in.readByte() != 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTextResId);
        dest.writeByte((byte) (mCorrectAnswer ? 1 : 0));
        dest.writeInt(mUserAnswer);
        dest.writeByte((byte) (mUserCheated ? 1 : 0));
    }

    public void resetAnswer() {
        mUserAnswer = 0;
    }

    public void setUserAnswer(boolean inAnswer) {
        mUserAnswer = inAnswer ? 1 : -1;
    }

    public boolean getUserAnswer() {

        return mUserAnswer == 1;
    }

    public boolean hasUserAnswered() {
        return (mUserAnswer != 0);
    }

    public void setUserCheated(boolean cheated) {
        mUserCheated = cheated;
    }

    public boolean hasUserCheated() {
        return mUserCheated;
    }

    //This will return false when the user has not answered.
    public boolean isUserCorrect() {
      return (mCorrectAnswer ? 1 : -1) == mUserAnswer;
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



    @Override
    public int describeContents() {
        return 0;
    }

    // --- Static Methods ---

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
