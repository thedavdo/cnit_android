package com.davdo.todolistkotlin.android

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.davdo.todolistkotlin.db.NoteRepository
import com.davdo.todolistkotlin.src.Note
import java.util.*
import kotlin.math.abs

class NoteListViewModel : ViewModel() {

	//val notes = mutableMapOf<UUID, Note>() //mutableListOf<Note>()
    private val noteRepository = NoteRepository.get()
    val noteListLiveData = noteRepository.getNotes()

	private val noteIDLiveData = MutableLiveData<UUID>()

	var noteLiveData: LiveData<Note?> = Transformations.switchMap(noteIDLiveData) { noteID ->
		noteRepository.getNote(noteID)
	}

	fun loadNote(noteID: UUID) {
		noteIDLiveData.value = noteID
	}

	fun saveNote(note: Note) {
		noteRepository.updateNote(note)
	}

	fun getNote(uuid: UUID): Note? {
		return noteRepository.getNote(uuid).value
	}


	fun generateExamples() {

		val cal: Calendar = Calendar.getInstance()
		val r = Random()

//		val countNotes: Int = noteListLiveData. .size + 1

		for (i in 1 until 20) {
			cal.timeInMillis = abs(r.nextLong())
			val tempNote = Note("My Note #$i", cal.time, r.nextBoolean())

			noteRepository.addNote(tempNote)
//			notes[tempNote.uuid] = tempNote
		}

	}
}