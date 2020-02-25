package com.davdo.todolist.anrdoid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.davdo.todolist.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {

//            fragment = new NoteFragment();
            fragment = new NoteListFragment();

//            Bundle testBundle = new Bundle();
//            String noteKey =  mNotes.getNotes().keyAt(0);
//            testBundle.putParcelable(NoteFragment.NOTE_INDEX, mNotes.getNotes().get(noteKey));
//
//            fragment.setArguments(testBundle);

            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
