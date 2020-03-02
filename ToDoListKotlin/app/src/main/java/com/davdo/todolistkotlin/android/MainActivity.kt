package com.davdo.todolistkotlin.android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.src.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val CreateNoteIndex = "save_note"

class MainActivity : AppCompatActivity() {

    private val mCreateRequestCode = 0

    private var addNewNoteButton : FloatingActionButton? = null

    private var mNoteList : NoteListFragment? = null

    private lateinit var mNewNoteDisplay: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNewNoteDisplay = Intent(this, EditNoteActivity::class.java)

        addNewNoteButton = findViewById(R.id.fab_add_note)

        addNewNoteButton?.setOnClickListener {

//            mNewNoteDisplay
            mNewNoteDisplay.putExtra(EditNoteIndex, Note())
            startActivityForResult(mNewNoteDisplay, mCreateRequestCode)
        }

        val frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_note_container)

//        mNoteList =

        if(frag == null) {
//            myFragment = NoteFragment()
            mNoteList = NoteListFragment()

            supportFragmentManager.beginTransaction().add(R.id.fragment_note_container, mNoteList!!).commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == mCreateRequestCode) {
            if (resultCode == Activity.RESULT_OK) {

                val save : Int? = data?.getIntExtra(SaveNoteIndex, EditNoteActivity.ActionNone)
                val resNote: Note? = data?.getParcelableExtra(EditNoteIndex)

//                Toast.makeText(this, "$save", Toast.LENGTH_SHORT).show()

                if(resNote != null) {

                    when {
                        (save == EditNoteActivity.ActionSave) -> {
                            mNoteList?.addNote(resNote)
                        }
                    }
                }



               // if(save == 1) {

//                        noteListViewModel.notes[resNote.uuid]?.title = resNote.title
//                        noteListViewModel.notes[resNote.uuid]?.date = resNote.date
//                        noteListViewModel.notes[resNote.uuid]?.done = resNote.done
//
//                        noteRecyclerView.adapter?.notifyDataSetChanged()

//                        mNoteList?.noteListViewModel!!.notes[resNote.uuid] = resNote

//                    }
//                }
            }
        }
    }
}
