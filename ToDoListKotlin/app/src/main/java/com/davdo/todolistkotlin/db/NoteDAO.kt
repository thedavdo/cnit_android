package com.davdo.todolistkotlin.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface NoteDAO {

	@Query("SELECT * FROM note")
	fun getNotes():LiveData<List<Note>>

	@Query("SELECT * FROM note WHERE uuid=(:id)")
	fun getNote(id: UUID): LiveData<Note?>

	@Update
	fun updateNote(note: Note)

	@Insert
	fun addNote(note: Note)

	@Delete
	fun deleteNote(note: Note)
}