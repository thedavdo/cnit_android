package com.davdo.todolistkotlin.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.davdo.todolistkotlin.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), NoteListFragment.Callbacks {

	private var addNewNoteButton : FloatingActionButton? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		addNewNoteButton = findViewById(R.id.fab_add_note)

		addNewNoteButton?.setOnClickListener {

			val frag = NoteFragment()

			supportFragmentManager.beginTransaction()
				.replace(R.id.fragment_container, frag)
				.addToBackStack(null)
				.commit()
		}

		var frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)

		if(frag == null) {
			frag = NoteListFragment()
			supportFragmentManager.beginTransaction().add(R.id.fragment_container, frag).commit()
		}
	}

	override fun onNoteSelected(noteID: UUID) {

		val fragment = NoteFragment.newInstance(noteID)

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.addToBackStack(null)
			.commit()
	}

//	private fun showEdit

//	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//		super.onActivityResult(requestCode, resultCode, data)
//
//		if (requestCode == mCreateRequestCode) {
//			if (resultCode == Activity.RESULT_OK) {
//
////				val save : Int? = data?.getIntExtra(SaveNoteIndex, EditNoteActivity.ActionNone)
////				val resNote: Note? = data?.getParcelableExtra(EditNoteIndex)
//
////				if(resNote != null) {
////
////					when {
////						(save == EditNoteActivity.ActionSave) -> {
////
////						}
////					}
////				}
//			}
//		}
//	}
}
