package com.davdo.todolistkotlin.android

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


class NoteListFragment : Fragment() {


    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteListViewModel: NoteListViewModel

    private var adapter : NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
        noteListViewModel.generateExamples()
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

        }
    }

    private inner class NoteAdapter(var notes : List<Note>) : RecyclerView.Adapter<NoteHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
            val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
            return NoteHolder(view)
        }

        override fun getItemCount(): Int {
            return notes.size
        }

        override fun onBindViewHolder(holder: NoteHolder, position: Int) {
            holder.bind(notes[position])
        }

    }
}