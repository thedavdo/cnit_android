package com.davdo.todolist.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoteCollection {


    private ArrayList<Note> mNotes;

    public NoteCollection() {
        mNotes = new ArrayList<>();
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }
}
