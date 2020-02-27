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
import androidx.fragment.app.Fragment
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.src.Note
import java.util.*


private const val noteIndex = "NOTE_OBJ"
private const val notTag = "NOTE_TAG"

class NoteFragment : Fragment() {

    private lateinit var cal: Calendar

    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mDoneCheckbox: CheckBox? = null

    private lateinit var mNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cal = Calendar.getInstance()

        var tempNote: Note? = savedInstanceState?.getParcelable(noteIndex)
        if(tempNote == null) tempNote = Note()

        mNote = tempNote

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(noteIndex, mNote)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       val v = inflater.inflate(R.layout.fragment_note, container, false)

        if(arguments != null) {
            val parNote: Note? = arguments?.getParcelable(noteIndex)
            if(parNote != null) mNote = parNote
        }

        mTitleField = v.findViewById(R.id.edit_text_title)
        mDateButton = v.findViewById(R.id.button_date)
        mDoneCheckbox = v.findViewById(R.id.checkbox_finished)

        mTitleField?.setText(mNote.title)
        mDateButton?.text = (mNote.date.toString())
        mDoneCheckbox?.isChecked = mNote.done


        mTitleField?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mNote.title = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        mTitleField?.setOnEditorActionListener { _, actionId, _ ->
            //Log.d(TAG, "onEditorAction() called: " + textView.getText());
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mTitleField?.clearFocus()
            }
            false
        }

        mDoneCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            mNote.done = isChecked
        }

        val dateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                cal[Calendar.YEAR] = year
                cal[Calendar.MONTH] = monthOfYear
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth

                mNote.date.time = cal.time.time
                mDateButton?.text = mNote.date.toString()
        }

        mDateButton?.setOnClickListener {
            cal.time = mNote.date

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
}