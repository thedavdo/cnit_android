package com.davdo.todolist.anrdoid;

import androidx.lifecycle.ViewModel;

import com.davdo.todolist.src.Note;
import com.davdo.todolist.src.NoteCollection;

import java.util.Calendar;
import java.util.Random;

public class NoteListVewModel extends ViewModel {

    private NoteCollection mNotes;

    public NoteListVewModel() {

        mNotes = new NoteCollection();
    }

    public void generateExamples() {

        Calendar cal = Calendar.getInstance();
        Random r = new Random();

        int countNotes = mNotes.getNotes().size() + 1;

        for(int i = countNotes; i < countNotes + 20; i++) {

            cal.setTimeInMillis(Math.abs(r.nextLong()));

            Note tempNote = new Note("My Note #" + (i), cal.getTime(), r.nextBoolean());

            mNotes.insertNote(tempNote);
        }
    }

    public NoteCollection getNoteCollection() {
        return mNotes;
    }
}
