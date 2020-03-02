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
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_note_container);

        if(fragment == null) {

//            fragment = new NoteFragment();
            fragment = new NoteListFragment();

//            Bundle testBundle = new Bundle();
//            testBundle.putParcelable(NoteFragment.NOTE_INDEX, note);
//            fragment.setArguments(testBundle);

            fragmentManager.beginTransaction().add(R.id.fragment_note_container, fragment).commit();
        }
    }
}
