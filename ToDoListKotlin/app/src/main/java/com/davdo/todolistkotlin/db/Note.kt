package com.davdo.todolistkotlin.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(@PrimaryKey var uuid: UUID) : Parcelable {

	var title: String
	var date: Date
	var done: Boolean

	init {
		this.title = ""
		this.date = Date()
		this.done = false
	}

	constructor(): this(null,null,null,null)

	constructor(title: String, date: Date, done:Boolean) : this(title, date, done, null)

	constructor(title: String?, date: Date?, done:Boolean?, id: UUID?) : this(UUID.randomUUID()) {

		if(title != null) this.title = title
		if(date != null) this.date = date
		if(done != null) this.done = done
		if(id != null) this.uuid = id
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