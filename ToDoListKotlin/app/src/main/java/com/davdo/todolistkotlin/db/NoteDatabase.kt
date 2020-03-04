package com.davdo.todolistkotlin.db

import androidx.room.*

@Database(entities = [Note::class], version=1)
@TypeConverters(NoteTypeConverters::class)
abstract class NoteDatabase : RoomDatabase() {

		abstract fun noteDAO(): NoteDAO
}