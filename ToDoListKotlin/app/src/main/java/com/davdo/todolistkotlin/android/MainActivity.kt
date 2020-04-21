package com.davdo.todolistkotlin.android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.davdo.todolistkotlin.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity(), NoteListFragment.Callbacks {

	private var mAppBar: AppBarLayout? = null
	private var mToolbar: Toolbar? = null
	private var mActionbar : ActionBar? = null

	private var floatingActionButton : FloatingActionButton? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mAppBar = findViewById(R.id.appbar)
		mToolbar = findViewById(R.id.toolbar)

		setSupportActionBar(mToolbar)
		mActionbar = supportActionBar

		floatingActionButton = findViewById(R.id.fab_activity_main)

		floatingActionButton?.setOnClickListener {
			openNewNoteFragment()
		}

		var frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)

		if(frag == null) {
			frag = NoteListFragment()
			supportFragmentManager.beginTransaction().add(R.id.fragment_container, frag).commit()
		}
	}

	override fun onNoteSelected(noteID: UUID, position: Int) {
		openNoteFragment(position)
//		openNoteFragment(noteID)
	}

	override fun onBackPressed() {

		val frag : Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_container)
		var interrupt: Boolean? = false

		if(frag is BackPressedListener) {
			interrupt = (frag as BackPressedListener).onBackPressed()
		}

		if(interrupt != true) {
			super.onBackPressed()
			setShowBackHome(false)
			floatingActionButton?.show()
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
		mActionbar?.setDisplayHomeAsUpEnabled(show)
		mActionbar?.setDisplayShowHomeEnabled(show)
	}

	private fun openNewNoteFragment() {
		val frag = NoteFragment()
		openFragment(frag)
	}

	private fun openNoteFragment(position: Int) {

		val frag = NoteViewPagerFragment.newInstance(position)
		openFragment(frag)
	}

	private fun openNoteFragment(noteID: UUID) {
		val frag = NoteFragment.newInstance(noteID)
		openFragment(frag)
	}

	private fun openFragment(frag: Fragment) {
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, frag)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
			.addToBackStack(null)
			.commit()

		setShowBackHome(true)
		mAppBar?.setExpanded(true, true)
		floatingActionButton?.hide()
	}
}
