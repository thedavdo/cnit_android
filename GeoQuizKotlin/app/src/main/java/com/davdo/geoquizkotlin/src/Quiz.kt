package com.davdo.geoquizkotlin.src

import android.os.Parcel
import android.os.Parcelable
import com.davdo.geoquizkotlin.R as Res


class Quiz() : Parcelable {

    var questionList: MutableList<Question> = mutableListOf(
        Question(Res.string.question_australia, true),
        Question(Res.string.question_oceans, true),
        Question(Res.string.question_mideast, false),
        Question(Res.string.question_africa, false),
        Question(Res.string.question_americas, true),
        Question(Res.string.question_asia, true)
    )
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                score = 0
                questionIndex = 0
            }
        }

    var currentQuestion: Question
        get() {
            return questionList[questionIndex]
        }
        set(value) {
            val tempIndex = questionList.indexOf(value)
            if (tempIndex != -1)
                questionIndex = tempIndex
        }

    var questionIndex :Int = 0
        set(value) {
            field = when {
                (value >= 0 && value <= questionList.size - 1) -> value
                else -> field
            }
        }

    var score :Int = 0
        private set

    init {
        questionIndex = 0
        score = 0
    }

    constructor(questionList: MutableList<Question>): this() {

        this.questionList = questionList
    }

    private constructor(inData: Parcel) : this() {

        questionIndex = inData.readInt()
        score = inData.readInt()

        inData.readTypedList<Question>(questionList, Question)
    }

    override fun writeToParcel(out: Parcel, flags: Int) {

        out.writeInt(questionIndex)
        out.writeInt(score)

        //out.writeTypedArray(mQuestionList, 0)

        out.writeTypedList<Question>(questionList)
    }

    fun startQuiz() {
        questionIndex = 0
        score = 0
        questionList.shuffle()
    }

    fun resetQuiz() {
        startQuiz()
        for (q in questionList) {
            q.resetAnswer()
        }
    }


    fun progressQuestion() {
        progressQuestion(false)
    }

    fun progressQuestion(backward: Boolean) {

        if (backward) {
            if(questionIndex - 1 < 0)
                questionIndex = questionList.size - 1
            else
                questionIndex--
        }
        else {
            if(questionIndex + 1 > questionList.size - 1)
                questionIndex = 0
            else
                questionIndex++
        }
    }

    fun selectAnswer(answer: Boolean): Boolean {

        currentQuestion.setUserAnswer(answer)

        val correct = currentQuestion.isUserCorrect()

        if (currentQuestion.userCheated) {
            score -= 10
        }
        else {
            if (correct) score++
            else score--
        }

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