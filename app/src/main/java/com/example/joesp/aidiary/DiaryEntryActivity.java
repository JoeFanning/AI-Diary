package com.example.joesp.aidiary;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DiaryEntryActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 2000;

    private EditText edtText;
    private TextToSpeech tts;
    String databaseDate;
    static Date dateObj;
    String diaryTxtEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        TextView txtViewTitleDate = (TextView) findViewById(R.id.txtViewDisplayDate);
        edtText = (EditText) findViewById(R.id.txtViewDiaryEntry);
        Button btnOpen = (Button) findViewById(R.id.btnOpen);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnSpeechToText = (Button) findViewById(R.id.btnSpeechToText);
        Button btnTextToSpeech = (Button) findViewById(R.id.btnTextToSpeech);
        Button btnLocation = (Button) findViewById(R.id.btnLocation);
        Button btnWeather = (Button) findViewById(R.id.btnWeather);
        Button btnNews = (Button) findViewById(R.id.btnNews);
        Button btnWatsonAIAnalysis = (Button) findViewById(R.id.btnWatsonAIAnalysis);

        txtViewTitleDate.setText(MainActivity.displayDate);

        //Check for internet connection
        if (isNetworkAvailable() == false) {
            AlertDialog alrtDialog = new AlertDialog.Builder(DiaryEntryActivity.this).create();
            alrtDialog.setTitle("No internet connection!");
            alrtDialog.setMessage("Please connect to the internet" +
                    "\nto use location, news and weather services");
            alrtDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alrtDialog.show();
        }

        //Call JSON data from openweatherapi.org endpoint
        WeatherDownloadAPIData task = new WeatherDownloadAPIData();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat=53.350140&lon=-6.266155&appid=24b33237d7920999ebb42bedc591472c");

        //Call and get the newsapi.org data
        NewsDownloadAPIData runTaskNewsData = new NewsDownloadAPIData();
        runTaskNewsData.execute("https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=d232f226cdfd49ad90597fc0064a72b7");

        //Button to show a dialog if the user trys to save an empty EditText diary entry.
        //Parse the date into day, month and year.
        //add the diary entry data to the database.
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Show a dialog if the user trys to save an empty EditText diary entry
                String ed_text = edtText.getText().toString().trim();

                if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DiaryEntryActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please enter text into the text  field before saving");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                //Set the date as an id for all the tables in the database so location, weather and news can be matched to
                //a diary entry. Links on how to get date: https://stackoverflow.com/questions/5046771/how-to-get-todays-date
                // https://stackoverflow.com/questions/22989840/get-day-month-and-year-separately-using-simpledateformat
                SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy  hh:mm", Locale.ENGLISH);
                try {
                    dateObj = format.parse("JAN 13,2014  09:15");
                    Calendar myCal = new GregorianCalendar();
                    myCal.setTime(dateObj = new Date());
                    String day = "" + myCal.get(Calendar.DAY_OF_MONTH);
                    String month = "" + (myCal.get(Calendar.MONTH) + 1);
                    String year = "" + myCal.get(Calendar.YEAR);
                    diaryTxtEntry = "" + edtText.getText().toString();
                    databaseDate = day + "" + month + "" + year;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Get all databases data
                //Diary data
                DiaryEntrySQLLiteHelper ddb = new DiaryEntrySQLLiteHelper(DiaryEntryActivity.this);
                List<DiaryEntry> allDiaryEntries = ddb.getAllDiaryEntries();
                //Weather data
                WeatherSQLiteHelper wdb = new WeatherSQLiteHelper(DiaryEntryActivity.this);
                List<Weather> allWeatherEntries = wdb.getAllWeather();
                //News data
                NewsSQLiteHelper ndb = new NewsSQLiteHelper(DiaryEntryActivity.this);
                List<News> allNewsEntries = ndb.getAllNews();
                //Location data
                LocationsSQLiteHelper ldb = new LocationsSQLiteHelper(DiaryEntryActivity.this);
                List<Locations> allLocationsEntries = ldb.getAllLocations();

                //Delete duplicate diary entry data from database
                for (int i = 0; i < allDiaryEntries.size(); ++i) {
                    // int iDate = Integer.parseInt(allDiaryEntries.get(i).getDate());
                    if (databaseDate.equals(String.valueOf(allDiaryEntries.get(i).getDate()))) {
                        ddb.deleteDiaryEntry(allDiaryEntries.get(i));
                    }
                }
                //Delete duplicate weather data from database
                for (int i = 0; i < allWeatherEntries.size(); ++i) {
                    if (databaseDate.equals(String.valueOf(allWeatherEntries.get(i).getDate()))) {
                        wdb.deleteWeather(allWeatherEntries.get(i));
                    }
                }
                //Delete duplicate news data from database
                for (int i = 0; i < allNewsEntries.size(); ++i) {
                    if (databaseDate.equals(String.valueOf(allNewsEntries.get(i)))) {
                        ndb.deleteNewsEntry(allNewsEntries.get(i));
                    }
                }
                //Delete duplicate locations data from database
                for (int i = 0; i < allLocationsEntries.size(); ++i) {
                    if (databaseDate.equals(String.valueOf(allLocationsEntries.get(i).getDate()))) {
                        ldb.deleteALocationEntry(allLocationsEntries.get(i));
                    }
                }

                //Add the diary entry data to the database
                DiaryEntry diaryEntry = new DiaryEntry(/*databaseDate, displayDate, diaryEntry*/);
                diaryEntry.setDate(databaseDate);
                diaryEntry.setDisplayDate(MainActivity.displayDate);
                diaryEntry.setDiaryEntry(diaryTxtEntry);
                ddb.addDiaryEntry(diaryEntry);


                //TESTING
                //View all the diary entry rows for TESTING
                if (TESTINGActivity.turnOnTestingMode == true) {
                    Toast.makeText(DiaryEntryActivity.this, "New DiaryEntry Data Added for: " + databaseDate,
                            Toast.LENGTH_LONG).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(DiaryEntryActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("DiaryEntryActivity.java"
                            + "\n\nNumber of DIARYENTRYS entries in table: " + allDiaryEntries.size()
                            + "\n\ndiary entry data for: " + databaseDate + " saved is"
                            + "\ndate = " + databaseDate
                            + "\ndisplay date = " + MainActivity.displayDate
                            + "\ndiary entry = " + diaryEntry);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                //END TESTING


                Weather weather = new Weather();
                weather.setDate(Integer.parseInt(databaseDate));
                weather.setDisplayDate(MainActivity.displayDate);
                weather.setCountry(WeatherDownloadAPIData.sysCountry);
                weather.setArea(WeatherDownloadAPIData.area);
                weather.setWeatherDescription(WeatherDownloadAPIData.weatherDescription);
                weather.setWeatherImage(WeatherDownloadAPIData.weatherId);
                weather.setTemperature(String.valueOf(WeatherDownloadAPIData.mainTemperature));
                weather.setWindSpeed(WeatherDownloadAPIData.windSpeed);
                weather.setPressure(WeatherDownloadAPIData.mainPressure);
                weather.setHumidity(WeatherDownloadAPIData.mainHumidity);
                weather.setVisibility(WeatherDownloadAPIData.visibility);
                weather.setSunrise(WeatherDownloadAPIData.epochSunriseToHumanDate.toString());
                weather.setSunset(WeatherDownloadAPIData.epochSunsetToHumanDate.toString());
                wdb.addWeather(weather);

                //TESTING
                if (TESTINGActivity.turnOnTestingMode == true) {
                    //View all the weather rows for TESTING
                    Toast.makeText(DiaryEntryActivity.this, "New Weather Data Added for: " + databaseDate,
                            Toast.LENGTH_LONG).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(DiaryEntryActivity.this).create();
                    alertDialog.setTitle("WEATHER");
                    alertDialog.setMessage("DiaryEntryActivity.java"
                            + "\n\nNumber of WEATHER entries in table: " + allWeatherEntries.size()
                            + "\n\nWeather data for: " + databaseDate + " saved is"
                            + "\n\ndate = " + databaseDate
                            + "\ndisplay date = " + MainActivity.displayDate
                            + "\ncountry = " + WeatherDownloadAPIData.sysCountry
                            + "\narea = " + WeatherDownloadAPIData.area
                            + "\nweather description = " + WeatherDownloadAPIData.weatherDescription
                            + "\nect ect ect");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                //END TESTING


                //Add the News object data to the database
                News news = new News();
                news.setDate(Integer.parseInt(databaseDate));
                news.setDisplayDate(MainActivity.displayDate);
                news.setArticleTitle1(NewsDownloadAPIData.newsArticleTitle1);
                news.setArticleTitle2(NewsDownloadAPIData.newsArticleTitle2);
                news.setArticleTitle3(NewsDownloadAPIData.newsArticleTitle3);
                news.setArticleTitle4(NewsDownloadAPIData.newsArticleTitle4);
                news.setArticleTitle5(NewsDownloadAPIData.newsArticleTitle5);
                news.setArticleDescription1(NewsDownloadAPIData.newsArticleDescription1);
                news.setArticleDescription2(NewsDownloadAPIData.newsArticleDescription2);
                news.setArticleDescription3(NewsDownloadAPIData.newsArticleDescription3);
                news.setArticleDescription4(NewsDownloadAPIData.newsArticleDescription4);
                news.setArticleDescription5(NewsDownloadAPIData.newsArticleDescription5);
                news.setArticleImage1(NewsDownloadAPIData.imageNewsArticle1);
                news.setArticleImage2(NewsDownloadAPIData.imageNewsArticle2);
                news.setArticleImage3(NewsDownloadAPIData.imageNewsArticle3);
                news.setArticleImage4(NewsDownloadAPIData.imageNewsArticle4);
                news.setArticleImage5(NewsDownloadAPIData.imageNewsArticle5);
                ndb.addNews(news);


                if (TESTINGActivity.turnOnTestingMode) {
                    //TESTING
                    AlertDialog alertDial = new AlertDialog.Builder(DiaryEntryActivity.this).create();
                    alertDial.setTitle("NEWS");
                    alertDial.setMessage("DiaryEntryActivity.java"
                            + "\n\nNumber of NEWS entries in table: " + allNewsEntries.size()
                            + "\n\nNews data for: " + databaseDate + " saved is"
                            + "\n\ndate = " + databaseDate
                            + "\ndisplay date = " + MainActivity.displayDate
                            + "\narticleTitle1 = " + NewsDownloadAPIData.newsArticleTitle1
                            + "\narticleTitle2 = " + NewsDownloadAPIData.newsArticleTitle2
                            + "\narticleTitle3 = " + NewsDownloadAPIData.newsArticleTitle3
                            + "\nect ect ect");
                    alertDial.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDial.show();
                    //END TESTING
                }


                //Add the Locations object data to the database
                Locations locations = new Locations();
                locations.setDate(Integer.parseInt(databaseDate));
                locations.setLongitude(String.valueOf(Locations.historicalLongitude));
                locations.setLatitude(String.valueOf(Locations.historicalLatitude));
                locations.setLastLocationUpdateTime(String.valueOf(Locations.historicalUpdateTime));
                ldb.addLocation(locations);

                if (TESTINGActivity.turnOnTestingMode == true) {
                    //TESTING
                    //View all the weather rows for testing
                    AlertDialog alrtDialog = new AlertDialog.Builder(DiaryEntryActivity.this).create();
                    alrtDialog.setTitle("LOCATIONS");
                    alrtDialog.setMessage("DiaryEntryActivity.java"
                            + "\n\nNumber of LOCATIONS entries in table: " + allLocationsEntries.size()
                            + "\n\nLocations data for: " + databaseDate + " saved is"
                            + "\n\ndate = " + databaseDate
                            + "\nlongitude = " + locations.getLongitude()
                            + "\nlatitude = " + locations.getLatitude()
                            + "\ngetLastLocationUpdateTime = " + locations.getLastLocationUpdateTime()
                            + "\nect ect ect");
                    alrtDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alrtDialog.show();
                    //END TESTING
                }

                Toast.makeText(getApplicationContext(),
                        "Diary entry SAVED",
                        Toast.LENGTH_LONG).show();

            }
        });

        //Button to open the date picker to select an histotical date
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryEntryActivity.this, DatePickerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        //Button to activate popup window with location and Google Map
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryEntryActivity.this,
                        LocationPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to activate popup window with weather API
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //The layout method in WeatherDownLoadAPIData is seperate from the
                // jSON(onPostExecute method) and URL connection(doInBackground)
                //This is so the jSON(onPostExecute method) and URL connection(doInBackground) can be used for collecting data
                //form the Weather API for the database.
                //if this button is pressed the layout will be run
                //otherwise it won't.
                WeatherDownloadAPIData.runTheSetLayoutMethod = true;

                startActivity(new Intent(DiaryEntryActivity.this,
                        WeatherPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));

            }
        });

        //Button to activate popup window with news API
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryEntryActivity.this,
                        NewsPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        //Button to open WatsonAIAnalysis activity
        btnWatsonAIAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryEntryActivity.this,
                        WatsonAnalysisActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to activate speech to text
        btnSpeechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
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

        //Button to activate text to speech
        btnTextToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = edtText.getText().toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }//end of onCreate()

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    //Showing google speech input dialog..
    private void promptSpeechInput() {
        //create an intent to recognise speech input and create language locale- get default language
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if requestCode == REQ_CODE_SPEECH_INPUT and resultCode is ok and data is not null add text to EditText
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Add text to current text
                    edtText.setText(edtText.getText() + " " + result.get(0));
                }
                break;
            }
        }
    }

    //Use to check for internet connection at start of this activity
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

