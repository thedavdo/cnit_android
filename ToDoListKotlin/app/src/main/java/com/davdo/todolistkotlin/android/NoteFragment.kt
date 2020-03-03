package com.davdo.todolistkotlin.android


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.src.Note
import androidx.lifecycle.Observer
import java.util.*


class NoteFragment : Fragment() {

	private lateinit var noteListViewModel: NoteListViewModel
	private lateinit var noteDetailViewModel: NoteDetailViewModel

	private var mTitleField: EditText? = null
	private var mDateButton: Button? = null
	private var mDoneCheckbox: CheckBox? = null

	private lateinit var cal: Calendar

	private var mNote: Note? = null

	private var mChangesMade: Boolean = false
	private var mNewNote: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
		noteDetailViewModel = ViewModelProvider(this).get(NoteDetailViewModel::class.java)

		cal = Calendar.getInstance()

		val argID = arguments?.getSerializable(ARG_NOTE_ID)

		if(argID != null) {
			val noteID: UUID = argID as UUID
			noteDetailViewModel.loadNote(noteID)
		}
		else {
			mNote = Note()
			mNewNote = true
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		noteDetailViewModel.noteLiveData.observe(viewLifecycleOwner, Observer { note ->

			note?.let {
				this.mNote = note
				updateUI()
			}
		})
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

	   val v = inflater.inflate(R.layout.fragment_note, container, false)

		mTitleField = v.findViewById(R.id.edit_text_title)
		mDateButton = v.findViewById(R.id.button_date)
		mDoneCheckbox = v.findViewById(R.id.checkbox_finished)

		mTitleField?.setText(mNote?.title)
		mDateButton?.text = (mNote?.date.toString())

		mDoneCheckbox?.isChecked = mNote?.done ?: false


		mTitleField?.addTextChangedListener(object : TextWatcher {
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				mNote?.title = s.toString()
				mChangesMade = true
			}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
			override fun afterTextChanged(s: Editable) {}
		})

		mTitleField?.setOnEditorActionListener { _, actionId, _ ->

			if (actionId == EditorInfo.IME_ACTION_DONE) {
				mTitleField?.clearFocus()
			}
			false
		}

		mDoneCheckbox?.setOnCheckedChangeListener { _, isChecked ->
			mNote?.done = isChecked
			mChangesMade = true
		}

		val dateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
			cal[Calendar.YEAR] = year
			cal[Calendar.MONTH] = monthOfYear
			cal[Calendar.DAY_OF_MONTH] = dayOfMonth

			mNote?.date?.time = cal.time.time
			mDateButton?.text = mNote?.date.toString()
			mChangesMade = true
		}

		mDateButton?.setOnClickListener {

			cal.time = mNote?.date ?: Date()

			val dialog = DatePickerDialog(
				context!!,
				dateListener,
				cal[Calendar.YEAR],
				cal[Calendar.MONTH],
				cal[Calendar.DAY_OF_MONTH]
			)

			dialog.show()
		}

		return v
	}

	override fun onStart() {
		super.onStart()

	}

	override fun onStop() {
		super.onStop()

		if(mNote != null) {
			if(mNewNote) {
				noteListViewModel.addNote(mNote!!)
			}
			else {
				noteDetailViewModel.updateNote(mNote!!)
			}
		}
	}

	private fun updateUI() {
		mTitleField?.setText(mNote?.title)
		mTitleField?.jumpDrawablesToCurrentState()
		mDateButton?.text = mNote?.date.toString()
		mDoneCheckbox?.isChecked = mNote?.done ?: false
		mDoneCheckbox?.jumpDrawablesToCurrentState()
	}

	companion object {

		private const val ARG_NOTE_ID: String = "note_id"

		fun newInstance(noteID: UUID): NoteFragment {

			val args = Bundle().apply { putSerializable(ARG_NOTE_ID, noteID) }

			return NoteFragment().apply { arguments = args }
		}
	}
}
