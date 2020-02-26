package com.davdo.todolist.src;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class NoteTypeConverters {


    @TypeConverter
    public long fromDate(Date d) {

        if(d != null)
            return d.getTime();

        return -1;
    }

    @TypeConverter
    public Date toDate(long t) {

        return new Date(t);
    }

    @TypeConverter
    public String fromUUID(UUID id) {

        if(id != null)
            return id.toString();

        return null;
    }

    @TypeConverter
    public UUID toUUID(String id) {

        return UUID.fromString(id);
    }

}
