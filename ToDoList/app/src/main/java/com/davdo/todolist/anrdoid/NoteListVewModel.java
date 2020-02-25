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

            Note tempNote = new Note();
            tempNote.setTitle("My Note #" + (i));
            tempNote.setDone(r.nextBoolean());

            cal.setTimeInMillis(Math.abs(r.nextLong()));
            tempNote.setDate(cal.getTime());

            mNotes.insertNote(tempNote);
        }
    }

    public NoteCollection getNoteCollection() {
        return mNotes;
    }
}
