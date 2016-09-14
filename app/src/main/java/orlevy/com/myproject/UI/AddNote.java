package orlevy.com.myproject.UI;

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

import orlevy.com.myproject.Class.Note;
import orlevy.com.myproject.DB.DBHandler;
import orlevy.com.myproject.R;

public class AddNote extends AppCompatActivity {
    private static int RESTORE_NOTES = Menu.FIRST + 1;
    EditText subject;
    EditText note;
    private boolean isEdit = false;
    private static MenuItem star;
    private static int idToEdit;
    private boolean isArchived = false;
    private static Intent edit;
    private Note noteToEDIT;
    private DBHandler handler;
    private Boolean isStarred = false;
    private String strNote;
    private String strSubject;
    private boolean isFromStarred = false;

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
        if ((subject.getText().toString().equals("") && note.getText().toString().equals(""))) {
            finish();
        } else if (isEdit) {
            Note clone = new Note(0, subject.getText().toString(), note.getText().toString(), isStarred, false);
            if (noteToEDIT.isClone(clone)) {
                finish();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                builder.setTitle("Save");
                builder.setMessage("Would you like to save your changes?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.editNote(idToEdit, subject.getText().toString(), note.getText().toString(), isStarred, isArchived);
                        if (!isArchived) {
                            Intent edit = new Intent(AddNote.this, MainActivity.class);
                            startActivity(edit);
                        } else {
                            Intent editArchived = new Intent(AddNote.this, Archived.class);
                            startActivity(editArchived);
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
            builder.setTitle("Save");
            builder.setMessage("Would you like to save your note?");
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.addNote(subject.getText().toString(), note.getText().toString(), isStarred, false);
                    Intent add = new Intent(AddNote.this, MainActivity.class);
                    startActivity(add);

                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        edit = getIntent();
        try {
            idToEdit = edit.getIntExtra("id", -1);
            isArchived = edit.getBooleanExtra("archived", false);
            isFromStarred = edit.getBooleanExtra("starred",false);
            if (isFromStarred) {
               isStarred = true;
            }
            DBHandler handler = new DBHandler(this);
            if (idToEdit != -1) {
                isEdit = true;
                noteToEDIT = handler.getItem(idToEdit);
                subject.setText(noteToEDIT.getSubject());
                note.setText(noteToEDIT.getNote());
                isStarred = noteToEDIT.isStarred();
                if (isStarred) {
                    star.setIcon(R.drawable.starred);
                } else {
                    star.setIcon(R.drawable.not_starred);
                }

            }

        } catch (Exception e) {
            Log.d("new item", "new item");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note_v2, menu);
        star = menu.findItem(R.id.action_star);
        if (isArchived) {
            menu.add(0, RESTORE_NOTES, Menu.NONE, "Restore to notes");
        }
        updateStarredStatus();
        return true;
    }

    private void updateStarredStatus() {
        if (isStarred) {
            star.setIcon(R.drawable.starred);
        } else {
            star.setIcon(R.drawable.not_starred);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add) {
                strSubject = subject.getText().toString();
                strNote = note.getText().toString();
                if (!strSubject.equals("")) {
                    if (!strNote.equals("")) {
                        if (!isEdit) {
                            handler.addNote(strSubject, strNote, isStarred, false);
                        } else {
                            handler.editNote(idToEdit, strSubject, strNote, isStarred, isArchived);
                            isEdit = false;
                        }
                        if (isArchived) {
                            Intent a = new Intent(AddNote.this, Archived.class);
                            startActivity(a);
                        } else {
                            Intent a = new Intent(AddNote.this, MainActivity.class);
                            startActivity(a);
                        }

                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                        builder.setTitle("Empty note");
                        builder.setMessage("Would you like to save an empty note?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isEdit) {
                                    handler.addNote(strSubject, strNote, isStarred, false);

                                } else {
                                    handler.editNote(idToEdit, strSubject, strNote, isStarred, isArchived);
                                    isEdit = false;
                                }
                                if (!isArchived) {
                                    Intent a = new Intent(AddNote.this, MainActivity.class);
                                    startActivity(a);
                                } else {
                                    Intent editArchive = new Intent(AddNote.this, Archived.class);
                                    startActivity(editArchive);
                                }
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

            } else if (itemId == R.id.action_remove) {
                onBackPressed();
            } else if (itemId == R.id.action_star) {
                isStarred = !isStarred;
                updateStarredStatus();
            } else if (itemId == RESTORE_NOTES) {
                strSubject = subject.getText().toString();
                strNote = note.getText().toString();
                if (!(strSubject == "")) {
                    if (!(strNote == "")) {
                        handler.editNote(idToEdit, strSubject, strNote, isStarred, false);
                        Intent back = new Intent(AddNote.this, MainActivity.class);
                        startActivity(back);
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                        builder.setTitle("Empty note");
                        builder.setMessage("Would you like to save an empty note?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.editNote(idToEdit, strSubject, strNote, isStarred, false);
                                Intent back = new Intent(AddNote.this, MainActivity.class);
                                startActivity(back);
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
            }

        return true;
    }
}

