package com.davdo.todolistkotlin.android

import androidx.lifecycle.ViewModel
import com.davdo.todolistkotlin.src.Note
import java.util.Calendar
import java.util.Random
import kotlin.math.abs

class NoteListViewModel : ViewModel() {


    val notes = mutableListOf<Note>()


    fun generateExamples() {

        val cal: Calendar = Calendar.getInstance()
        val r = Random()

        val countNotes: Int = notes.size + 1

        for (i in countNotes until countNotes + 20) {
            cal.timeInMillis = abs(r.nextLong())
            val tempNote = Note("My Note #$i", cal.time, r.nextBoolean())
            notes += tempNote
        }
    }


}