package orlevy.com.myproject.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import orlevy.com.myproject.Class.Note;
import orlevy.com.myproject.DB.DBHandler;
import orlevy.com.myproject.R;

public class AddNote extends AppCompatActivity {
    private static int RESTORE_NOTES = Menu.FIRST + 1;
    private static int DELETE_NOTE = Menu.FIRST+2;
    EditText subject;
    EditText note;
    private boolean isEdit = false;
    private static MenuItem star;
    private static int idToEdit;
    private boolean isArchived = false;
    private Note noteToEDIT;
    private DBHandler handler;
    private Boolean isStarred = false;
    private Boolean fromStarred = false;
    private Boolean fromArchived = false;
    private String strNote;
    private String strSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new DBHandler(this);
        subject = (EditText) findViewById(R.id.Subject);
        note = (EditText) findViewById(R.id.Note);
        Intent edit = getIntent();
        idToEdit = edit.getIntExtra("id", -1);
        fromArchived = edit.getBooleanExtra("fromArchived",false);
        fromStarred = edit.getBooleanExtra("fromStarred",false);
        if(fromStarred) {
            isStarred = true;
        }
        if(idToEdit != -1) {
            isEdit = true;
            updateEdit();
        }
    }

    private void updateEdit() {
            noteToEDIT = handler.getItem(idToEdit);
            subject.setText(noteToEDIT.getSubject());
            note.setText(noteToEDIT.getNote());
            isStarred = noteToEDIT.isStarred();
    }

    @Override
    public void onBackPressed() {
        if (subject.getText().toString().equals("") && note.getText().toString().equals("")) {
            sendBack();
        }
        else if (!(subject.getText().toString().equals("")) && note.getText().toString().equals("")) { // This is in case that Subject contains text but not is empty
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
            builder.setTitle("Save");
            builder.setMessage("Empty note, are you sure you want to save?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addNewNote();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
            builder.setTitle("Save");
            builder.setMessage("Do you want to save your changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addNewNote();
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
    }

    private void sendBack() {
        if(fromArchived) {
            Intent archived = new Intent(AddNote.this, Archived.class);
            startActivity(archived);
            finish();
        } else if(fromStarred && !isStarred) {
            Intent starred = new Intent(AddNote.this,Starred.class);
            startActivity(starred);
            finish();
        } else {
            Intent main = new Intent(AddNote.this,MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    private void addNewNote() {
        strSubject = subject.getText().toString();
        strNote = note.getText().toString();
        if(isEdit) {
            handler.editNote(idToEdit, subject.getText().toString(), note.getText().toString(), isStarred, isArchived);
        } else {
            handler.addNote(strSubject,strNote,isStarred,isArchived);
        }
        if(fromArchived) {
            Intent archived = new Intent(AddNote.this,Archived.class);
            startActivity(archived);
        } else if(fromStarred && isStarred) {
            Intent starred = new Intent(AddNote.this,Starred.class);
            startActivity(starred);
        } else {
            Intent mainActivity = new Intent(AddNote.this,MainActivity.class);
            startActivity(mainActivity);
        }
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note_v2, menu);
        star = menu.findItem(R.id.action_star);
        if (isArchived) {
            menu.add(0, RESTORE_NOTES, Menu.NONE, "Restore to notes");
        } if (idToEdit != -1) {
            menu.add(1,DELETE_NOTE,Menu.NONE,"Delete");
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
                if (strSubject.equals("")) {
                    if(strNote.equals("")) {
                        View view = findViewById(android.R.id.content);
                        Snackbar.make(view, "Empty note, please insert data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                        builder.setTitle("Save");
                        builder.setMessage("Would you like to save your changes?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addNewNote();
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
                    addNewNote();
                    finish();
                }
            }
            else if (itemId == R.id.action_remove) {
                if(isEdit || fromStarred) {
                    handler.archiveRecord(noteToEDIT.getId());
                    finish();
                } else if(fromArchived) {
                    handler.deleteRecord(noteToEDIT.getId());
                    finish();
                }
            } else if (itemId == R.id.action_star) {
                isStarred = !isStarred;
                updateStarredStatus();
            } else if (itemId == RESTORE_NOTES) {
                handler.editNote(noteToEDIT.getId(),strSubject,strNote,isStarred,false);
                Intent backToMain = new Intent(AddNote.this,MainActivity.class);
                startActivity(backToMain);
                finish();
            } else if(itemId == DELETE_NOTE) {
                if(idToEdit != -1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(fromArchived) {
                                handler.deleteRecord(noteToEDIT.getId());
                                Intent a = new Intent(AddNote.this,MainActivity.class);
                                startActivity(a);
                                finish();
                            } else {
                                handler.archiveRecord(noteToEDIT.getId());
                                Intent a = new Intent(AddNote.this,MainActivity.class);
                                startActivity(a);
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            }


        return true;
    }
}

