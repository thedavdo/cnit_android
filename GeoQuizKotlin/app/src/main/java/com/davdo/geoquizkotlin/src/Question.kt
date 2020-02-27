package com.davdo.geoquizkotlin.src

import android.os.Parcel
import android.os.Parcelable

class Question : Parcelable {

    var textResId: Int = 0
    var correctAnswer: Boolean = false
    var userCheated: Boolean = false

    private var mUserAnswer: Int = 0

    constructor (resId: Int, answer: Boolean) {
        textResId = resId
        mUserAnswer = 0
        correctAnswer = answer
    }

    constructor(arrive: Parcel) {

        textResId = arrive.readInt()
        correctAnswer = arrive.readInt() != 0
        userCheated = arrive.readInt() != 0
        mUserAnswer = arrive.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeInt(textResId)
        dest.writeInt((if (correctAnswer) 1 else 0))
        dest.writeInt((if (userCheated) 1 else 0))
        dest.writeInt(mUserAnswer)
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

    //This will return false when the user has not answered.
    fun isUserCorrect(): Boolean {
        return (if (correctAnswer) 1 else -1) == mUserAnswer
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