package com.davdo.geoquizkotlin.src

import android.os.Parcel
import android.os.Parcelable
import com.davdo.geoquizkotlin.R as Res


class Quiz() : Parcelable {

    private var mQuestionList: MutableList<Question> = mutableListOf(
        Question(Res.string.question_australia, true),
        Question(Res.string.question_oceans, true),
        Question(Res.string.question_mideast, false),
        Question(Res.string.question_africa, false),
        Question(Res.string.question_americas, true),
        Question(Res.string.question_asia, true)
    )

    private var mQuestionIndex :Int = 0
    private var mScoreValue :Int = 0

    constructor(inData: Parcel) : this() {

        mQuestionIndex = inData.readInt()
        mScoreValue = inData.readInt()

        inData.readTypedList<Question>(mQuestionList, Question)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {

        out.writeInt(mQuestionIndex)
        out.writeInt(mScoreValue)

        //out.writeTypedArray(mQuestionList, 0)

        out.writeTypedList<Question>(mQuestionList)
    }

    fun startQuiz() {
        //val shuffleList = mutableListOf(mQuestionList)
        mQuestionList.shuffle()

        setQuestionList(mQuestionList)
    }

    fun resetQuiz() {
        startQuiz()
        for (q in mQuestionList) {
            q.resetAnswer()
        }
    }

    fun setQuestionList(questionList: MutableList<Question>) {

        if (questionList.isNotEmpty()) {
            mQuestionList = questionList
            mQuestionIndex = 0
            mScoreValue = 0
        }
    }

    fun setQuestion(index: Int) {
        if (index < 0) return
        if (index > mQuestionList.size - 1) return
        mQuestionIndex = index
    }

    fun getScore(): Int {
        return mScoreValue
    }

    fun getQuestionIndex(): Int {
        return mQuestionIndex
    }

    fun getCurrentQuestion(): Question {
        return mQuestionList[mQuestionIndex]
    }

    fun getQuestionList(): MutableList<Question> {
        return mQuestionList
    }

    fun progressQuestion() {
        progressQuestion(false)
    }

    fun progressQuestion(backward: Boolean) {
        if (backward)
            mQuestionIndex--
        else
            mQuestionIndex++

        if (mQuestionIndex < 0)
            mQuestionIndex = mQuestionList.size - 1

        if (mQuestionIndex >= mQuestionList.size)
            mQuestionIndex = 0
        setQuestion(mQuestionIndex)
    }

    fun selectAnswer(answer: Boolean): Boolean {

        getCurrentQuestion().setUserAnswer(answer)

        val correct = getCurrentQuestion().isUserCorrect()

        if (getCurrentQuestion().hasUserCheated()) {
            mScoreValue = mScoreValue - 10
        }
        else {
            if (correct)
                mScoreValue++
            else
                mScoreValue--
        }
        //progressQuestion();
        return correct
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quiz> {
        override fun createFromParcel(parcel: Parcel): Quiz {
            return Quiz(parcel)
        }

        override fun newArray(size: Int): Array<Quiz?> {
            return arrayOfNulls(size)
        }
    }
}