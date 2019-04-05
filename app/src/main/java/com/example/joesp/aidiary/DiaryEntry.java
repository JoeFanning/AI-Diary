package com.example.joesp.aidiary;

/**
 * Diary entry class for the database
 * Created by Joe Fanning on 14/11/2017.
 */

public class DiaryEntry {

    private int id;
    private String date;
    private String displayDate;
    private String diaryEntry;

    DiaryEntry() {
    }

    DiaryEntry(String date, String diaryEntry, String displayDate) {
        super();
        this.date = date;
        this.diaryEntry = diaryEntry;
        this.displayDate = displayDate;
    }

    @Override
    public String toString() {
        return " Diary id: " + id + "\n Date: " + date + " Display date: " + displayDate + "\n Diary entry: " + diaryEntry;
    }

    //getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    String getDiaryEntry() {
        return diaryEntry;
    }

    void setDiaryEntry(String diaryEntry) {
        this.diaryEntry = diaryEntry;
    }
}



