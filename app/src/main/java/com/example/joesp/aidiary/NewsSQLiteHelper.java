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
 * Database for News data
 * Created by joesp on 22/11/2017.
 */

class NewsSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 2;
    // Database Name
    static final String DATABASE_NAME = "NewsRecordsDB";

    NewsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create schema for database table
        String CREATE_NEWS_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS NewsRecords ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INT, " +
                "displayDate TEXT, " +
                "articleTitle1 TEXT, " +
                "articleTitle2 TEXT, " +
                "articleTitle3 TEXT, " +
                "articleTitle4 TEXT, " +
                "articleTitle5 TEXT, " +
                "articleDescription1 TEXT, " +
                "articleDescription2 TEXT, " +
                "articleDescription3 TEXT, " +
                "articleDescription4 TEXT, " +
                "articleDescription5 TEXT, " +
                "articleImage1 TEXT, " +
                "articleImage2 TEXT, " +
                "articleImage3 TEXT, " +
                "articleImage4 TEXT, " +
                "articleImage5 TEXT)";
        // Create news records table
        db.execSQL(CREATE_NEWS_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older news records table if existed
        db.execSQL("DROP TABLE IF EXISTS NewsRecords");
        // Create fresh news records table
        this.onCreate(db);
    }

    // News table name
    private static final String NEWS_RECORDS = "NewsRecords";
    // News table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DISPLAY_DATE = "displaydate";
    private static final String KEY_ARTICLE_TITLE1 = "articletitle1";
    private static final String KEY_ARTICLE_TITLE2 = "articletitle2";
    private static final String KEY_ARTICLE_TITLE3 = "articletitle3";
    private static final String KEY_ARTICLE_TITLE4 = "articletitle4";
    private static final String KEY_ARTICLE_TITLE5 = "articletitle5";
    private static final String KEY_ARTICLE_DESCRIPTION1 = "articledescription1";
    private static final String KEY_ARTICLE_DESCRIPTION2 = "articledescription2";
    private static final String KEY_ARTICLE_DESCRIPTION3 = "articledescription3";
    private static final String KEY_ARTICLE_DESCRIPTION4 = "articledescription4";
    private static final String KEY_ARTICLE_DESCRIPTION5 = "articledescription5";
    private static final String KEY_ARTICLE_IMAGE1 = "articleimage1";
    private static final String KEY_ARTICLE_IMAGE2 = "articleimage2";
    private static final String KEY_ARTICLE_IMAGE3 = "articleimage3";
    private static final String KEY_ARTICLE_IMAGE4 = "articleimage4";
    private static final String KEY_ARTICLE_IMAGE5 = "articleimage5";

    private static final String[] COLUMNS = {
            KEY_ID, KEY_DATE, KEY_DISPLAY_DATE,
            KEY_ARTICLE_TITLE1, KEY_ARTICLE_TITLE2, KEY_ARTICLE_TITLE3, KEY_ARTICLE_TITLE4, KEY_ARTICLE_TITLE5,
            KEY_ARTICLE_DESCRIPTION1, KEY_ARTICLE_DESCRIPTION2, KEY_ARTICLE_DESCRIPTION3, KEY_ARTICLE_DESCRIPTION4,
            KEY_ARTICLE_DESCRIPTION5,
            KEY_ARTICLE_IMAGE1, KEY_ARTICLE_IMAGE2, KEY_ARTICLE_IMAGE3, KEY_ARTICLE_IMAGE4, KEY_ARTICLE_IMAGE5};

    //Add single News entry
    void addNews(News news) {
        // get reference to writable DB
        SQLiteDatabase db = NewsSQLiteHelper.this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, news.getDate());
        values.put(KEY_DISPLAY_DATE, news.getDisplayDate());
        values.put(KEY_ARTICLE_TITLE1, news.getArticleTitle1());
        values.put(KEY_ARTICLE_TITLE2, news.getArticleTitle2());
        values.put(KEY_ARTICLE_TITLE3, news.getArticleTitle3());
        values.put(KEY_ARTICLE_TITLE4, news.getArticleTitle4());
        values.put(KEY_ARTICLE_TITLE5, news.getArticleTitle5());
        values.put(KEY_ARTICLE_DESCRIPTION1, news.getArticleDescription1());
        values.put(KEY_ARTICLE_DESCRIPTION2, news.getArticleDescription2());
        values.put(KEY_ARTICLE_DESCRIPTION3, news.getArticleDescription3());
        values.put(KEY_ARTICLE_DESCRIPTION4, news.getArticleDescription4());
        values.put(KEY_ARTICLE_DESCRIPTION5, news.getArticleDescription5());
        values.put(KEY_ARTICLE_IMAGE1, news.getArticleImage1());
        values.put(KEY_ARTICLE_IMAGE2, news.getArticleImage2());
        values.put(KEY_ARTICLE_IMAGE3, news.getArticleImage3());
        values.put(KEY_ARTICLE_IMAGE4, news.getArticleImage4());
        values.put(KEY_ARTICLE_IMAGE5, news.getArticleImage5());
        // insert
        db.insert(NEWS_RECORDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        //close
        db.close();

    }

    public News getNews(int id) {
        //Get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        //build query
        Cursor cursor =
                db.query(NEWS_RECORDS, //table
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
        //build news object
        News news = new News();
        news.setId(Integer.parseInt(cursor.getString(0)));
        news.setDate(cursor.getInt(1));
        news.setDisplayDate(cursor.getString(2));
        news.setArticleTitle1(cursor.getString(3));
        news.setArticleTitle2(cursor.getString(4));
        news.setArticleTitle3(cursor.getString(5));
        news.setArticleTitle4(cursor.getString(6));
        news.setArticleTitle5(cursor.getString(7));
        news.setArticleDescription1(cursor.getString(8));
        news.setArticleDescription2(cursor.getString(9));
        news.setArticleDescription3(cursor.getString(10));
        news.setArticleDescription4(cursor.getString(11));
        news.setArticleDescription5(cursor.getString(12));
        news.setArticleImage1(cursor.getString(13));
        news.setArticleImage2(cursor.getString(14));
        news.setArticleImage3(cursor.getString(15));
        news.setArticleImage4(cursor.getString(16));
        news.setArticleImage5(cursor.getString(17));
        Log.d("getNewsEntry(" + id + ")", news.toString());
        //return news entry
        return news;
    }


    // Collect and return all news entries
    List<News> getAllNews() {
        List<News> newsEntries = new LinkedList<News>();
        // build the query
        String query = "SELECT  * FROM " + NEWS_RECORDS;
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // go over each row, build news entries and add it to list
        News news = null;
        if (cursor.moveToFirst()) {
            do {
                news = new News();
                news.setId(Integer.parseInt(cursor.getString(0)));
                news.setDate(cursor.getInt(1));
                news.setDisplayDate(cursor.getString(2));
                news.setArticleTitle1(cursor.getString(3));
                news.setArticleTitle2(cursor.getString(4));
                news.setArticleTitle3(cursor.getString(5));
                news.setArticleTitle4(cursor.getString(6));
                news.setArticleTitle5(cursor.getString(7));
                news.setArticleDescription1(cursor.getString(8));
                news.setArticleDescription2(cursor.getString(9));
                news.setArticleDescription3(cursor.getString(10));
                news.setArticleDescription4(cursor.getString(11));
                news.setArticleDescription5(cursor.getString(12));
                news.setArticleImage1(cursor.getString(13));
                news.setArticleImage2(cursor.getString(14));
                news.setArticleImage3(cursor.getString(15));
                news.setArticleImage4(cursor.getString(16));
                news.setArticleImage5(cursor.getString(17));
                // Add news entries to newsEntries
                newsEntries.add(news);
            } while (cursor.moveToNext());
        }
        // return news
        return newsEntries;
    }

    // Updating single news entry
    public int updateNews(News newsEntry) {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("date", newsEntry.getDate());
        values.put("displayDate", newsEntry.getDisplayDate());
        values.put("articleTitle1", newsEntry.getArticleTitle1());
        values.put("articleTitle2", newsEntry.getArticleTitle2());
        values.put("articleTitle3", newsEntry.getArticleTitle3());
        values.put("articleTitle4", newsEntry.getArticleTitle4());
        values.put("articleTitle5", newsEntry.getArticleTitle5());
        values.put("articleDescription1", newsEntry.getArticleDescription1());
        values.put("articleDescription2", newsEntry.getArticleDescription2());
        values.put("articleDescription3", newsEntry.getArticleDescription3());
        values.put("articleDescription4", newsEntry.getArticleDescription4());
        values.put("articleDescription5", newsEntry.getArticleDescription5());
        values.put("articleImage1", newsEntry.getArticleImage1());
        values.put("articleImage2", newsEntry.getArticleImage2());
        values.put("articleImage3", newsEntry.getArticleImage3());
        values.put("articleImage4", newsEntry.getArticleImage4());
        values.put("articleImage5", newsEntry.getArticleImage5());
        // updating row
        int i = db.update(NEWS_RECORDS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(newsEntry.getId())}); //selection args
        //Close
        db.close();
        return i;
    }

    // Deleting single news row/tuple
    void deleteNewsEntry(News newsEntry) {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //delete
        db.delete(NEWS_RECORDS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(newsEntry.getId())});
        //close
        db.close();
        Log.d("deleteNewsEntry", newsEntry.toString());
    }

    // Deleting all news entries
    public void deleteAllNewsEntries() {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM NewsRecords");
        //close
        db.close();
    }
}
