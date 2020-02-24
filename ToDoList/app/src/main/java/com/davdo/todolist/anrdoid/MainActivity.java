package com.davdo.todolist.anrdoid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.davdo.todolist.R;
import com.davdo.todolist.src.Note;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {

//            Bundle testBundle = new Bundle();
//
//            Note testNote = new Note();
//            testNote.setTitle("Tester");
//            testNote.setDone(true);
//
//            testBundle.putParcelable(NoteFragment.NOTE_INDEX, testNote);

            fragment = new NoteFragment();
//            fragment.setArguments(testBundle);

            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
