package com.davdo.todolistkotlin.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.db.Note


class NoteViewPagerFragment : Fragment(), BackPressedListener {

	private lateinit var viewPager: ViewPager2
	private lateinit var noteListViewModel: NoteListViewModel

	private var startPos = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)

		val argIndex = arguments?.getSerializable(ARG_NOTE_INDEX)

		if(argIndex != null) {
			startPos = argIndex as Int
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		val v: View = inflater.inflate(R.layout.fragment_note_view_pager, container, false)

		viewPager = v.findViewById(R.id.viewpager2)

		viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
		})

		return v
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		noteListViewModel.noteListLiveData.observe(viewLifecycleOwner,
			Observer { notes ->
				notes.let {
					updateUI(notes)
				}
			})
	}


	override fun onBackPressed(): Boolean {

		return false
	}

	private fun updateUI(notes: List<Note>) {

		val pagerAdapter = ScreenSlidePagerAdapter(notes,this)
		viewPager.adapter = pagerAdapter

		if(startPos != -1) {
			viewPager.setCurrentItem(startPos, false)
		}
	}

	private inner class ScreenSlidePagerAdapter(var notes: List<Note>, fr: Fragment) : FragmentStateAdapter(fr) {

		override fun createFragment(position: Int): Fragment {

			val note : Note? = notes[position]

			val fragment = if(note != null) NoteFragment.newInstance(note.uuid)
			else NoteFragment()

			return fragment
		}

		override fun getItemCount(): Int = notes.size
	}

	companion object {

		private const val ARG_NOTE_INDEX: String = "note_index"

		fun newInstance(index: Int): NoteViewPagerFragment {

			val args = Bundle().apply { putSerializable(ARG_NOTE_INDEX, index) }
			return NoteViewPagerFragment().apply { arguments = args }
		}
	}
}