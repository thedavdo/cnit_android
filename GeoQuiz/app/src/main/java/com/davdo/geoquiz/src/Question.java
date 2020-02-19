package com.davdo.geoquiz.src;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private int mTextResId;
    private boolean mCorrectAnswer;

    public Question(int textResId, boolean answer) {
        mTextResId = textResId;
        mCorrectAnswer = answer;
    }

    protected Question(Parcel in) {
        mTextResId = in.readInt();
        mCorrectAnswer = (in.readByte() != 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTextResId);
        dest.writeByte((byte) (mCorrectAnswer ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
