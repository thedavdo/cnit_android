package com.davdo.todolistkotlin.src

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Note : Parcelable {

    var uuid: UUID
    var title: String
    var date: Date
    var done: Boolean

    constructor(): this(null,null,null,null)

    constructor(id: UUID) : this(null,null,  null, id)

    constructor(title: String?, date: Date?, done:Boolean?, id: UUID?) {

        if(title != null) this.title = title
        else this.title = ""

        if(date != null) this.date = date
        else this.date = Date()

        if(done != null) this.done = done
        else this.done = false

        if(id != null) this.uuid = id
        else this.uuid = UUID.randomUUID()
    }

    private constructor(parcel: Parcel) : this(parcel.readString(), Date(parcel.readLong()), (parcel.readInt() == 1), UUID.fromString(parcel.readString()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeLong(date.time)
        parcel.writeInt(if (done) 1 else 0)
        parcel.writeString(uuid.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}