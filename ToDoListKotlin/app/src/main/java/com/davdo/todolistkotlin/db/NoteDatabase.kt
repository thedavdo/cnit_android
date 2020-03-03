package com.davdo.todolistkotlin.db

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.davdo.todolistkotlin.src.Note

@Database(entities = [Note::class], version=1)
@TypeConverters(NoteTypeConverters::class)
abstract class NoteDatabase : RoomDatabase() {

		abstract fun noteDAO(): NoteDAO
}