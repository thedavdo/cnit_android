package com.davdo.todolistkotlin.android

import android.app.Application
import com.davdo.todolistkotlin.db.NoteRepository

class TodoListIntentApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		NoteRepository.initialize(this)
	}
}