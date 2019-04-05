package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class DatePickerActivity extends AppCompatActivity {

    static int dateToFindInDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        final DatePicker simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);
        final int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case .9 by .7
        getWindow().setLayout(width / 10 * 9, height / 10 * 7);

        //Button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //How to set an event listener on the datepicker when a date is picked
        //Research link: https://stackoverflow.com/questions/2051153/android-ondatechangedlistener-how-do-you-set-this
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        simpleDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        //Use the date as an id for fetching from the database
                        dateToFindInDatabase = Integer.parseInt(dayOfMonth + "" + (month + 1) + "" + year);
                        Toast.makeText(DatePickerActivity.this,
                                "Searching for date: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_LONG).show();


                        DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(DatePickerActivity.this);
                        //Set up a List to collect the rows/tuples in the database table
                        List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();

                        //Before using the database check if its empty
                        if (allDiaryEntries.isEmpty()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(DatePickerActivity.this).create();
                            alertDialog.setTitle("You have no diary entries to look up");
                            alertDialog.setMessage("You have not entered any diary entries yet."
                                    +"\n\nPlease enter your first diary entry. Please also make sure your FIRST diary entry "
                                    +"has at least 100 words or Watson Personality Insights will not have enough text to analyse. "
                                    +"This is just for your first diary entry after that for all other diary entries you can enter"
                                    +" as little or as many words as you like.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } else {
                            //Iterate through the database table rows and if the date matches the date picked set the
                            //HistoricalDiaryEntryActivity.java display date and set HistoricalDiaryEntryActivity.java
                            //dateToFindInDataBase date.
                            boolean databaseDateFound = false;
                            for (int i = 0; i < allDiaryEntries.size(); ++i) {
                                int iDate = Integer.parseInt(allDiaryEntries.get(i).getDate());
                                //Before using the database check if its empty
                                if (allDiaryEntries.isEmpty()) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(DatePickerActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("The database is empty, please enter a diary entry to save in the database");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    break;
                                }
                                //Set the HistoricalDiaryEntryActivity date and diary entry from the database data
                                //and set the HistoricalDiaryEntryActivity dateToFindInDatabase variable
                                else if (iDate == dateToFindInDatabase) {
                                    HistoricalDiaryEntryActivity.dateToFindInDataBase = dateToFindInDatabase;
                                    HistoricalDiaryEntryActivity.historical_DisplayDate = allDiaryEntries.get(i).getDisplayDate();
                                    HistoricalDiaryEntryActivity.textForDiaryEntry = allDiaryEntries.get(i).getDiaryEntry();
                                    databaseDateFound = true;
                                } else {
                                    if (databaseDateFound == false) {
                                        //Set the HistoricalDiaryEntryActivity data for a diary entry with NO! record in the database
                                        HistoricalDiaryEntryActivity.historical_DisplayDate = dayOfMonth + " - " + (month + 1) + " - " + year;
                                        HistoricalDiaryEntryActivity.dateToFindInDataBase = dateToFindInDatabase;
                                        HistoricalDiaryEntryActivity.textForDiaryEntry = "There is no diary entry for: " + dayOfMonth + " - " + (month + 1) + " - " + year;
                                    }
                                    databaseDateFound = false;
                                }
                            }
                            Intent intent = new Intent(DatePickerActivity.this, HistoricalDiaryEntryActivity.class);
                            startActivity(intent);

                        }//end of else of if that checking for empty database
                    }
                });
    }
}
