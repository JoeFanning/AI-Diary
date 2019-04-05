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
 * Database for Weather
 * Created by joesp on 16/11/2017.
 */

class WeatherSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    static final String DATABASE_NAME = "WeatherRecordsDB";

    WeatherSQLiteHelper(Context context) {
        //use same database as DiaryEntrySQLLiteHelper no need to have another database.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create schema for database table
        String CREATE_WEATHER_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS WeatherRecords ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INT, " +
                "displayDate TEXT, " +
                "country TEXT, " +
                "area TEXT, " +
                "weatherDescription TEXT, " +
                "weatherImage TEXT, " +
                "temperature TEXT, " +
                "windSpeed TEXT, " +
                "pressure TEXT, " +
                "humidity TEXT, " +
                "visibility TEXT, " +
                "sunrise TEXT, " +
                "sunset TEXT)";
        // create weather records table
        db.execSQL(CREATE_WEATHER_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older weather records table if existed
        db.execSQL("DROP TABLE IF EXISTS WeatherRecords");
        // create fresh weather records table
        this.onCreate(db);
    }

    //Weather table name
    private static final String WEATHER_RECORDS = "WeatherRecords";
    //Weather Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DISPLAY_DATE = "displaydate";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_AREA = "area";
    private static final String KEY_WEATHER_DESCRIPTION = "weatherdescription";
    private static final String KEY_WEATHER_IMAGE = "weatherimage";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_WIND_SPEED = "windspeed";
    private static final String KEY_PRESSURE = "pressure";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_VISIBILITY = "visibility";
    private static final String KEY_SUNRISE = "sunrise";
    private static final String KEY_SUNSET = "sunset";

    private static final String[] COLUMNS = {KEY_ID, KEY_DATE, KEY_DISPLAY_DATE, KEY_COUNTRY, KEY_AREA, KEY_WEATHER_DESCRIPTION,
            KEY_WEATHER_IMAGE, KEY_TEMPERATURE, KEY_WIND_SPEED, KEY_PRESSURE, KEY_HUMIDITY, KEY_VISIBILITY,
            KEY_SUNRISE, KEY_SUNSET
    };

    // Add single weather row/tuple
    void addWeather(Weather weather) {
        Log.d("addWeather", weather.toString());
        // get reference to writable DB
        SQLiteDatabase db = WeatherSQLiteHelper.this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, weather.getDate());
        values.put(KEY_DISPLAY_DATE, weather.getDisplayDate());
        values.put(KEY_COUNTRY, weather.getCountry());
        values.put(KEY_AREA, weather.getArea());
        values.put(KEY_WEATHER_DESCRIPTION, weather.getWeatherDescription());
        values.put(KEY_WEATHER_IMAGE, weather.getWeatherImage());
        values.put(KEY_TEMPERATURE, weather.getTemperature());
        values.put(KEY_WIND_SPEED, weather.getWindSpeed());
        values.put(KEY_PRESSURE, weather.getPressure());
        values.put(KEY_HUMIDITY, weather.getHumidity());
        values.put(KEY_VISIBILITY, weather.getVisibility());
        values.put(KEY_SUNRISE, weather.getSunrise());
        values.put(KEY_SUNSET, weather.getSunset());

        // insert
        db.insert(WEATHER_RECORDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        //close
        db.close();

    }

    // Get single weather row/tuple
    Weather getWeather(int id) {
        //get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        //build query
        Cursor cursor =
                db.query(WEATHER_RECORDS, //table
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
        //build weather object
        Weather weather = new Weather();
        weather.setId(Integer.parseInt(cursor.getString(0)));
        weather.setDate(cursor.getInt(1));
        weather.setDisplayDate(cursor.getString(2));
        weather.setCountry(cursor.getString(3));
        weather.setArea(cursor.getString(4));
        weather.setWeatherDescription(cursor.getString(5));
        weather.setWeatherImage(cursor.getString(6));
        weather.setTemperature(cursor.getString(7));
        weather.setWindSpeed(cursor.getString(8));
        weather.setPressure(cursor.getString(9));
        weather.setHumidity(cursor.getString(10));
        weather.setVisibility(cursor.getString(11));
        weather.setSunrise(cursor.getString(12));
        weather.setSunset(cursor.getString(13));

        Log.d("getWeather(" + id + ")", weather.toString());
        //return weather entry
        return weather;
    }

    // Collect and return all weather entries
    List<Weather> getAllWeather() {
        List<Weather> weatherList = new LinkedList<Weather>();
        // build the query
        String query = "SELECT  * FROM " + WEATHER_RECORDS;
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // go over each row, build weatherList and add it to list
        Weather weather = null;
        if (cursor.moveToFirst()) {
            do {
                weather = new Weather();
                weather.setId(Integer.parseInt(cursor.getString(0)));
                weather.setDate(cursor.getInt(1));
                weather.setDisplayDate(cursor.getString(2));
                weather.setCountry(cursor.getString(3));
                weather.setArea(cursor.getString(4));
                weather.setWeatherDescription(cursor.getString(5));
                weather.setWeatherImage(cursor.getString(6));
                weather.setTemperature(cursor.getString(7));
                weather.setWindSpeed(cursor.getString(8));
                weather.setPressure(cursor.getString(9));
                weather.setHumidity(cursor.getString(10));
                weather.setVisibility(cursor.getString(11));
                weather.setSunrise(cursor.getString(12));
                weather.setSunset(cursor.getString(13));
                // Add weather entries to weatherList
                weatherList.add(weather);
            } while (cursor.moveToNext());
        }
        // return weatherList
        return weatherList;
    }

    // Updating single weather entry
    int updateWeather(Weather weather) {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("date", weather.getDate());
        values.put("displayDate", weather.getDisplayDate());
        values.put("country", weather.getCountry());
        values.put("area", weather.getArea());
        values.put("weatherDescription", weather.getWeatherDescription());
        values.put("weatherImage", weather.getWeatherImage());
        values.put("temperature", weather.getTemperature());
        values.put("windSpeed", weather.getWindSpeed());
        values.put("pressure", weather.getPressure());
        values.put("humidity", weather.getHumidity());
        values.put("visibility", weather.getVisibility());
        values.put("sunrise", weather.getSunrise());
        values.put("sunset", weather.getSunset());
        // updating row
        int i = db.update(WEATHER_RECORDS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(weather.getId())}); //selection args
        //close
        db.close();
        return i;
    }

    // Deleting single weather entry
    void deleteWeather(Weather weather) {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //delete
        db.delete(WEATHER_RECORDS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(weather.getId())});
        //close
        db.close();
        Log.d("deleteWeather", weather.toString());
    }

    // Deleting all weather entries
    void deleteAllWeatherEntries() {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM WeatherRecords");
        //close
        db.close();
    }
}

