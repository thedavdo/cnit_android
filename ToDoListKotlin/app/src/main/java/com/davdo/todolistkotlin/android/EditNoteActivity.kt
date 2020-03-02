package com.davdo.todolistkotlin.android

import android.app.Activity
import android.content.Intent
import android.drm.DrmStore
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.src.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditNoteActivity : AppCompatActivity() {

    companion object {
        const val ActionRemove = 2
        const val ActionSave = 1
        const val ActionNone = 0
    }

    private lateinit var dataCallback: Intent
    private var mActionBar: ActionBar? = null
    private var myFragment: NoteFragment? = null

    private var finishButton : FloatingActionButton? = null

    private var mConfirmSave: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_note)

        dataCallback = Intent()

        mActionBar = supportActionBar

        mActionBar?.setDisplayHomeAsUpEnabled(true)
        mActionBar?.setDisplayShowHomeEnabled(true)

        finishButton = findViewById(R.id.fab_finish)

        finishButton?.setOnClickListener {
            updateIntent(ActionSave)
            finish()
        }

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(R.string.dialog_edit_prompt)
        dialogBuilder.setPositiveButton(R.string.dialog_edit_confirm) { _,_ ->

            updateIntent(ActionSave)
            finish()
        }
        dialogBuilder.setNegativeButton(R.string.dialog_edit_discard) { _,_ ->

            updateIntent(ActionNone)
            finish()
        }
        dialogBuilder.setNeutralButton(R.string.dialog_edit_cancel) { _,_ -> }

        mConfirmSave = dialogBuilder.create()

        myFragment = supportFragmentManager.findFragmentById(R.id.fragment_note_container) as NoteFragment?

        val myNote : Note? = intent.getParcelableExtra(EditNoteIndex)

        if(myFragment == null) {

            myFragment = NoteFragment()

            if(myNote != null) {
                val testBundle = Bundle()
                testBundle.putParcelable(EditNoteIndex, myNote)
                myFragment?.arguments = testBundle
            }

            supportFragmentManager.beginTransaction().add(R.id.fragment_note_container, myFragment!!).commit()
        }
    }

    private fun updateIntent(action : Int) {

        dataCallback.putExtra(SaveNoteIndex, action)
        dataCallback.putExtra(EditNoteIndex, myFragment?.mNote)
        setResult(Activity.RESULT_OK, dataCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


       // return super.onOptionsItemSelected(item)
        return when(item.itemId) {
            android.R.id.home -> {
                if(myFragment?.mChangesMade == true) mConfirmSave?.show()
                else finish()
                true
            }
            R.id.menu_button_delete_note -> {
                updateIntent(ActionRemove)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }
}