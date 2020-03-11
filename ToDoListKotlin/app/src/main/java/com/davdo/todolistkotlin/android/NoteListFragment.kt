package com.davdo.todolistkotlin.android


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davdo.todolistkotlin.R
import com.davdo.todolistkotlin.db.Note
import java.text.DateFormat
import java.util.*


class NoteListFragment : Fragment() {

	interface Callbacks {
		fun onNoteSelected(noteID: UUID, position: Int)
	}

	private var callbacks: Callbacks? = null

	private lateinit var noteRecyclerView: RecyclerView
	private lateinit var noteListViewModel: NoteListViewModel
	private var adapter : NoteAdapter? = NoteAdapter(emptyList())

	override fun onAttach(context: Context) {
		super.onAttach(context)

		callbacks = context as Callbacks?
	}

	override fun onDetach() {
		super.onDetach()

		callbacks = null
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		noteListViewModel = ViewModelProvider(this).get(NoteListViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		val view = inflater.inflate(R.layout.fragment_note_list, container, false)

		noteRecyclerView = view.findViewById(R.id.note_recycler_view)
		noteRecyclerView.layoutManager = LinearLayoutManager(context)

		noteRecyclerView.adapter = adapter

		return view
	}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteListViewModel.noteListLiveData.observe(viewLifecycleOwner,
            Observer{ notes ->
                notes.let {
                    updateUI(notes)
                }
            })
    }

	private fun updateUI(notes: List<Note>) {
		adapter = NoteAdapter(notes)
		noteRecyclerView.adapter = adapter
	}

	private inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

		val titleTextView : TextView = itemView.findViewById(R.id.note_title)
		val dateTextView : TextView = itemView.findViewById(R.id.note_date)
		val displayDoneImage: ImageView = itemView.findViewById(R.id.note_done)

		lateinit var note : Note

		init {
			itemView.setOnClickListener(this)
		}

		fun bind(newNote: Note) {
			note = newNote
			titleTextView.text = note.title
			dateTextView.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(note.date)
			displayDoneImage.visibility = if(note.done) View.VISIBLE else View.INVISIBLE
		}

		override fun onClick(v: View?) {
			callbacks?.onNoteSelected(note.uuid, this.layoutPosition)
		}
	}

	private inner class NoteAdapter(var notes: List<Note>) : RecyclerView.Adapter<NoteHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
			val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
			return NoteHolder(view)
		}

		override fun onBindViewHolder(holder: NoteHolder, position: Int) {

			val note : Note? = notes[position]
			
			if(note != null) holder.bind(note)
		}

		override fun getItemCount(): Int {
			return notes.size
		}
	}
}