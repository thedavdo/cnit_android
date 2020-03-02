package com.davdo.todolistkotlin.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.src.Note
import java.util.*


const val SaveNoteIndex = "save_note"
const val EditNoteIndex = "edit_uuid"

class NoteListFragment : Fragment() {

    private val mEditRequestCode = 0

    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteListViewModel: NoteListViewModel

    private lateinit var mEditNoteDisplay: Intent

    private var adapter : NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
//        noteListViewModel.generateExamples()

        mEditNoteDisplay = Intent(activity, EditNoteActivity::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        noteRecyclerView = view.findViewById(R.id.note_recycler_view)
        noteRecyclerView.layoutManager = LinearLayoutManager(context)

        initUI()

        return view
    }

    private fun initUI() {
        val notes = noteListViewModel.notes
        adapter = NoteAdapter(notes)
        noteRecyclerView.adapter = adapter
    }

    fun addNote(note: Note) {
        noteListViewModel.notes[note.uuid] = note
        adapter?.notifyDataSetChanged()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == mEditRequestCode && resultCode == Activity.RESULT_OK) {

            val save : Int? = data?.getIntExtra(SaveNoteIndex, 0)
            val resNote: Note? = data?.getParcelableExtra(EditNoteIndex)

            if(resNote != null) {
                when {
                    (save == EditNoteActivity.ActionSave) -> {
                        noteListViewModel.notes[resNote.uuid]?.title = resNote.title
                        noteListViewModel.notes[resNote.uuid]?.date = resNote.date
                        noteListViewModel.notes[resNote.uuid]?.done = resNote.done

                        noteRecyclerView.adapter?.notifyDataSetChanged()
                    }
                    (save == EditNoteActivity.ActionRemove) -> {
                        noteListViewModel.notes.remove(resNote.uuid)
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val titleTextView : TextView = itemView.findViewById(R.id.note_title)
        val dateTextView : TextView = itemView.findViewById(R.id.note_date)
        lateinit var note : Note

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(newNote: Note) {
            note = newNote
            titleTextView.text = note.title
            dateTextView.text = note.date.toString()
        }

        override fun onClick(v: View?) {
            mEditNoteDisplay.putExtra(EditNoteIndex, note)
            startActivityForResult(mEditNoteDisplay, mEditRequestCode)
        }
    }

    private inner class NoteAdapter(var notes: MutableMap<UUID, Note>) : RecyclerView.Adapter<NoteHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
            val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
            return NoteHolder(view)
        }

        override fun onBindViewHolder(holder: NoteHolder, position: Int) {

            val id: UUID = notes.keys.asSequence().elementAt(position)

            val note : Note? = notes[id]

            if(note != null)
                holder.bind(note)
        }

        override fun getItemCount(): Int {
            return notes.size
        }
    }
}