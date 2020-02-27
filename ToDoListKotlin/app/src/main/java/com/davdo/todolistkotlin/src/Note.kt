package com.davdo.todolistkotlin.src

import java.util.*

class Note {

    var uuid: UUID
    var title: String
    var date: Date
    var done: Boolean

    constructor(): this(UUID.randomUUID())

    constructor(id: UUID) : this("", Date(), false, id)

    constructor(title: String, date: Date, done:Boolean, id: UUID) {
        this.title = title
        this.date = date
        this.done = done
        this.uuid = id
    }


}