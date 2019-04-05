package com.example.joesp.aidiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Database for Locations data
 * Created by joesp on 26/11/2017.
 */

class LocationsSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 3;
    // Database Name
    static final String DATABASE_NAME = "LocationsRecordsDB";

    LocationsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create schema for database table
        String CREATE_LOCATIONS_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS LocationsRecords ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INT, " +
                "longitude TEXT, " +
                "latitude TEXT, " +
                "lastLocationUpdateTime TEXT )";
        // create locations records table
        db.execSQL(CREATE_LOCATIONS_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older locations records table if existed
        db.execSQL("DROP TABLE IF EXISTS LocationsRecords");
        // create fresh locations records table
        this.onCreate(db);
    }

    // News table name
    private static final String LOCATIONS_RECORDS = "LocationsRecords";

    // News table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LAST_LOCATION_UPDATE_TIME = "lastlocationupdatetime";

    private static final String[] COLUMNS = {KEY_ID, KEY_DATE, KEY_LONGITUDE, KEY_LATITUDE, KEY_LAST_LOCATION_UPDATE_TIME};

    void addLocation(Locations location) {
        Log.d("addLocation", location.toString());
        // get reference to writable DB
        SQLiteDatabase db = LocationsSQLiteHelper.this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, location.getDate());
        values.put(KEY_LONGITUDE, location.getLongitude());
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LAST_LOCATION_UPDATE_TIME, location.getLastLocationUpdateTime());
        // insert
        db.insert(LOCATIONS_RECORDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        //close
        db.close();
    }

    public Locations getLocation(int id) {
        //get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        //build query
        Cursor cursor =
                db.query(LOCATIONS_RECORDS, //table
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
        //build location object
        Locations location = new Locations();
        location.setId(Integer.parseInt(cursor.getString(0)));
        location.setDate(cursor.getInt(1));
        location.setLongitude(cursor.getString(2));
        location.setLatitude(cursor.getString(3));
        location.setLastLocationUpdateTime(cursor.getString(4));
        Log.d("getLocationsEntry(" + id + ")", location.toString());
        //return location entry
        return location;
    }


    // Collect and return all location entries
    List<Locations> getAllLocations() {
        List<Locations> locationEntries = new LinkedList<Locations>();
        // build the query
        String query = "SELECT  * FROM " + LOCATIONS_RECORDS;
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // go over each row, build news entries and add it to list
        Locations location = null;
        if (cursor.moveToFirst()) {
            do {
                location = new Locations();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setDate(cursor.getInt(1));
                location.setLongitude(cursor.getString(2));
                location.setLatitude(cursor.getString(3));
                location.setLastLocationUpdateTime(cursor.getString(4));
                // Add location entries to locationEntries
                locationEntries.add(location);
            } while (cursor.moveToNext());
        }
        // return news
        return locationEntries;
    }

    // Updating single location entry
    public int updateSingleLocation(Locations locationEntry) {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("date", locationEntry.getDate());
        values.put("longitude", locationEntry.getLongitude());
        values.put("latitude", locationEntry.getLatitude());
        values.put("lastLocationUpdateTime", locationEntry.getLastLocationUpdateTime());
        // updating row
        int i = db.update(LOCATIONS_RECORDS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(locationEntry.getId())}); //selection args
        //close
        db.close();
        return i;
    }

    // Deleting single news row/tuple
    void deleteALocationEntry(Locations locationEntry) {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //delete
        db.delete(LOCATIONS_RECORDS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(locationEntry.getId())});
        //close
        db.close();
        Log.d("deleteLocationEntry", locationEntry.toString());
    }

    // Deleting all news entries
    public void deleteAllNewsEntries() {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM LocationsRecords");
        //close
        db.close();
    }
}

