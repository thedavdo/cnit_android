package com.davdo.geoquiz.src;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.davdo.geoquiz.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Quiz implements Parcelable {

    private Question[] mQuestionList = {
        new Question(R.string.question_australia, true),
        new Question(R.string.question_oceans, true),
        new Question(R.string.question_mideast, false),
        new Question(R.string.question_africa, false),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia, true)
    };

    private int mQuestionIndex;
    private int mScoreValue;

    public Quiz() {
        mQuestionIndex = 0;
        mScoreValue = 0;
    }

    public Quiz(Question[] questionList) {

        setQuestionList(questionList);
    }

    private Quiz(Parcel in) {

        mQuestionIndex = in.readInt();
        mScoreValue = in.readInt();

        in.readTypedArray(mQuestionList, Question.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(mQuestionIndex);
        out.writeInt(mScoreValue);

        out.writeTypedArray(mQuestionList, 0);
    }


    public void startQuiz() {

        List<Question> shuffleList = Arrays.asList(mQuestionList);

        Collections.shuffle(shuffleList);

        setQuestionList((Question[]) shuffleList.toArray());
    }

    public void resetQuiz() {

        startQuiz();

        for(Question q : mQuestionList) {
            q.resetAnswer();
        }
    }

    public void setQuestionList(Question[] questionList) {

        if(questionList.length > 0) {
            mQuestionList = questionList;

            mQuestionIndex = 0;
            mScoreValue = 0;
        }
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

    public boolean selectAnswer(boolean answer) {

        getCurrentQuestion().setUserAnswer(answer);

        boolean correct = getCurrentQuestion().isUserCorrect();

        if(getCurrentQuestion().hasUserCheated()) {
            mScoreValue = mScoreValue - 10;
        }
        else {
            if(correct)
                mScoreValue++;
            else
                mScoreValue--;
        }

        //progressQuestion();

        return correct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // --- Static Methods ---

    public static final Parcelable.Creator<Quiz> CREATOR = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
}
