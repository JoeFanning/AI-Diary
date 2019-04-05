package com.example.joesp.aidiary;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database for DiaryEntry entries
 * Created by joesp on 14/11/17.
 */
class DiaryEntrySQLLiteHelper extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 1;
    // Database Name
    static final String DATABASE_NAME = "DiaryEntryRecordsDB";

    DiaryEntrySQLLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create schema for database table
        String CREATE_DIARY_ENTRY_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS DiaryEntryRecords ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "displayDate TEXT, " +
                "diaryEntry TEXT)";
        // create diary entry records table
        db.execSQL(CREATE_DIARY_ENTRY_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older diary entry records table if existed
        db.execSQL("DROP TABLE IF EXISTS DiaryEntryRecords");
        // create fresh diary entry records table
        this.onCreate(db);
    }

    // Diary entry table name
    public static final String DIARY_ENTRY_RECORDS = "DiaryEntryRecords";

    // Diary entry Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DISPLAY_DATE = "displaydate";
    private static final String KEY_DIARY_ENTRY = "diaryentry";

    private static final String[] COLUMNS = {KEY_ID, KEY_DATE, KEY_DISPLAY_DATE, KEY_DIARY_ENTRY};

    void addDiaryEntry(DiaryEntry diaryEntry) {
        Log.d("addDiaryEntry", diaryEntry.toString());
        // get reference to writable DB
        SQLiteDatabase db = DiaryEntrySQLLiteHelper.this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, diaryEntry.getDate());
        values.put(KEY_DISPLAY_DATE, diaryEntry.getDisplayDate());
        values.put(KEY_DIARY_ENTRY, diaryEntry.getDiaryEntry());
        // insert
        db.insert(DIARY_ENTRY_RECORDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        //close
        db.close();
    }

    //Return single diary entry
    public DiaryEntry getDiaryEntry(int id) {
        //get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        //build query
        Cursor cursor =
                db.query(DIARY_ENTRY_RECORDS, //table
                        COLUMNS, //column names
                        " id = ?", // selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, //group by
                        null, //having
                        null, //order by
                        null); //limit
        //if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        //build DiaryEntry object
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setId(Integer.parseInt(cursor.getString(0)));
        diaryEntry.setDate(cursor.getString(1));
        diaryEntry.setDisplayDate(cursor.getString(2));
        diaryEntry.setDiaryEntry(cursor.getString(3));
        //return diary entry
        return diaryEntry;
    }

    // Collect and return all diary entries
    List<DiaryEntry> getAllDiaryEntries() {
        List<DiaryEntry> diaryEntries = new LinkedList<DiaryEntry>();
        // build the query
        String query = "SELECT  * FROM " + DIARY_ENTRY_RECORDS;
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // go over each row, build diary entry and add it to list
        DiaryEntry diaryEntry = null;
        if (cursor.moveToFirst()) {
            do {
                diaryEntry = new DiaryEntry();
                diaryEntry.setId(Integer.parseInt(cursor.getString(0)));
                diaryEntry.setDate(cursor.getString(1));
                diaryEntry.setDisplayDate(cursor.getString(2));
                diaryEntry.setDiaryEntry(cursor.getString(3));
                // Add diary entries to diaryEntries
                diaryEntries.add(diaryEntry);
            } while (cursor.moveToNext());
        }
        // return diaryEntries
        return diaryEntries;
    }

    // Updating single diary entry
    public int updateDiaryEntry(DiaryEntry diaryEntry) {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("date", diaryEntry.getDate());
        values.put("displayDate", diaryEntry.getDisplayDate());
        values.put("diaryEntry", diaryEntry.getDiaryEntry());
        // updating row
        int i = db.update(DIARY_ENTRY_RECORDS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(diaryEntry.getId())}); //selection args
        //close
        db.close();
        return i;
    }

    // Deleting single diaryEntry
    void deleteDiaryEntry(DiaryEntry diaryEntry) {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //delete
        db.delete(DIARY_ENTRY_RECORDS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(diaryEntry.getId())});
        //close
        db.close();
        Log.d("deleteDiaryEntry", diaryEntry.toString());
    }

    // Deleting all diary entries
    public void deleteAllDiaryEntries() {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM DiaryEntryRecords");
        //close
        db.close();
    }
}
