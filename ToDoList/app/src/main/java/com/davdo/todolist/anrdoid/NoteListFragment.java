package com.davdo.todolist.anrdoid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davdo.todolist.R;
import com.davdo.todolist.src.Note;
import com.davdo.todolist.src.NoteCollection;

public class NoteListFragment extends Fragment {

    private NoteListVewModel mNoteListVewModel;
    private RecyclerView noteListView;
    private NoteAdapter noteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_note_list, container, false);

        mNoteListVewModel = new ViewModelProvider(this).get(NoteListVewModel.class);
        mNoteListVewModel.generateExamples();

        noteListView = v.findViewById(R.id.note_recycler_view);
        noteListView.setLayoutManager(new LinearLayoutManager(getContext()));

        noteAdapter = new NoteAdapter(mNoteListVewModel.getNoteCollection());
        noteListView.setAdapter(noteAdapter);

        return v;
    }

    private void updateUI() {

        noteAdapter.notifyDataSetChanged();
    }

    private class NoteHolder extends RecyclerView.ViewHolder {

        private TextView titleText;
        private TextView dateText;

        NoteHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.note_title);
            dateText = itemView.findViewById(R.id.note_date);
        }

        public void applyNote(Note note) {

            if(note != null) {
                titleText.setText(note.getTitle());
                dateText.setText(note.getDate().toString());
            }
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter {

       private NoteCollection mNotes;

        NoteAdapter(NoteCollection notes) {
            mNotes = notes;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = getLayoutInflater().inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Note note = mNotes.getNotes().valueAt(position);

            if(note != null) {
                NoteHolder noteHolder = (NoteHolder) holder;
                noteHolder.applyNote(note);
            }
        }

        @Override
        public int getItemCount() {
            return mNotes.getNotes().size();
        }

    }
}

