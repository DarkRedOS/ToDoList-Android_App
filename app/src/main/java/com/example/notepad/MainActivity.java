package com.example.notepad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText noteEditText;
    private TextView displayTextView;
    private Button saveButton;
    private Button clearButton;
    private List<String> notesList;

    private static final String NOTES_KEY = "notes"; // Key for SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditText = findViewById(R.id.noteEditText);
        displayTextView = findViewById(R.id.displayTextView);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);

        notesList = new ArrayList<>();

        // Retrieve the notes from SharedPreferences
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String notes = sharedPreferences.getString(NOTES_KEY, "");
        if (!notes.isEmpty()) {
            String[] noteArray = notes.split(",");
            for (String note : noteArray) {
                notesList.add(note);
            }
            displayNotes();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = noteEditText.getText().toString();
                notesList.add(note);
                displayNotes();
                noteEditText.setText("");

                // Save the notes to SharedPreferences
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString(NOTES_KEY, String.join(",", notesList));
                editor.apply();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesList.clear();
                displayNotes();
                noteEditText.setText("");

                // Clear the notes from SharedPreferences
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.remove(NOTES_KEY);
                editor.apply();
            }
        });
    }

    private void displayNotes() {
        if (notesList.size() == 0) {
            displayTextView.setText("No notes yet");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String note : notesList) {
                sb.append(note).append("\n\n");
            }
            displayTextView.setText(sb.toString());
        }
    }
}
