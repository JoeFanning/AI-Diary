package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MainDate = "MainDate";
    Date dateObject;
    Date dateObj;
    static int date;
    static boolean welcomeNoteHasRun;
    static String displayDate = "";//use for all app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtViewCurrentDate = (TextView) findViewById(R.id.txtViewCurrentDate);
        Button btnWriteDiaryEntry = (Button) findViewById(R.id.btnWriteDiary);
        Button btnVideoDiary = (Button) findViewById(R.id.btnVideoDiary);
        Button btnFindDiary = (Button) findViewById(R.id.btnFindDiary);

        //Welcome note to the user
        if(welcomeNoteHasRun== false) {
            AlertDialog alrtDialog = new AlertDialog.Builder(MainActivity.this).create();
            alrtDialog.setTitle("Hi ! Welcome to A.I. Diary");
            alrtDialog.setMessage("Just a few notes before starting. \n\nIf you are using Watson AI Analytics, "
                    +"please make sure you enter your first diary entry so Watson has a diary entry to analyse. "
                    +"Please also make sure your FIRST diary entry has at least 100 words or Watson Personality "
                    +"Insights will not have enough text to analyse. This is just for your first diary entry after "
                    +"that for all other diary entries you can enter as little or as many words as you like."
                    +"\n\nNow press the WRITE DIARY button when you close this message, and enter your first diary "
                    +"entry with at least 100 words.\n\nGOOD LUCK! AND HAVE FUN ANALYSING.");
            alrtDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alrtDialog.show();
            welcomeNoteHasRun =true;
        }

        //Go to DiaryEntryActivity
        btnWriteDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiaryEntryActivity.class);
                startActivity(intent);
            }
        });

        //Go to VideoDiaryEntryActivity, this activity is currently not in use and will be developed at a later stage
        //it is currently being used as a TESTING activity
        btnVideoDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TESTINGActivity.class);//VideoDiaryEntryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        //Open the date picker
        btnFindDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DatePickerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        //Set the display date
        dateObject = new Date();
        displayDate = dateObject.toString().substring(0, 10) + " " + dateObject.toString().substring(30);
        txtViewCurrentDate.setText(displayDate);
        Log.d(MainDate, "--" + MainActivity.displayDate + "--");

        //Set the date as an id for all the tables in the database so location, weather and news can be matched to a diary entry.
        //https://stackoverflow.com/questions/5046771/how-to-get-todays-date
        // https://stackoverflow.com/questions/22989840/get-day-month-and-year-separately-using-simpledateformat
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm", Locale.ENGLISH);
        //Set the date as an id for all the tables in the database so location, weather and news can be matched to a diary entry.
        try {
            dateObj = format.parse("JAN 13,2014  09:15");
            Calendar myCal = new GregorianCalendar();
            myCal.setTime(dateObj = new Date());
            String day = "" + myCal.get(Calendar.DAY_OF_MONTH);
            String month = "" + (myCal.get(Calendar.MONTH) + 1);
            String year = "" + myCal.get(Calendar.YEAR);
            date = Integer.parseInt(day + "" + month + "" + year);

            Toast.makeText(MainActivity.this,
                     "Date: "+displayDate, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}