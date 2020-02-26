package com.davdo.geoquizkotlin.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davdo.geoquizkotlin.R
import com.davdo.geoquizkotlin.src.Question
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import android.app.Activity
import android.graphics.Color


const val ANSWER_INDEX = "show_answer"

class CheatActivity : AppCompatActivity() {

    private val mLogTag = "CheatActivity"

    private var mActionBar: ActionBar? = null
    private var mConfirmCheat: AlertDialog? = null
    private var mQuestionDisplay: TextView? = null
    private var mAnswerResult: TextView? = null
    private var dataCallback: Intent? = null

    private var mQuestion: Question? = null
    private var showAnswer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        mQuestionDisplay = findViewById(R.id.text_view_question)
        mAnswerResult = findViewById(R.id.text_view_answer)

        mActionBar = supportActionBar

        mActionBar?.setDisplayHomeAsUpEnabled(true)
        mActionBar?.setDisplayShowHomeEnabled(true)

        dataCallback = Intent()

        if (savedInstanceState != null) {
            mQuestion = savedInstanceState.getParcelable(QUESTION_INDEX)
            showAnswer = savedInstanceState.getByte(ANSWER_INDEX).toInt() != 0

            updateIntent()
        }
        else {
            mQuestion = intent.getParcelableExtra(QUESTION_INDEX)
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.dialog_cheat_inform)
        builder.setPositiveButton(R.string.dialog_cheat_confirm) { dialog, id ->

            showAnswer = true
            updateIntent()
            updateDisplay()
        }
        builder.setNegativeButton(R.string.dialog_cheat_deny) { dialog, id ->
            updateIntent()
            finish()
        }
        builder.setOnDismissListener {
            if (!showAnswer) {
                updateIntent()
                finish()
            }
        }

        mConfirmCheat = builder.create()

        if (showAnswer) {
            updateDisplay()
        }
        else {
            mConfirmCheat?.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(mLogTag, "onSaveInstanceState")

        savedInstanceState.putParcelable(QUESTION_INDEX, mQuestion)
        savedInstanceState.putByte(ANSWER_INDEX, (if (showAnswer) 1 else 0).toByte())
    }

    private fun updateIntent() {

        dataCallback?.putExtra(ANSWER_INDEX, (if (showAnswer) 1 else 0).toByte())
        setResult(Activity.RESULT_OK, dataCallback)
    }

    private fun updateDisplay() {

        mQuestionDisplay?.setText(mQuestion!!.getTextResId())

        var answerID = R.string.button_false
        var answerColor = Color.RED

        if (mQuestion!!.getCorrectAnswer()) {
            answerID = R.string.button_true
            answerColor = Color.GREEN
        }

        mAnswerResult?.setText(answerID)
        mAnswerResult?.setTextColor(answerColor)
    }
}
