package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

import org.json.JSONObject;

import java.util.List;


public class NLUPopupWindow extends WatsonAnalysisActivity {

    TextView txtViewAnger;
    TextView txtViewDisgust;
    TextView txtViewFear;
    TextView txtViewJoy;
    TextView txtViewSadness;
    StringBuilder diaryTextsForEmotionalAnalysis;
    StringBuilder diaryTextsForSentimentAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nlupopup_window);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button btnWatsonNLUEmotionFeature = (Button) findViewById(R.id.btnWatsonEmotionFeature);
        Button btnWatsonNLUSentimentFeature = (Button) findViewById(R.id.btnWatsonSentimentFeature);
        txtViewAnger = (TextView) findViewById(R.id.txtViewAnger);
        txtViewDisgust = (TextView) findViewById(R.id.txtViewDisgust);
        txtViewFear = (TextView) findViewById(R.id.txtViewFear);
        txtViewJoy = (TextView) findViewById(R.id.txtViewJoy);
        txtViewSadness = (TextView) findViewById(R.id.txtViewSadness);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case 100%/10*9 = 90%
        getWindow().setLayout(width / 10 * 9, height / 10 * 9);

        //Button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NLUPopupWindow.this,
                        WatsonAnalysisActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                finish();
            }
        });

        //Run emotion feature on Watson Natural Language Understanding API on IBM Cloud
        btnWatsonNLUEmotionFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the diary text entries in the database for emotional analysis
                DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(NLUPopupWindow.this);
                List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
                diaryTextsForEmotionalAnalysis = new StringBuilder();
                for (int i = 0; i < allDiaryEntries.size(); ++i) {
                    diaryTextsForEmotionalAnalysis.append(allDiaryEntries.get(i).getDiaryEntry());
                }

                //Make sure the user has about 10 words for NLU analysis or Watson won't run
                if(diaryTextsForEmotionalAnalysis.length() > 40) {
                new WatsonNLUEmotion().execute("" + diaryTextsForEmotionalAnalysis);}
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(NLUPopupWindow.this).create();
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


                if (TESTINGActivity.turnOnTestingMode == true) {
                    //TESTING
                    // View all the diary text entries in the database appended together
                    AlertDialog alertDialog = new AlertDialog.Builder(NLUPopupWindow.this).create();
                    alertDialog.setTitle("All diary text entries in the database for emotional analysis");
                    alertDialog.setMessage("NLUPopupWindow.java"
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

            }
        });

        //Run sentiment feature on Watson Natural Language Understanding API on IBM Cloud
        btnWatsonNLUSentimentFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the text diary entries in the database for sentiment analysis
                DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(NLUPopupWindow.this);
                List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
                diaryTextsForSentimentAnalysis = new StringBuilder();
                for (int i = 0; i < allDiaryEntries.size(); ++i) {
                    diaryTextsForSentimentAnalysis.append(allDiaryEntries.get(i).getDiaryEntry());
                }
                //Make sure the user has about 10 words for NLU analysis or Watson won't run
                if(diaryTextsForSentimentAnalysis.length() > 40) {
                new WatsonNLUSentiment().execute("" + diaryTextsForSentimentAnalysis);}
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(NLUPopupWindow.this).create();
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
        });
    }

    //Asynchronous request class for running emotion feature on Watson Natural Language Understanding API on IBM Cloud
    private class WatsonNLUEmotion extends AsyncTask<String, Void, String> {

        //Run when background process is started and signal the user
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(),
                    "Connecting to Watson API - Please wait !",
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

//                List<String> targets = new ArrayList<>();
//                targets.add("apples");
//                targets.add("oranges");

            //link to NLU emotions feature documention https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#emotion
            EmotionOptions emotion = new EmotionOptions.Builder()
                    .document(true) //Use .document() and set it to true to detect entire document or .targets(targets) for specific strings in the document
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
                // View JSON for testing
                AlertDialog alertDialog = new AlertDialog.Builder(NLUPopupWindow.this).create();
                alertDialog.setTitle("Watson emotion JSON data");
                alertDialog.setMessage("NLUPopupWindow.java"
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
                        "Watson Analysis Completed",
                        Toast.LENGTH_LONG).show();

                //set all the JSON data to the TextViews and color them for emotional description
                txtViewAnger.setBackgroundColor(Color.RED);
                txtViewAnger.setText(getString(R.string.anger) +" "+ anger);
                txtViewDisgust.setBackgroundColor(Color.GREEN);
                txtViewDisgust.setTextColor(Color.BLACK);
                txtViewDisgust.setTextSize(20);
                txtViewDisgust.setText(getString(R.string.disgust) + " "+disgust);
                txtViewFear.setBackgroundColor(Color.CYAN);
                txtViewFear.setText(getString(R.string.fear) +" "+ fear);
                txtViewJoy.setBackgroundColor(Color.YELLOW);
                txtViewJoy.setText(getString(R.string.joy) +" "+ joy);
                txtViewSadness.setBackgroundColor(Color.BLUE);
                txtViewSadness.setText(getString(R.string.sadness) +" "+ sadness);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Asynchronous request class for running sentiment feature in Watson Natural Language Understanding API on IBM Cloud
    private class WatsonNLUSentiment extends AsyncTask<String, Void, String> {

        //Run when background process is started and signal the user
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(),
                    "Connecting to Watson API\n- Please wait !",
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

            //link to NLU sentiment feature documentation https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#sentiment
            SentimentOptions sentiment = new SentimentOptions.Builder()
                    .document(true)
                    .build();

            Features features = new Features.Builder()
                    .sentiment(sentiment)
                    .build();

            AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                    .text(inputText)
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
                AlertDialog alertDialog = new AlertDialog.Builder(NLUPopupWindow.this).create();
                alertDialog.setTitle("Watson sentiment JSON data");
                alertDialog.setMessage("NLUPopupWindow.java"
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
                JSONObject sentiment = new JSONObject(jsonObject.getString("sentiment"));
                JSONObject document = new JSONObject(sentiment.getString("document"));
                String score = document.getString("score");

                Toast.makeText(getApplicationContext(),
                        "Watson Analysis Completed",
                        Toast.LENGTH_LONG).show();

                //Set all the JSON data to the TextViews and color them for emotional description
                //Use the emotional TextViews as well for displaying the sentiment data.
                txtViewAnger.setTextColor(Color.WHITE);
                txtViewAnger.setBackgroundColor(Color.parseColor("#63B8FF"));
                txtViewAnger.setText("");
                txtViewDisgust.setTextColor(Color.WHITE);
                txtViewDisgust.setBackgroundColor(Color.BLUE);
                txtViewDisgust.setTextSize(17);
                txtViewDisgust.setText("Sentiment score ranging from -1\n(negative sentiment) to 1 (positive sentiment).");
                txtViewFear.setBackgroundColor(Color.parseColor("#63B8FF"));
                txtViewFear.setTextColor(Color.BLACK);
                txtViewFear.setText(getString(R.string.sentimentscore) +" "+ score);
                txtViewJoy.setBackgroundColor(Color.BLUE);
                txtViewJoy.setText("");
                txtViewSadness.setBackgroundColor(Color.BLUE);
                txtViewSadness.setText(" ");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    }