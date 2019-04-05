package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class HistoricalDiaryEntryActivity extends AppCompatActivity {

    EditText edtTxtDiaryEntry;
    static CharSequence historical_DisplayDate = "historical_DisplayDate";
    int dateInDatabase;
    static int dateToFindInDataBase;
    static CharSequence textForDiaryEntry;
    private TextToSpeech tts;
    private boolean weather_Location_News_Data_In_Database = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_diary_entry);

        edtTxtDiaryEntry = (EditText) findViewById(R.id.txtViewDiaryEntry);
        final TextView displayDate = (TextView) findViewById(R.id.txtViewDisplayDate);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnOpen = (Button) findViewById(R.id.btnOpen);
        Button btnTextToSpeech = (Button) findViewById(R.id.btnSpeechToText);
        Button btnLocation = (Button) findViewById(R.id.btnLocation);
        Button btnWeather = (Button) findViewById(R.id.btnWeather);
        Button btnNews = (Button) findViewById(R.id.btnNews);
        Button btnWatsonAIAnalysis = (Button) findViewById(R.id.btnWatsonAIAnalysis);

        //Display a date to the layout if there is no data for this date in the database.
        //This 'historical_DisplayDate' is initialised in the date picker before entering this activity.
        displayDate.setText(historical_DisplayDate);

        //Display database data to the layout
        DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(HistoricalDiaryEntryActivity.this);
        List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
        for (int i = 0; i < allDiaryEntries.size(); ++i) {
            dateInDatabase = Integer.parseInt(allDiaryEntries.get(i).getDate());
            if (dateInDatabase == dateToFindInDataBase) {//global var dateToFindInDataBase is set in DatePickerActivity.java
                textForDiaryEntry = allDiaryEntries.get(i).getDiaryEntry();
                historical_DisplayDate = allDiaryEntries.get(i).getDisplayDate();
                displayDate.setText(historical_DisplayDate);
                edtTxtDiaryEntry.setText(textForDiaryEntry);
                break;
            } else {
                edtTxtDiaryEntry.setText("There was no diary entered for date: " + historical_DisplayDate);
                weather_Location_News_Data_In_Database = false;
            }
        }

        //Save edited historical diary. First check for empty text field, check for duplicates dates in the database.
        //check for user trying to save an entry with no history
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dont let user edit a historical diary with no database data
                if (weather_Location_News_Data_In_Database == false & dateInDatabase != dateToFindInDataBase) {
                    AlertDialog alertDialog = new AlertDialog.Builder(HistoricalDiaryEntryActivity.this).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("You cannot save a historical diary entry in the present\n\n because "
                            + "there is no previous weather, news or location data stored for this entry");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    //Check for saving empty/null text field
                    String ed_text = edtTxtDiaryEntry.getText().toString().trim();
                    if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                        AlertDialog alertDialog = new AlertDialog.Builder(HistoricalDiaryEntryActivity.this).create();
                        alertDialog.setTitle("Empty text area");
                        alertDialog.setMessage("Please enter text into the text field before saving");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                    //Delete duplicate diary entry data from database
                    DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(HistoricalDiaryEntryActivity.this);
                    List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
                    for (int i = 0; i < allDiaryEntries.size(); ++i) {
                        int iDate = Integer.parseInt(allDiaryEntries.get(i).getDate());
                        if (iDate == dateToFindInDataBase) {
                            db.deleteDiaryEntry(allDiaryEntries.get(i));
                        }
                    }

                    //Set the data to add to the database
                    //The weather, news and location can not be changed as they are historical
                    String diaryTextEntry = edtTxtDiaryEntry.getText().toString();
                    String displayDate = HistoricalDiaryEntryActivity.historical_DisplayDate.toString();
                    String idDate = String.valueOf(HistoricalDiaryEntryActivity.dateToFindInDataBase);
                    //Add the data to the database
                    DiaryEntry diaryEntry = new DiaryEntry();
                    diaryEntry.setDate(idDate);
                    diaryEntry.setDisplayDate(displayDate);
                    diaryEntry.setDiaryEntry(diaryTextEntry);
                    db.addDiaryEntry(diaryEntry);

                    Toast.makeText(HistoricalDiaryEntryActivity.this,
                            "New Diary Entry Saved for " + displayDate, Toast.LENGTH_LONG).show();


                    if (TESTINGActivity.turnOnTestingMode == true) {
                        //TESTING
                        WeatherSQLiteHelper wdb = new WeatherSQLiteHelper(HistoricalDiaryEntryActivity.this);
                        List<Weather> allWeatherEntries = wdb.getAllWeather();
                        NewsSQLiteHelper ndb = new NewsSQLiteHelper(HistoricalDiaryEntryActivity.this);
                        List<News> allNewsEntries = ndb.getAllNews();
                        LocationsSQLiteHelper ldb = new LocationsSQLiteHelper(HistoricalDiaryEntryActivity.this);
                        List<Locations> allLocationsEntries = ldb.getAllLocations();
                        if (!allDiaryEntries.isEmpty()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    HistoricalDiaryEntryActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("HistoricaDiaryEntry.java"
                                    + "\n\nNo of WEATHER entry table rows= " + allWeatherEntries.size()
                                    + "\nNo. of NEWS entry table rows= " + allNewsEntries.size()
                                    + "\nNo. of LOCATIONS entry table rows= " + allLocationsEntries.size()
                                    + "\n\nNo. of DIARYENTRY table rows = " + allDiaryEntries.size()
                                    + "\n\ndateToFindInDataBase =" + dateToFindInDataBase
                                    + "\nhistorical display date =" + historical_DisplayDate + "\n"
                                    + "\nRow 0 in diary entry table: " + allDiaryEntries.get(0).getDate()
                                    + "\n Diary text entry: " + allDiaryEntries.get(0).getDiaryEntry());
                            //  +" Display date: "+allDiaryEntries.get(0).getDisplayDate());
                            //                                    +"\nDate 1: "+allDiaryEntries.get(1).getDate()+" Diary entry: "+allDiaryEntries.get(1).getDiaryEntry()
                            //                                            +" Display date: "+allDiaryEntries.get(1).getDisplayDate()
                            //                                    +"\nDate 2: "+allDiaryEntries.get(2).getDate()+" Diary entry: "+allDiaryEntries.get(2).getDiaryEntry()
                            //                                            +" Display date: "+allDiaryEntries.get(2).getDisplayDate()
                            //                                    +"\nDate 3: "+allDiaryEntries.get(3).getDate()+" Diary entry: "+allDiaryEntries.get(3).getDiaryEntry()
                            //                                            +" Display date: "+allDiaryEntries.get(3).getDisplayDate());

                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    HistoricalDiaryEntryActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Number of rows in diary entry table = "
                                    + allDiaryEntries.size());
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                    //END TESTING

                }//end of else
            }
        });

        //Button to open the date picker to select a histotical date
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoricalDiaryEntryActivity.this, DatePickerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        //Button to activate popup window with location and Google Map
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoricalDiaryEntryActivity.this,
                        HistoricalLocationPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to activate popup window with weather API
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {//The layout method in WeatherDownLoadAPIData is seperate from the
                // jSON(onPostExecute method) and URL connection(doInBackground)
                //This is so the jSON(onPostExecute method) and URL connection(doInBackground) can be used for collecting data
                //form the Weather API for the database.
                //if this button is pressed the layout will be run
                //otherwise it won't.
                //Don't run it in this case because todays weather layout is not to be set, a historical weather layout is to be set
                //WeatherDownloadAPIData.runTheSetLayoutMethod = false;

                startActivity(new Intent(HistoricalDiaryEntryActivity.this, HistoricalWeatherPopupWindow.class));
            }
        });

        //Button to activate popup window with news API
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoricalDiaryEntryActivity.this,
                        HistoricalNewsPopupWindow.class));
            }
        });

        //Button to open WatsonAIAnalysis activity
        btnWatsonAIAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoricalDiaryEntryActivity.this, WatsonAnalysisActivity.class));
            }
        });


        //Text to speech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                }
            }
        });

        btnTextToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = edtTxtDiaryEntry.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }//end of onCreate

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}