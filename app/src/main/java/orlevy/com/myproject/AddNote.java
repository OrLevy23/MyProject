package orlevy.com.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class AddNote extends AppCompatActivity {
    EditText subject;
    EditText note;
    private static ArrayList<String> list = new ArrayList<>();
    private static boolean isEdit = false;
    private static int idToEdit;
    private static Intent edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DBHandler handler = new DBHandler(this);
        subject = (EditText) findViewById(R.id.Subject);
        note = (EditText) findViewById(R.id.Note);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strSubject = subject.getText().toString();
                final String strNote = note.getText().toString();
                if (!strSubject.equals("")) {
                    if (!strNote.equals("")) {
                        if (!isEdit) {
                            handler.addNote(strSubject, strNote);
                        } else {
                            handler.editNote(idToEdit,strSubject,strNote);
                            isEdit = false;
                        }
                        Intent a = new Intent(AddNote.this, MainActivity.class);
                        startActivity(a);
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                        builder.setTitle("Empty note");
                        builder.setMessage("Would you like to save an empty note?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!isEdit) {
                                    handler.addNote(strSubject, strNote);

                                } else {
                                    handler.editNote(idToEdit,strSubject,strNote);
                                    isEdit = false;
                                }
                                Intent a = new Intent(AddNote.this, MainActivity.class);
                                startActivity(a);
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }

                } else {
                    Snackbar.make(view, "Empty note, please fill out", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!(subject.getText().toString().equals("") && note.getText().toString().equals(""))){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Everything will be deleted");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    } else {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        edit = getIntent();
        try {
            idToEdit = edit.getIntExtra("id", -1);
            DBHandler handler = new DBHandler(this);
            if(idToEdit != -1) {
                Note noteToEDIT = handler.getItem(idToEdit);
                subject.setText(noteToEDIT.getSubject());
                note.setText(noteToEDIT.getNote());
                isEdit = true;
            }

        } catch (Exception e) {
            Log.d("new item", "new item");
        }
    }
}
