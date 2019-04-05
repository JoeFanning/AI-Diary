package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;

import org.json.JSONObject;

import java.util.List;

public class EnviornmentalEffectsPopupWindow extends AppCompatActivity {

    TextView txtViewAnger;
    TextView txtViewDisgust;
    TextView txtViewFear;
    TextView txtViewJoy;
    TextView txtViewSadness;
    TextView txtViewEnvironmentalAngerScore;
    TextView txtViewEnvironmentalDisgustScore;
    TextView txtViewEnvironmentalFearScore;
    TextView txtViewEnvironmentalJoyScore;
    TextView txtViewEnvironmentalSadnessScore;
    StringBuilder diaryTextsForEmotionalAnalysis;
    StringBuilder newsTextsForEmotionalAnalysis;
    StringBuilder weatherDataForEmotionalAnalysis;
    StringBuilder environmentalDataForEmotionalAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviornmental_effects_popup_window);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button btnShowEnvironmentalEmotionalScores = (Button) findViewById(R.id.btnShowEnvironmentalEmotionalScores);
        txtViewAnger = (TextView) findViewById(R.id.txtViewAnger);
        txtViewDisgust = (TextView) findViewById(R.id.txtViewDisgust);
        txtViewFear = (TextView) findViewById(R.id.txtViewFear);
        txtViewJoy = (TextView) findViewById(R.id.txtViewJoy);
        txtViewSadness = (TextView) findViewById(R.id.txtViewSadness);
        txtViewEnvironmentalAngerScore = (TextView) findViewById(R.id.txtViewEnvironmentalAngerScore);
        txtViewEnvironmentalDisgustScore = (TextView) findViewById(R.id.txtViewEnvironmentalDisgustScore);
        txtViewEnvironmentalFearScore = (TextView) findViewById(R.id.txtViewEnvironmentalFearScore);
        txtViewEnvironmentalJoyScore = (TextView) findViewById(R.id.txtViewEnvironmentalJoyScore);
        txtViewEnvironmentalSadnessScore = (TextView) findViewById(R.id.txtViewEnvironmentalSadnessScore);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);
        int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case 100%/10*9 = 90%
        getWindow().setLayout(width / 10 * 9, height / 10 * 9);

        //button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnviornmentalEffectsPopupWindow.this,
                        WatsonAnalysisActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                finish();
            }
        });

        //Button to show environmental emotional scores.
        //Get emotional scores for all the environmental data collected and compare the scores
        //with all diary text entry emotional scores.
        btnShowEnvironmentalEmotionalScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the text diary entries in the database for emotional analysis
                NewsSQLiteHelper ndb = new NewsSQLiteHelper(EnviornmentalEffectsPopupWindow.this);
                List<News> allNewsEntries = ndb.getAllNews();
                newsTextsForEmotionalAnalysis = new StringBuilder();
                for (int i = 0; i < allNewsEntries.size(); ++i) {
                    newsTextsForEmotionalAnalysis.append("News article one: " + allNewsEntries.get(i).getArticleDescription1() + " .");
                    newsTextsForEmotionalAnalysis.append("News article two: " + allNewsEntries.get(i).getArticleDescription2() + " .");
                    newsTextsForEmotionalAnalysis.append("News article three: " + allNewsEntries.get(i).getArticleDescription3() + " .");
                    newsTextsForEmotionalAnalysis.append("News article four: " + allNewsEntries.get(i).getArticleDescription4() + " .");
                    newsTextsForEmotionalAnalysis.append("News article five: " + allNewsEntries.get(i).getArticleDescription5() + " .");
                }
                //Get all the weather data in the database for emotional analysis
                WeatherSQLiteHelper wdb = new WeatherSQLiteHelper(EnviornmentalEffectsPopupWindow.this);
                List<Weather> allWeatherData = wdb.getAllWeather();
                weatherDataForEmotionalAnalysis = new StringBuilder();
                for (int i = 0; i < allWeatherData.size(); ++i) {
                    weatherDataForEmotionalAnalysis.append("\n\nThe weather forcast today is " + allWeatherData.get(i).getWeatherDescription() + ".");
                    weatherDataForEmotionalAnalysis.append("Todays temperature is " + allWeatherData.get(i).getTemperature() + ".");
                    weatherDataForEmotionalAnalysis.append("Todays windspeed is " + allWeatherData.get(i).getWindSpeed() + ".");
                    weatherDataForEmotionalAnalysis.append("Todays pressure is " + allWeatherData.get(i).getPressure() + ".");
                    weatherDataForEmotionalAnalysis.append("Today humidity is " + allWeatherData.get(i).getHumidity() + ".");
                    weatherDataForEmotionalAnalysis.append("Todays visibility is " + allWeatherData.get(i).getVisibility() + ".");
                    weatherDataForEmotionalAnalysis.append("Sunrise today is at " + allWeatherData.get(i).getSunrise() + ".");
                    weatherDataForEmotionalAnalysis.append("Sunset today is at " + allWeatherData.get(i).getSunset() + ".");
                }
                environmentalDataForEmotionalAnalysis = new StringBuilder(
                        newsTextsForEmotionalAnalysis + "" + weatherDataForEmotionalAnalysis);


                if (TESTINGActivity.turnOnTestingMode == true) {
                    //TESTING
                    // View all the concatenated environmental data text
                    //to check its edited correctly for analysis.
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            EnviornmentalEffectsPopupWindow.this).create();
                    alertDialog.setTitle("All news and weather data text for analysis");
                    alertDialog.setMessage("EnviornmentalEffectsPopupWindow.java"
                            + "\n\n" + environmentalDataForEmotionalAnalysis);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    //END TESTING
                }

                //Make sure the user has about 10 words for NLU analysis or Watson won't run
                if(diaryTextsForEmotionalAnalysis.length() > 40) {
                new WatsonNLUEnvironmentalEmotionalScores().execute("Here is the news and weather for " +
                        "today." + environmentalDataForEmotionalAnalysis);}
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(EnviornmentalEffectsPopupWindow.this).create();
                        alertDialog.setTitle("Watson can't Analyse");
                        alertDialog.setMessage("Watson Natural Language Understanding can't Analyse with less than 7 words. "
                                +"Please make sure your FIRST diary entry has at least 7 word."
                        );
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
            }
        });

        //Get all the text diary entries in the database for emotional analysis
        DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(EnviornmentalEffectsPopupWindow.this);
        List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
        diaryTextsForEmotionalAnalysis = new StringBuilder();
        for (int i = 0; i < allDiaryEntries.size(); ++i) {
            diaryTextsForEmotionalAnalysis.append(allDiaryEntries.get(i).getDiaryEntry());
        }


        if (TESTINGActivity.turnOnTestingMode == true) {
            //TESTING
            // View all the concatenated diary texts
            //to check they are edited correctly for analysis.
            AlertDialog alertDialog = new AlertDialog.Builder(
                    EnviornmentalEffectsPopupWindow.this).create();
            alertDialog.setTitle("All diary texts for analysis");
            alertDialog.setMessage("EnviornmentalEffectsPopupWindow.java"
                    + "\n\n" + diaryTextsForEmotionalAnalysis);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            //END TESTING
        }

        //Make sure the user has about 10 words for NLU analysis or Watson won't run
        if(diaryTextsForEmotionalAnalysis.length() > 40) {
        new WatsonNLUAllDiaryTextEmotionalScores().execute("" + diaryTextsForEmotionalAnalysis);}
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(EnviornmentalEffectsPopupWindow.this).create();
            alertDialog.setTitle("Watson can't Analyse");
            alertDialog.setMessage("Watson Natural Language Understanding can't Analyse with less than 10 words. "
                    +"Please make sure your FIRST diary entry has at least 10 word."
            );
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    //Asynchronous request class for running emotion feature on Watson Natural Language Understanding API on IBM Cloud
    //Use this class for analysing all the diary text entries
    private class WatsonNLUAllDiaryTextEmotionalScores extends AsyncTask<String, Void, String> {

        //Run when background process is started and signal the user
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(),
                    "Connecting to Watson API \n- Please wait !",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        //Run Watson as a background process
        protected String doInBackground(String... params) {

            String inputText = params[0];

            //Call the service by entering the credentials: id and password, and set the API endpoint
            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                    NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                    "a42451f8-c746-498a-a0ce-62bd08382bbc",
                    "1DBvb6a2MMsO"
            );
            service.setEndPoint("https://gateway.watsonplatform.net/natural-language-understanding/api");

            //link to NLU emotions feature documention https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#emotion
            EmotionOptions emotion = new EmotionOptions.Builder()
                    .document(true)
                    .build();

            Features features = new Features.Builder()
                    .emotion(emotion)
                    .build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                    .text("" + inputText)
                    .features(features)
                    .build();

            AnalysisResults response = service
                    .analyze(parameters)
                    .execute();

            return "" + response;
        }

        //Run when background process is complete
        protected void onPostExecute(String result) {


            if (TESTINGActivity.turnOnTestingMode == true) {
                //TESTING
                // View Watson JSON for testing.
                AlertDialog alertDialog = new AlertDialog.Builder(
                        EnviornmentalEffectsPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("EnviornmentalEffectsPopupWindow.java"
                        + "\nWatsonNLUAllDiaryTextEmotionalScores"
                        + "\n\nWatson JSON data"
                        + "\n\n" + result);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                //END TESTING
            }


            //Exract all the JSON data from the emotions feature in Watson Natural Languge Understanding.
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject emotion = new JSONObject(jsonObject.getString("emotion"));
                JSONObject document = new JSONObject(emotion.getString("document"));
                JSONObject documentEmotion = new JSONObject(document.getString("emotion"));
                String anger = documentEmotion.getString("anger");
                String disgust = documentEmotion.getString("disgust");
                String fear = documentEmotion.getString("fear");
                String joy = documentEmotion.getString("joy");
                String sadness = documentEmotion.getString("sadness");

                Toast.makeText(getApplicationContext(),
                        "Watson Analysis COMPLETED",
                        Toast.LENGTH_LONG).show();

                //Set all the JSON data to the TextViews and color them for emotional description.
                txtViewAnger.setBackgroundColor(Color.RED);
                txtViewAnger.setText("Anger: " + anger);
                txtViewDisgust.setBackgroundColor(Color.GREEN);
                txtViewDisgust.setTextColor(Color.BLACK);
                txtViewDisgust.setTextSize(20);
                txtViewDisgust.setText("Disgust: " + disgust);
                txtViewFear.setBackgroundColor(Color.CYAN);
                txtViewFear.setText("Fear: " + fear);
                txtViewJoy.setBackgroundColor(Color.YELLOW);
                txtViewJoy.setText("Joy: " + joy);
                txtViewSadness.setBackgroundColor(Color.BLUE);
                txtViewSadness.setText("Sadness: " + sadness);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Asynchronous request class for running emotion feature on Watson Natural Language Understanding API on IBM Cloud
    //Use this class for analysing all the weather and news text data
    private class WatsonNLUEnvironmentalEmotionalScores extends AsyncTask<String, Void, String> {

        //Run when background process is started and signal the user
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(),
                    "Connecting to Watson API \n- Please wait !",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        //Run Watson as a background process
        protected String doInBackground(String... params) {

            String inputText = params[0];

            //Call the service by entering the credentials: id and password, and set the API endpoint
            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                    NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                    "a42451f8-c746-498a-a0ce-62bd08382bbc",
                    "1DBvb6a2MMsO"
            );
            service.setEndPoint("https://gateway.watsonplatform.net/natural-language-understanding/api");

            EmotionOptions emotion = new EmotionOptions.Builder()
                    .document(true)
                    .build();

            Features features = new Features.Builder()
                    .emotion(emotion)
                    .build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                    .text("" + inputText)
                    .features(features)
                    .build();

            AnalysisResults response = service
                    .analyze(parameters)
                    .execute();

            return "" + response;
        }

        //Run when background process is complete
        protected void onPostExecute(String result) {


            if (TESTINGActivity.turnOnTestingMode == true) {
                //TESTING
                // View Watson JSON for testing
                AlertDialog alertDialog = new AlertDialog.Builder(
                        EnviornmentalEffectsPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("EnviornmentalEffectsPopupWindow.java"
                        + "\nWatsonNLUEnvironmentaEmotionalScores"
                        + "\n\nWatson JSON data"
                        + "\n\n" + result);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                //END TESTING
            }


            //Extract all the JSON data from the emotions feature in Watson Natural Language Understanding
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject emotion = new JSONObject(jsonObject.getString("emotion"));
                JSONObject document = new JSONObject(emotion.getString("document"));
                JSONObject documentEmotion = new JSONObject(document.getString("emotion"));
                String anger = documentEmotion.getString("anger");
                String disgust = documentEmotion.getString("disgust");
                String fear = documentEmotion.getString("fear");
                String joy = documentEmotion.getString("joy");
                String sadness = documentEmotion.getString("sadness");

                Toast.makeText(getApplicationContext(),
                        "Watson Analysis COMPLETED",
                        Toast.LENGTH_LONG).show();

                txtViewEnvironmentalAngerScore.setBackgroundColor(Color.RED);
                txtViewEnvironmentalAngerScore.setText("Environment Anger: " + anger);
                txtViewEnvironmentalDisgustScore.setBackgroundColor(Color.GREEN);
                txtViewEnvironmentalDisgustScore.setTextColor(Color.BLACK);
                txtViewEnvironmentalDisgustScore.setTextSize(20);
                txtViewEnvironmentalDisgustScore.setText("Environment Disgust: " + disgust);
                txtViewEnvironmentalFearScore.setBackgroundColor(Color.CYAN);
                txtViewEnvironmentalFearScore.setText("Environment Fear: " + fear);
                txtViewEnvironmentalJoyScore.setBackgroundColor(Color.YELLOW);
                txtViewEnvironmentalJoyScore.setText("Environment Joy: " + joy);
                txtViewEnvironmentalSadnessScore.setBackgroundColor(Color.BLUE);
                txtViewEnvironmentalSadnessScore.setText("Environment Sadness: " + sadness);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
