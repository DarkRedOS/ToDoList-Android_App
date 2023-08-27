package com.example.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText noteEditText;
    private Button saveButton;
    private Button clearButton;
    private ListView notesListView;
    private NotesListAdapter notesListAdapter;
    private List<String> notesList;

    private static final String NOTES_KEY = "notes"; // Key for SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);
        notesListView = findViewById(R.id.notesListView);

        notesList = new ArrayList<>();
        notesListAdapter = new NotesListAdapter(this, notesList);
        notesListView.setAdapter(notesListAdapter);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String notes = sharedPreferences.getString(NOTES_KEY, "");
        if (!notes.isEmpty()) {
            String[] noteArray = notes.split(",");
            for (String note : noteArray) {
                notesList.add(note);
            }
            notesListAdapter.notifyDataSetChanged();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = noteEditText.getText().toString();
                notesList.add(note);
                notesListAdapter.notifyDataSetChanged();
                noteEditText.setText("");

                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString(NOTES_KEY, String.join(",", notesList));
                editor.apply();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.clear();
                notesListAdapter.notifyDataSetChanged();
                noteEditText.setText("");

                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.remove(NOTES_KEY);
                editor.apply();
            }
        });
    }

    private class NotesListAdapter extends ArrayAdapter<String> {

        private Context context;
        private List<String> notes;

        public NotesListAdapter(Context context, List<String> notes) {
            super(context, R.layout.note_item, notes);
            this.context = context;
            this.notes = notes;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.note_item, parent, false);

            TextView noteTextView = rowView.findViewById(R.id.noteTextView);
            Button deleteButton = rowView.findViewById(R.id.deleteButton);

            noteTextView.setText(notes.get(position));

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notes.remove(position);
                    notifyDataSetChanged();

                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putString(NOTES_KEY, String.join(",", notes));
                    editor.apply();
                }
            });

            return rowView;
        }
    }
}
