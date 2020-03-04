package com.davdo.todolistkotlin.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "note-database"

class NoteRepository private constructor(context: Context) {

	private val database:NoteDatabase = Room.databaseBuilder(
		context.applicationContext,
		NoteDatabase::class.java,
		DATABASE_NAME
	).build()

	private val noteDAO = database.noteDAO()
	private val executor = Executors.newSingleThreadExecutor()

	fun getNotes(): LiveData<List<Note>> = noteDAO.getNotes()

	fun getNote(id: UUID):LiveData<Note?> = noteDAO.getNote(id)

	fun updateNote(note: Note) {
		executor.execute {
			noteDAO.updateNote(note)
		}
	}

	fun addNote(note: Note) {
		executor.execute {
			noteDAO.addNote(note)
		}
	}

	fun deleteNote(note: Note) {
		executor.execute {
			noteDAO.deleteNote(note)
		}
	}

	//Singleton :(
	companion object {

		private var INSTANCE: NoteRepository? = null

		fun initialize(context: Context) {
			if(INSTANCE == null) {
				INSTANCE = NoteRepository(context)
			}
		}

		fun get(): NoteRepository {
			return INSTANCE?: throw IllegalStateException("NoteRepository must be initialized")
		}
	}

}