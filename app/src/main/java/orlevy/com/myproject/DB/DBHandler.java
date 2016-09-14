package orlevy.com.myproject.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import orlevy.com.myproject.CONSTANTS;
import orlevy.com.myproject.Class.Note;

public class DBHandler {
    private DBHelper helper;

    public DBHandler(Context context) {
        helper = new DBHelper(context, "Note.db", null, 2);
    }

    public void addNote(String subject, String note,boolean starred,boolean Archived) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //  db.execSQL("Insert into book values('pinokio','jepeto','100')"); NOT recommended
            ContentValues values = new ContentValues();
            values.put(CONSTANTS.DB_SUBJECT, subject);
            values.put(CONSTANTS.DB_NOTE, note);
            values.put(CONSTANTS.DB_STARRED,starToInt(starred));
            values.put(CONSTANTS.DB_ARCHIVED,0);
            db.insert(CONSTANTS.DB_TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

    }




    public void removeAll() {
        SQLiteDatabase db = helper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        try {
            db.delete(CONSTANTS.DB_TABLE_NAME, CONSTANTS.DB_ARCHIVED+"=1", null);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }


    }
    public void archiveAll() {
        SQLiteDatabase db = helper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        try {

//            db.update(CONSTANTS.DB_TABLE_NAME, CONSTANTS.DB_ARCHIVED+"=1", null);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }

    }

    public Note getItem(int id) {
        Boolean isStarred = false;
        Boolean isArchived = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        Note temp = null;
        try {
            cursor = db.query(CONSTANTS.DB_TABLE_NAME,null,CONSTANTS.DB_ID+"=?",new String[]{Integer.toString(id)},null,null,null,null);
            while (cursor.moveToNext()) {
                int getID = cursor.getInt(0);
                String subject = cursor.getString(1);
                String note = cursor.getString(2);
                isStarred = intToBool(cursor.getInt(3));
                isArchived = intToBool(cursor.getInt(4));
                temp = new Note(getID,subject,note,isStarred,isArchived);
            }
        } catch (SQLiteException e) {
            e.getMessage();
        }

        return temp;

    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        Boolean isStarred;
        Boolean isArchived;
        try {
            cursor = db.query(CONSTANTS.DB_TABLE_NAME,null,CONSTANTS.DB_ARCHIVED+"=0",null,null,null,null);
//            cursor = db.query(CONSTANTS.DB_TABLE_NAME, null, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String subject = cursor.getString(1);
                String note = cursor.getString(2);
                isStarred = intToBool(cursor.getInt(3));
                isArchived = intToBool(cursor.getInt(4));
                list.add(new Note(id,subject, note,isStarred,isArchived));
            }
        } catch (SQLiteException e) {
            e.getMessage();
        }
        return list;
    }

    public void editNote(int idToEdit, String strSubject, String strNote, boolean isStarred,boolean isArchived) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues args = new ContentValues();
            args.put(CONSTANTS.DB_SUBJECT, strSubject);
            args.put(CONSTANTS.DB_NOTE, strNote);
            args.put(CONSTANTS.DB_STARRED, starToInt(isStarred));
            args.put(CONSTANTS.DB_ARCHIVED,isArchived);
            db.update("Note", args, "_id" + "=" + idToEdit, null);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()) {
                db.close();
            }
        }
        }
    public void deleteRecord(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.delete(CONSTANTS.DB_TABLE_NAME,"_id=?",new String[]{Integer.toString(id)});
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()){
                db.close();
            }
        }
    }
    public void archiveRecord(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues archived = new ContentValues();
            archived.put(CONSTANTS.DB_SUBJECT, note.getSubject());
            archived.put(CONSTANTS.DB_NOTE, note.getNote());
            archived.put(CONSTANTS.DB_STARRED, 0); // if note was archived - star will automatically remove
            archived.put(CONSTANTS.DB_ARCHIVED,1);
            db.update(CONSTANTS.DB_TABLE_NAME,archived,CONSTANTS.DB_ID + "=?",new String[]{Integer.toString(note.getId())});
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()){
                db.close();
            }
        }
    }

    public ArrayList<Note> getStarred() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor =  db.query(CONSTANTS.DB_TABLE_NAME,null,CONSTANTS.DB_STARRED + "=?",new String[]{"1"},null,null,null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String subject = cursor.getString(1);
                String note = cursor.getString(2);
                Boolean isStarred = intToBool(cursor.getInt(3));
                Boolean isArchived = intToBool(cursor.getInt(4));
                list.add(new Note(id, subject, note, isStarred,isArchived));
            }
        } catch (SQLiteException e) {
            e.getMessage();
        }
        return list;
    }
    public ArrayList<Note> getArchived() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor =  db.query(CONSTANTS.DB_TABLE_NAME,null,CONSTANTS.DB_ARCHIVED + "=?",new String[]{"1"},null,null,null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String subject = cursor.getString(1);
                String note = cursor.getString(2);
                Boolean isStarred = intToBool(cursor.getInt(3));
                Boolean isArchived = intToBool(cursor.getInt(4));
                list.add(new Note(id, subject, note, isStarred,isArchived));
            }
        } catch (SQLiteException e) {
            e.getMessage();
        }
        return list;
    }

    public boolean intToBool(int i) {
        if (i==1) {
            return true;
        } else {
            return false;
        }
    }
    public int starToInt(Boolean a) {
        if (a) {
            return 1;
        } else {
            return 0;
        }
    }

    public void star(int id,Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues args = new ContentValues();
            args.put(CONSTANTS.DB_SUBJECT, note.getSubject());
            args.put(CONSTANTS.DB_NOTE, note.getNote());
            args.put(CONSTANTS.DB_STARRED, starToInt(!(note.isStarred())));
            db.update("Note", args, "_id" + "=" + id, null);
        } catch (SQLiteException e) {
            e.getMessage();
        } finally {
            if(db.isOpen()){
                db.close();
            }
        }
    }
}


