package com.davdo.todolistkotlin.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.davdo.todolistkotlin.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment_note_container)

        if(myFragment == null) {
            myFragment = NoteFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_note_container, myFragment).commit()
        }
    }
}
