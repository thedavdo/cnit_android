package com.davdo.todolistkotlin.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.davdo.todolistkotlin.R
import java.util.*

class MainActivity : AppCompatActivity(), NoteListFragment.Callbacks {

	private var mActionBar: ActionBar? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mActionBar = supportActionBar

		var frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)

		if(frag == null) {
			frag = NoteListFragment()
			supportFragmentManager.beginTransaction().add(R.id.fragment_container, frag).commit()
		}
	}

	override fun onNoteSelected(noteID: UUID) {
		openNoteFragment(noteID)
	}

	override fun onAddNoteSelected() {
		openNoteFragment(null)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		if(item.itemId == android.R.id.home) {
			mActionBar?.setDisplayHomeAsUpEnabled(false)
			mActionBar?.setDisplayShowHomeEnabled(false)
			supportFragmentManager.popBackStack()
			return true
		}

		return super.onOptionsItemSelected(item)
	}

	private fun openNoteFragment(noteID: UUID?) {

		val frag : NoteFragment = if(noteID != null) NoteFragment.newInstance(noteID)
		else NoteFragment()

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, frag)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
			.addToBackStack(null)
			.commit()

		mActionBar?.setDisplayHomeAsUpEnabled(true)
		mActionBar?.setDisplayShowHomeEnabled(true)
	}

}
