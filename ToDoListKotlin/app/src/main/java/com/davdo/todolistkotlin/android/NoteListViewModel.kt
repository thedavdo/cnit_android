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

    private val noteRepository = NoteRepository.get()
    val noteListLiveData = noteRepository.getNotes()

	fun addNote(note: Note) {
		noteRepository.addNote(note)
	}

	fun deleteNote(note: Note) {
		noteRepository.deleteNote(note)
	}

	fun generateExamples() {

		val cal: Calendar = Calendar.getInstance()
		val rnd = Random()

		for (i in 1 until 20) {
			cal.timeInMillis = abs(rnd.nextLong())
			val tempNote = Note("My Note #${rnd.nextInt()}", cal.time, rnd.nextBoolean())

			noteRepository.addNote(tempNote)
		}
	}
}