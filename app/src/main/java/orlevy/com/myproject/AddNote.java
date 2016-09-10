package orlevy.com.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class AddNote extends AppCompatActivity {
    EditText subject;
    EditText note;
    private static ArrayList<String> list = new ArrayList<>();
    private boolean isEdit = false;
    private static MenuItem star;
    private static int idToEdit;
    private static Intent edit;
    private Note noteToEDIT;
    private DBHandler handler;
    private  Boolean isStarred = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new DBHandler(this);
        subject = (EditText) findViewById(R.id.Subject);
        note = (EditText) findViewById(R.id.Note);
    }

    @Override
    public void onBackPressed() {
            if (!(subject.getText().toString().equals("") && note.getText().toString().equals(""))) {
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
//    Note clone = new Note(0,subject.getText().toString(),note.getText().toString(),isStarred);
//    if(!isEdit && noteToEDIT.isClone(clone)) {


    @Override
    protected void onResume() {
        super.onResume();
        edit = getIntent();
        try {
            idToEdit = edit.getIntExtra("id", -1);
            DBHandler handler = new DBHandler(this);
            if(idToEdit != -1) {
                noteToEDIT = handler.getItem(idToEdit);
                subject.setText(noteToEDIT.getSubject());
                note.setText(noteToEDIT.getNote());
                isStarred = noteToEDIT.isStarred();
                if(isStarred) {
                    star.setIcon(R.drawable.starred);
                } else {
                    star.setIcon(R.drawable.not_starred);
                }
                isEdit = true;
            }

        } catch (Exception e) {
            Log.d("new item", "new item");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note_v2,menu);
        star = menu.findItem(R.id.action_star);
        updateStarredStatus();
        return true;
    }

    private void updateStarredStatus() {
        if(isStarred) {
            star.setIcon(R.drawable.starred);
        } else {
            star.setIcon(R.drawable.not_starred);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add: {
                final String strSubject = subject.getText().toString();
                final String strNote = note.getText().toString();
                if (!strSubject.equals("")) {
                    if (!strNote.equals("")) {
                        if (!isEdit) {
                            handler.addNote(strSubject, strNote,isStarred);
                        } else {
                            handler.editNote(idToEdit,strSubject,strNote,isStarred);
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
                                    handler.addNote(strSubject, strNote,isStarred);

                                } else {
                                    handler.editNote(idToEdit,strSubject,strNote,isStarred);
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
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "Empty note, please fill out", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            }

            case R.id.action_remove: {
                onBackPressed();
                break;
            }
            case R.id.action_star : {
                isStarred = !isStarred;
                updateStarredStatus();
            }
        }

        return true;
        }
    }

