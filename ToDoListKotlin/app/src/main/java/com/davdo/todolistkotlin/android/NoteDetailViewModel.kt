package com.davdo.todolistkotlin.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.davdo.todolistkotlin.db.NoteRepository
import com.davdo.todolistkotlin.src.Note
import java.util.*

class NoteDetailViewModel() : ViewModel() {

	private val noteRepository = NoteRepository.get()
	private val noteIdLiveData = MutableLiveData<UUID>()

	var noteLiveData: LiveData<Note?> = Transformations.switchMap(noteIdLiveData) { noteID ->
		noteRepository.getNote(noteID)
	}

	fun loadNote(noteID: UUID) {
		noteIdLiveData.value = noteID
	}

	fun updateNote(note: Note) {
		noteRepository.updateNote(note)
	}
}