package com.davdo.todolist.anrdoid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davdo.todolist.R;
import com.davdo.todolist.src.Note;

import java.util.Calendar;

public class NoteFragment extends Fragment {

    public static final String NOTE_INDEX = "NOTE_OBJ";

    private static final String TAG = "NOTE_FRAG";

    private Calendar cal;

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mDoneCheckbox;

    private Note mNote;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cal = Calendar.getInstance();

        if(savedInstanceState != null) {
            mNote = savedInstanceState.getParcelable(NOTE_INDEX);
        }
        else {
//            getIntent().getParcelableExtra(MainActivity.QUESTION_INDEX);
            mNote = new Note();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(NOTE_INDEX, mNote);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_note, container, false);

        Bundle args = getArguments();

        if(args != null) {
            mNote = getArguments().getParcelable(NoteFragment.NOTE_INDEX);
        }

        mTitleField = v.findViewById(R.id.edit_text_title);
        mDateButton = v.findViewById(R.id.button_date);
        mDoneCheckbox = v.findViewById(R.id.checkbox_finished);

        mTitleField.setText(mNote.getTitle());
        mDateButton.setText(mNote.getDate().toString());
        mDoneCheckbox.setChecked(mNote.isDone());

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mNote.getDate().setTime(cal.getTime().getTime());
                mDateButton.setText(mNote.getDate().toString());
            }
        };

        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                cal.setTime(mNote.getDate());

                DatePickerDialog dialog = new DatePickerDialog(getContext(), dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mTitleField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                //Log.d(TAG, "onEditorAction() called: " + textView.getText());
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mTitleField.clearFocus();
                }
                return false;
            }
        });

        mDoneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNote.setDone(isChecked);
            }
        });


        return v;
    }


}
