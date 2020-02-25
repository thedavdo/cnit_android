package com.davdo.todolist.src;

import androidx.collection.ArrayMap;

public class NoteCollection {

    private ArrayMap<String, Note> mNotes;

    public NoteCollection() {
        mNotes = new ArrayMap<>();
    }

    public void insertNote(Note newNote) {
        mNotes.put(newNote.getId().toString(), newNote);
    }

    public ArrayMap<String, Note> getNotes() {
        return mNotes;
    }
}
