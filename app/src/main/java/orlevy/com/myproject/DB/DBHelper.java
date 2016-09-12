package orlevy.com.myproject.DB;

/**
 * Created by Or.levy on 07/09/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String cmd ="CREATE TABLE " + CONSTANTS.DB_TABLE_NAME + "(" + CONSTANTS.DB_ID + "INTEGER PRIMARY KEY," + CONSTANTS.DB_SUBJECT + "TEXT" + CONSTANTS.DB_NOTE + "TEXT)";
        String cmd ="CREATE TABLE Note (_id INTEGER PRIMARY KEY, subject TEXT, note TEXT,starred INTEGER,archived INTEGER)";
        try {
            sqLiteDatabase.execSQL(cmd);
        } catch (SQLiteException e) {
            e.getMessage();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
