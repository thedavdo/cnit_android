package com.davdo.todolist.src;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    public List<Note> getNotes();

    @Query("SELECT * FROM note WHERE id=(:id)")
    public Note getNote(UUID id);
}
