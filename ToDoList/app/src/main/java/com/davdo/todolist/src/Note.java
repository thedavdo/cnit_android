package com.davdo.todolist.src;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import java.util.Date;
import java.util.UUID;

@Entity
public class Note implements Parcelable {

    private UUID mId;
    private String mTitle;
    private Date mDate;

    private boolean mDone;


    public Note() {

        mTitle = "";
        mDate = new Date();
        mDone = false;
        mId = UUID.randomUUID();
    }

    protected Note(Parcel in) {
        mTitle = in.readString();
        mDone = in.readByte() != 0;
        mDate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeByte((byte) (mDone ? 1 : 0));
        dest.writeLong(mDate.getTime());
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

}
