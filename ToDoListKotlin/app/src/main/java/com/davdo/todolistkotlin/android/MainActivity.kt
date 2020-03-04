package com.davdo.todolistkotlin.android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.davdo.todolistkotlin.R
import java.util.*


class MainActivity : AppCompatActivity(), NoteListFragment.Callbacks {

	private var mActionBar: ActionBar? = null

	private var mNoteListFragment : NoteListFragment? = null
	private var mNoteFragment : NoteFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mActionBar = supportActionBar

		var frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)

		if(frag == null) {
			mNoteListFragment = NoteListFragment()
			supportFragmentManager.beginTransaction().add(R.id.fragment_container, mNoteListFragment!!).commit()
		}
	}

	override fun onNoteSelected(noteID: UUID) {
		openNoteFragment(noteID)
	}

	override fun onAddNoteSelected() {
		openNoteFragment(null)
	}


	override fun onBackPressed() {

		val prompted: Boolean? = mNoteFragment?.onBackPressed()

		if(prompted == false) {
			super.onBackPressed()
			setShowBackHome(false)
			supportFragmentManager.popBackStack()
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		if(item.itemId == android.R.id.home) {
			onBackPressed()
			return true
		}

		return super.onOptionsItemSelected(item)
	}

	private fun setShowBackHome(show: Boolean) {
		mActionBar?.setDisplayHomeAsUpEnabled(show)
		mActionBar?.setDisplayShowHomeEnabled(show)
	}


	private fun openNoteFragment(noteID: UUID?) {

		mNoteFragment = if(noteID != null) NoteFragment.newInstance(noteID)
		else NoteFragment()

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, mNoteFragment!!)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
			.addToBackStack(null)
			.commit()

		setShowBackHome(true)
	}

}
