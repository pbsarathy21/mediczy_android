package app.mediczy_com.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static app.mediczy_com.utility.Bluetooth.TABLE_CHAT_INITAL;
import static app.mediczy_com.utility.Bluetooth.TABLE_POSTION;


public class Dbhelper extends SQLiteOpenHelper {


    long result;
    // Database Version
    private static final int DATABASE_VERSION = 1;


    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public Dbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //sessionForRepeatValue = new SessionForRepeatValue(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        //create sign up table
        db.execSQL(Bluetooth.CREATE_TABLE_CHAT_INITAIL);

        db.execSQL(Bluetooth.CREATE_TABLE_POSITION);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_INITAL);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTION);

        onCreate(db);
    }

    public Cursor getTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tablename, null);
        return res;
    }


    public long insert_chat_in(String c_sym_id, String c_comon_name, String c_choice_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Bluetooth.KEY_SYMPTON_ID, c_sym_id);
        contentValues.put(Bluetooth.KEY_CHAT_COMMON_NAME, c_comon_name);
        contentValues.put(Bluetooth.KEY_CHAT_CHOICE_ID, c_choice_id);
        long data = db.insert(TABLE_CHAT_INITAL, null, contentValues);
        db.close();
        return data;
    }

    public boolean update_chat_in(String id, String c_sym_id, String c_comon_name, String c_choice_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Bluetooth.KEY_SYMPTON_ID, c_sym_id);
        contentValues.put(Bluetooth.KEY_CHAT_COMMON_NAME, c_comon_name);
        contentValues.put(Bluetooth.KEY_CHAT_CHOICE_ID, c_choice_id);
        db.update(TABLE_CHAT_INITAL, contentValues, Bluetooth.COLUMN_ID + "=" + id, null);
        return true;
    }


    public long insert_Message(String Message_postion, String message_text) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Bluetooth.KEY_POSSION, Message_postion);
        contentValues.put(Bluetooth.KEY_MESSAGE, message_text);
        long data = db.insert(TABLE_POSTION, null, contentValues);
        db.close();
        return data;
    }

    public boolean update_Message(String id, String Message_postion, String message_text) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Bluetooth.KEY_POSSION, Message_postion);
        contentValues.put(Bluetooth.KEY_MESSAGE, message_text);
        db.update(TABLE_POSTION, contentValues, Bluetooth.COLUMN_ID + "=" + id, null);
        return true;
    }


    public List<Bluetooth> getAllNotes() {
        List<Bluetooth> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT_INITAL + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";


       /* // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT_INITAL + " GROUP BY " + Bluetooth.COLUMN_TIME_CAL + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";
*/
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bluetooth note = new Bluetooth();
                note.setId(cursor.getInt(cursor.getColumnIndex(Bluetooth.COLUMN_ID)));
                note.setSymptom_id(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_SYMPTON_ID)));
                note.setCommon_name(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_CHAT_COMMON_NAME)));
                note.setChoice_id(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_CHAT_CHOICE_ID)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }











    public List<Bluetooth> get_Mess() {
        List<Bluetooth> notes = new ArrayList<>();



        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POSTION + " ORDER BY " +
                Bluetooth.COLUMN_ID + " DESC ";

       /* String selectquery_date = "SELECT * FROM " + TABLE_POSTION + " WHERE " +
                Bluetooth.COLUMN_DATE + "='" + " 2018-07-06 " + "'";*/




        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bluetooth note = new Bluetooth();
                note.setId(cursor.getInt(cursor.getColumnIndex(Bluetooth.COLUMN_ID)));
                note.setMess_pos(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_POSSION)));
                note.setMess_tex(cursor.getString(cursor.getColumnIndex(Bluetooth.KEY_MESSAGE)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }



    public void deleteAllRecord()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_POSTION);

    }
    public void deleteNote(Bluetooth note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSTION, Bluetooth.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
