package com.davdo.geoquizkotlin.src

import android.os.Parcel
import android.os.Parcelable

class Question() : Parcelable {

    private var mTextResId: Int = 0
    private var mCorrectAnswer: Boolean = false

    private var mUserAnswer: Int = 0
    private var mUserCheated: Boolean = false

    constructor (textResId: Int, answer: Boolean) : this() {
        mTextResId = textResId
        mCorrectAnswer = answer
        mUserAnswer = 0
    }

    constructor(arrive: Parcel) : this() {

        mTextResId = arrive.readInt()
        mCorrectAnswer = arrive.readInt() != 0
        mUserAnswer = arrive.readInt()
        mUserCheated = arrive.readInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeInt(mTextResId)
        dest.writeInt((if (mCorrectAnswer) 1 else 0))
        dest.writeInt(mUserAnswer)
        dest.writeInt((if (mUserCheated) 1 else 0))
    }

    fun resetAnswer()  {
        mUserAnswer = 0
    }

    fun setUserAnswer(inAnswer: Boolean) {
        mUserAnswer = if (inAnswer) 1 else -1
    }

    fun getUserAnswer(): Boolean {
        return mUserAnswer == 1
    }

    fun hasUserAnswered(): Boolean {
        return mUserAnswer != 0
    }

    fun setUserCheated(cheated: Boolean) {
        mUserCheated = cheated
    }

    fun hasUserCheated(): Boolean {
        return mUserCheated
    }

    //This will return false when the user has not answered.
    fun isUserCorrect(): Boolean {
        return (if (mCorrectAnswer) 1 else -1) == mUserAnswer
    }

    fun getTextResId(): Int {
        return mTextResId
    }

    fun getCorrectAnswer(): Boolean {
        return mCorrectAnswer
    }

    fun setTextResId(textResId: Int) {
        mTextResId = textResId
    }

    fun setCorrectAnswer(answerTrue: Boolean) {
        mCorrectAnswer = answerTrue
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}