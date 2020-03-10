package com.davdo.todolistkotlin.android


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.db.Note
import androidx.lifecycle.Observer
import java.util.*


class NoteFragment : Fragment(), BackPressedListener {

	private lateinit var noteListViewModel: NoteListViewModel
	private lateinit var noteDetailViewModel: NoteDetailViewModel

	private var mTitleField: EditText? = null

	private var mDateTextView: TextView? = null
	private var mDoneCheckbox: CheckBox? = null

	private var mConfirmSave: AlertDialog? = null
	private var mConfirmDelete: AlertDialog? = null

	private lateinit var cal: Calendar

	private var mNote: Note? = null
	private var mChangeDate : Long? = null

	private var mNewNote: Boolean = false
	private var mReadOnly : Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setHasOptionsMenu(true)

		noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
		noteDetailViewModel = ViewModelProvider(this).get(NoteDetailViewModel::class.java)

		cal = Calendar.getInstance()

		val argID = arguments?.getSerializable(ARG_NOTE_ID)
		val argReadOnly = arguments?.getSerializable(ARG_NOTE_READONLY)

		if(argID != null) {
			val noteID: UUID = argID as UUID
			noteDetailViewModel.loadNote(noteID)
		}
		else {
			mNote = Note()
			mNewNote = true
		}

		if(argReadOnly == true) {
			mReadOnly = true
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)

		menu.clear()
		inflater.inflate(R.menu.edit_note, menu)

		if(mNewNote) {
			menu.findItem(R.id.menu_button_delete_note).isVisible = false
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		if(item.itemId == R.id.menu_button_delete_note) {
			mConfirmDelete?.show()
		}
		else if(item.itemId == R.id.menu_button_save_note) {
			doSave()
			activity?.onBackPressed()
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		noteDetailViewModel.noteLiveData.observe(viewLifecycleOwner, Observer { note ->

			note?.let {
				this.mNote = it
				updateUI()
			}
		})
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

	   val v = inflater.inflate(R.layout.fragment_note, container, false)

		mTitleField = v.findViewById(R.id.edit_text_title)
		mDoneCheckbox = v.findViewById(R.id.checkbox_finished)
		mDateTextView = v.findViewById(R.id.textview_date)

		mTitleField?.isEnabled = !mReadOnly
		mDateTextView?.isEnabled = !mReadOnly
		mDoneCheckbox?.isEnabled = !mReadOnly

		val builder = AlertDialog.Builder(inflater.context)
		builder.setMessage("Save changes?")
		builder.setPositiveButton("save") { _, _ ->
			doSave()
			activity?.onBackPressed()
		}
		builder.setNeutralButton("discard") { _,_ ->
			mNote = null
			activity?.onBackPressed()
		}
		builder.setNegativeButton("cancel") { _, _ -> }
		builder.setOnDismissListener {}

		mConfirmSave = builder.create()

		val deleteBuilder = AlertDialog.Builder(inflater.context)
		deleteBuilder.setMessage("Delete note?")
		deleteBuilder.setPositiveButton("delete") { _, _ ->
			noteListViewModel.deleteNote(mNote!!)
			mNote = null
			activity?.onBackPressed()
		}
		deleteBuilder.setNegativeButton("cancel") { _,_ -> }

		mConfirmDelete = deleteBuilder.create()

		mTitleField?.setText(mNote?.title)

		mDateTextView?.text = (mNote?.date.toString())

		mDoneCheckbox?.isChecked = mNote?.done ?: false
		mChangeDate = mNote?.date?.time

		mTitleField?.setOnEditorActionListener { _, actionId, _ ->

			if (actionId == EditorInfo.IME_ACTION_DONE) {
				mTitleField?.clearFocus()
			}
			false
		}

		val dateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
			cal[Calendar.YEAR] = year
			cal[Calendar.MONTH] = monthOfYear
			cal[Calendar.DAY_OF_MONTH] = dayOfMonth

			mChangeDate = cal.time.time
			mDateTextView?.text = mNote?.date.toString()
		}

		mDateTextView?.setOnClickListener {

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

	override fun onBackPressed(): Boolean {

		if(isChangesMade()) {
			mConfirmSave?.show()
			return true
		}

		return false
	}

	fun isChangesMade(): Boolean {

		var changed = false

		if(mNote != null) {
			if(!mTitleField?.text?.toString().equals(mNote?.title)) changed = true
			if(mDoneCheckbox?.isChecked != mNote?.done) changed = true
			if(mChangeDate != mNote?.date?.time) changed = true
		}

		return changed
	}

	private fun doSave() {

		if(mNote != null) {
			mNote?.title = mTitleField?.text.toString()
			mNote?.done = mDoneCheckbox?.isChecked ?: mNote?.done!!
			mNote?.date?.time = mChangeDate ?: mNote?.date?.time!!

			if(mNewNote) noteListViewModel.addNote(mNote!!)
			else noteDetailViewModel.updateNote(mNote!!)
		}
	}

	private fun updateUI() {
		mTitleField?.setText(mNote?.title)

		mChangeDate = mNote?.date?.time
		mDateTextView?.text = mNote?.date.toString()
		mDoneCheckbox?.isChecked = mNote?.done ?: false

		mTitleField?.jumpDrawablesToCurrentState()
		mDoneCheckbox?.jumpDrawablesToCurrentState()
	}

	companion object {

		private const val ARG_NOTE_ID: String = "note_id"
		private const val ARG_NOTE_READONLY: String = "note_readonly"

		fun newInstance(noteID: UUID): NoteFragment {

			val args = Bundle().apply { putSerializable(ARG_NOTE_ID, noteID) }
			return NoteFragment().apply { arguments = args }
		}

		fun newInstance(noteID: UUID, readOnly: Boolean): NoteFragment {

			val args = Bundle().apply {
				putSerializable(ARG_NOTE_ID, noteID)
				putSerializable(ARG_NOTE_READONLY, readOnly)
			}

			return NoteFragment().apply { arguments = args }
		}
	}
}
