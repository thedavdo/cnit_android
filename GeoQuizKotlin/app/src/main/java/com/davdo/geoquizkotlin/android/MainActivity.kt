package com.davdo.geoquizkotlin.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.davdo.geoquizkotlin.R
import com.davdo.geoquizkotlin.src.Quiz


class MainActivity : AppCompatActivity() {

    private val CHEAT_REQUEST_CODE = 0

    private val TAG = "MainActivity"
    val QUIZ_INDEX = "quiz_obj"
    val QUESTION_INDEX = "question_obj"

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private var disableButtons: Boolean = false
    private var disableAnswerButtons: Boolean = false

    private lateinit var mQuizObj: Quiz

    //private lateinit val mCheatDisplay: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.button_true)
        falseButton = findViewById(R.id.button_false)

        trueButton.setOnClickListener { view: View ->

            if(disableButtons) return@setOnClickListener
            if(disableAnswerButtons) return@setOnClickListener

            onChoice(true)
        }

        falseButton.setOnClickListener { view: View ->

            if(disableButtons) return@setOnClickListener
            if(disableAnswerButtons) return@setOnClickListener

            onChoice(false)
        }


        if (savedInstanceState != null) {
            mQuizObj = savedInstanceState.getParcelable<Quiz>(QUIZ_INDEX)!!
            Log.d(TAG, "Loading Parcelable!!!")
        } else {
            mQuizObj = Quiz()
            mQuizObj.startQuiz()
        }
    }

    fun onChoice(choice: Boolean) {

        Toast.makeText(this, "" + choice, Toast.LENGTH_SHORT).show()
    }
}
