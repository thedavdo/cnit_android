package com.davdo.geoquizkotlin.android

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.davdo.geoquizkotlin.R
import com.davdo.geoquizkotlin.src.Quiz
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.content.res.ResourcesCompat
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem

const val QUESTION_INDEX = "question_obj"

class MainActivity : AppCompatActivity() {

    private val mCheatRequestCode = 0

    private val mLogTag = "MainActivity"
    private val mQuizIndex = "quiz_obj"

    private var mTrueButton: Button? = null
    private var mFalseButton: Button? = null

    private var mSkipButton: FloatingActionButton? = null
    private var mBackButton: FloatingActionButton? = null

    private var mConfirmReset: AlertDialog? = null

    private var mQuestionDisplay: TextView? = null
    private var mAnswerResult: TextView? = null

    private var mScoreDisplay: TextView? = null

    private var disableButtons: Boolean = false
    private var disableAnswerButtons: Boolean = false

    private lateinit var mQuizObj: Quiz

    private lateinit var mCheatDisplay: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTrueButton = findViewById(R.id.button_true)
        mFalseButton = findViewById(R.id.button_false)

        mSkipButton = findViewById(R.id.button_skip)
        mBackButton = findViewById(R.id.button_back)

        mQuestionDisplay = findViewById(R.id.text_view_question)
        mAnswerResult = findViewById(R.id.text_view_result)
        mScoreDisplay = findViewById(R.id.text_view_score)

        mCheatDisplay = Intent(this@MainActivity, CheatActivity::class.java)

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(R.string.dialog_reset_inform)
        dialogBuilder.setPositiveButton(R.string.dialog_reset_confirm) { _,_ ->
            mQuizObj.resetQuiz()
            updateQuestionDisplay()
        }
        dialogBuilder.setNegativeButton(R.string.dialog_reset_deny) { _,_ -> }

        mConfirmReset = dialogBuilder.create()

        mTrueButton?.setOnClickListener {

            if(disableButtons) return@setOnClickListener
            if(disableAnswerButtons) return@setOnClickListener

            onChoice(true)
        }

        mFalseButton?.setOnClickListener {

            if(disableButtons) return@setOnClickListener
            if(disableAnswerButtons) return@setOnClickListener

            onChoice(false)
        }

        mSkipButton?.setOnClickListener {

            if(disableButtons) return@setOnClickListener

            mQuizObj.progressQuestion()
            updateQuestionDisplay()
        }

        mBackButton?.setOnClickListener {

            if(disableButtons) return@setOnClickListener

            mQuizObj.progressQuestion(true)
            updateQuestionDisplay()
        }


        if (savedInstanceState != null) {

            val bundleQuiz: Quiz? = savedInstanceState.getParcelable(mQuizIndex)

            if(bundleQuiz != null) mQuizObj = bundleQuiz

            Log.d(mLogTag, "Loading Parcelable!!!")
        }
        else {
            mQuizObj = Quiz()
            mQuizObj.startQuiz()
        }

        updateQuestionDisplay()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.menu_button_reset -> {
                mConfirmReset?.show()

                true
            }
            R.id.menu_button_cheat -> {

                mCheatDisplay.putExtra(QUESTION_INDEX, mQuizObj.getCurrentQuestion())
                startActivityForResult(mCheatDisplay, mCheatRequestCode)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == mCheatRequestCode) {
            if (resultCode == Activity.RESULT_OK) {

                val test: Byte? = data?.getByteExtra(ANSWER_INDEX, (-1).toByte())

                if (test?.toInt() == 1) {
                    mQuizObj.getCurrentQuestion().setUserCheated(true)
                }
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(mLogTag, "onSaveInstanceState")

        savedInstanceState.putParcelable(mQuizIndex, mQuizObj)
    }

    override fun onStart() {
        super.onStart()
        Log.d(mLogTag, "onStart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(mLogTag, "onPause() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(mLogTag, "onResume() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(mLogTag, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(mLogTag, "onDestroy() called")
    }

    private fun onChoice(choice: Boolean) {

        if(mQuizObj.getCurrentQuestion().hasUserCheated()) {
            Toast.makeText(applicationContext, "Cheating is Wrong.", Toast.LENGTH_SHORT).show()
        }

        mQuizObj.selectAnswer(choice)
        displayResult()

        //Log.d(mLogTag, "User Cheat Status: " + mQuizObj.getCurrentQuestion().hasUserCheated());

        pauseButtonInput()
    }

    private fun updateQuestionDisplay() {

        val index = mQuizObj.getQuestionIndex()
        val currentQ = mQuizObj.getCurrentQuestion()

        mQuestionDisplay?.text = String.format(
            getString(R.string.text_question),
            index + 1,
            getString(currentQ.getTextResId())
        )

        updateAnswerDisplay()
        updateScore()
    }

    private fun updateAnswerDisplay() {

        val currentQ = mQuizObj.getCurrentQuestion()

        if (currentQ.hasUserAnswered()) {

            val resultID: Int
            val answerResponseColor: Int

            if (currentQ.hasUserCheated()) {
                resultID = R.string.text_cheated
                answerResponseColor = Color.RED
            }
            else {
                if (currentQ.isUserCorrect()) {
                    resultID = R.string.text_correct
                    answerResponseColor = Color.GREEN
                }
                else {
                    resultID = R.string.text_incorrect
                    answerResponseColor = ResourcesCompat.getColor(
                        resources,
                        R.color.base_wrong,
                        null)
                }
            }

            val correctAnswer = if (currentQ.getCorrectAnswer())
                R.string.button_true
            else
                R.string.button_false

            val result = String.format(
                getString(R.string.text_result),
                getString(resultID),
                getString(correctAnswer)
            )

            mAnswerResult?.text = result
            mAnswerResult?.setTextColor(answerResponseColor)

            disableAnswerButtons = true
        }
        else {
            mAnswerResult?.text = ""
            disableAnswerButtons = false
        }
    }

    private fun updateScore() {

        val colorRef: Int = when {
            (mQuizObj.getScore() == 0) -> Color.BLACK
            (mQuizObj.getScore() < 0) -> ResourcesCompat.getColor(resources, R.color.base_wrong, null)
            else -> Color.GREEN
        }

        mScoreDisplay?.text = String.format(getString(R.string.text_score), mQuizObj.getScore())
        mScoreDisplay?.setTextColor(colorRef)
    }

    private fun clearResult() {
        mAnswerResult?.text = ""
    }

    private fun displayResult() {

        updateAnswerDisplay()
        updateScore()
    }

    private fun pauseButtonInput() {

        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {

                mQuizObj.progressQuestion()
                clearResult()
                updateQuestionDisplay()

                disableButtons = false
            }

            override fun onTick(millisUntilFinished: Long) {}
        }.start()

        disableButtons = true
    }
}
