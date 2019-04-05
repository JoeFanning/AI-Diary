package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Trait;

import java.util.List;

public class PersonalityInsightsPopupWindow extends WatsonAnalysisActivity {
    private String TAG = "Error in onFailure";
    StringBuilder diaryTextsForPersonalityInsightsAnalysis;
    static String dataFromWatsonServiceName;
    static String dataFromWatsonEndPoint;
    static String dataFromWatsonProcessedLanguage;
    static String dataFromWatsonWordCount;
    static String traitName_0;
    static String percentileScore_0;
    static String traitName_1;
    static String percentileScore_1;
    static String traitName_2;
    static String percentileScore_2;
    static String traitName_3;
    static String percentileScore_3;
    static String traitName_4;
    static String percentileScore_4;
    static String facetName_00;
    static String facetPercentileScore_00;
    static String facetName_10;
    static String facetPercentileScore_10;
    static String facetName_20;
    static String facetPercentileScore_20;
    static String facetName_30;
    static String facetPercentileScore_30;
    static String facetName_40;
    static String facetPercentileScore_40;
    static String facetName_01;
    static String facetPercentileScore_01;
    static String facetName_11;
    static String facetPercentileScore_11;
    static String facetName_21;
    static String facetPercentileScore_21;
    static String facetName_31;
    static String facetPercentileScore_31;
    static String facetName_41;
    static String facetPercentileScore_41;
    static String facetName_02;
    static String facetPercentileScore_02;
    static String facetName_12;
    static String facetPercentileScore_12;
    static String facetName_22;
    static String facetPercentileScore_22;
    static String facetName_32;
    static String facetPercentileScore_32;
    static String facetName_42;
    static String facetPercentileScore_42;
    static String facetName_03;
    static String facetPercentileScore_03;
    static String facetName_13;
    static String facetPercentileScore_13;
    static String facetName_23;
    static String facetPercentileScore_23;
    static String facetName_33;
    static String facetPercentileScore_33;
    static String facetName_43;
    static String facetPercentileScore_43;
    static String facetName_04;
    static String facetPercentileScore_04;
    static String facetName_14;
    static String facetPercentileScore_14;
    static String facetName_24;
    static String facetPercentileScore_24;
    static String facetName_34;
    static String facetPercentileScore_34;
    static String facetName_44;
    static String facetPercentileScore_44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_insights_popup_window);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button btnInfoOnTraitScores = (Button) findViewById(R.id.btnInfoOnTraitScores);
        //Keep these for displaying Trait scores above the FACETS buttons
//        final TextView txtViewTraitName0 = (TextView) findViewById(R.id.txtViewTrait0);
//        final TextView txtViewTraitName1 = (TextView) findViewById(R.id.txtViewTrait1);
//        final TextView txtViewTraitName2 = (TextView) findViewById(R.id.txtViewTrait2);
//        final TextView txtViewTraitName3 = (TextView) findViewById(R.id.txtViewTrait3);
//        final TextView txtViewTraitName4 = (TextView) findViewById(R.id.txtViewTrait4);
        Button btnFacets0 = (Button) findViewById(R.id.btnFacets0);
        Button btnFacets1 = (Button) findViewById(R.id.btnFacets1);
        Button btnFacets2 = (Button) findViewById(R.id.btnFacets2);
        Button btnFacets3 = (Button) findViewById(R.id.btnFacets3);
        Button btnFacets4 = (Button) findViewById(R.id.btnFacets4);


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
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        WatsonAnalysisActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                finish();
            }
        });

        //Button to open dialog to show explanation for percentile scores
        btnInfoOnTraitScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(PersonalityInsightsPopupWindow.this).create();
                alertDialog.setTitle("Explanation for percentile scores");
                alertDialog.setMessage("Percentile Number \n\n"
                        + "The normalized percentile score for the characteristic. The range "
                        + "is 0 to 1. For example, if the percentile for Openness is 0.60, the "
                        + "author scored in the 60th percentile; the author is more open than 59 "
                        + "percent of the population and less open than 39 percent of the population.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        //Button to display characteristic facets
        btnFacets0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        PIFacet0PopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to display characteristic facets
        btnFacets1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        PIFacet1PopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to display characteristic facets
        btnFacets2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        PIFacet2PopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to display characteristic facets
        btnFacets3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        PIFacet3PopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //Button to display characteristic facets
        btnFacets4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalityInsightsPopupWindow.this,
                        PIFacet4PopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        //http://watson-developer-cloud.github.io/java-sdk/docs/java-sdk-3.9.1/com/ibm/watson/developer_cloud/personality_insights/v3/PersonalityInsights.html//
        final PersonalityInsights service = new PersonalityInsights("2017-11-01");
        service.setUsernameAndPassword("9ee7716d-1cfd-40b0-8ba0-60dcf0aacbe9", "HLY0PTCJ1QvW");
        service.setEndPoint("https://gateway.watsonplatform.net/personality-insights/api");

        //Get all the text diary entries in the database for emotional analysis.
        DiaryEntrySQLLiteHelper db = new DiaryEntrySQLLiteHelper(PersonalityInsightsPopupWindow.this);
        List<DiaryEntry> allDiaryEntries = db.getAllDiaryEntries();
        diaryTextsForPersonalityInsightsAnalysis = new StringBuilder();
        for (int i = 0; i < allDiaryEntries.size(); ++i) {
            diaryTextsForPersonalityInsightsAnalysis.append(allDiaryEntries.get(i).getDiaryEntry());
        }

        //Show dialog if there is less than 100 words in all diary entries, Persoanlity insights must have at least a 100
        // words for analysis
        // average word length is 5.1 letters (see link for average word length: https://arxiv.org/ftp/arxiv/papers/1208/1208.6109.pdf)
        if(diaryTextsForPersonalityInsightsAnalysis.length() > 650) {
            //Accepts text and responds with a Profile with a tree of characteristics that include personality, needs, and values.
            final ServiceCall<Profile> call = service.getProfile("" + diaryTextsForPersonalityInsightsAnalysis);

            //Asynchronous requests, in this case, you receive a callback when the data has been received.
            call.enqueue(new ServiceCallback<Profile>() {
                @Override
                public void onResponse(Profile profile) {
                    //api: http://watson-developer-cloud.github.io/java-sdk/docs/java-sdk-3.9.1/com/ibm/watson/developer_cloud/personality_insights/v3/model/Profile.html

                    //FOR TESTING
                    //Get service info for testing connection
                    dataFromWatsonServiceName = service.getName();
                    dataFromWatsonEndPoint = service.getEndPoint();
                    dataFromWatsonProcessedLanguage = profile.getProcessedLanguage();
                    dataFromWatsonWordCount = profile.getWordCount().toString();
                    // dataFromWatsonWordCountMessage = profile.getWordCountMessage();
                    //END TESTING

                    //The Trait class is in: package com.ibm.watson.developer_cloud.personality_insights.v3.model;
                    //List<Trait> values = profile.getValues();
                    List<Trait> values = profile.getPersonality();
                    //Gets the user-visible name of the characteristic.
                    String traitName0 = values.get(0).getName();
                    //Gets the raw score for the characteristic.
                    //Text below from: https://www.ibm.com/watson/developercloud/personality-insights/api/v3/?cm_mc_uid=59944807660715078499130&cm_mc_sid_50200000=1511896212&cm_mc_sid_52640000=1511896320#profile
                    //raw_score number:
                    //If you request raw scores, the raw score for the characteristic. The range is 0 to 1. A higher
                    // score generally indicates a greater likelihood that the author has that characteristic, but raw
                    // scores must be considered in aggregate: The range of values in practice might be much smaller
                    // than 0 to 1, so an individual score must be considered in the context of the overall scores and
                    // their range. The raw score is computed based on the input and the service model; it is not
                    // normalized or compared with a sample population. The raw score enables comparison of the results
                    // against a different sampling population and with a custom normalization approach.
                    Double rawScore0 = values.get(0).getRawScore();//Not used here
                    //Gets the normalized percentile score for the characteristic. The range is 0 to 1.
                    //Text below from: https://www.ibm.com/watson/developercloud/personality-insights/api/v3/?cm_mc_uid=59944807660715078499130&cm_mc_sid_50200000=1511896212&cm_mc_sid_52640000=1511896320#profile
                    //percentile number:
                    // The normalized percentile score for the characteristic. The range is 0 to 1. For example, if the
                    // percentile for Openness is 0.60, the author scored in the 60th percentile; the author is more open
                    // than 59 percent of the population and less open than 39 percent of the population.
                    String percentileScore0 = String.valueOf(values.get(0).getPercentile()).substring(0, 6);

                    String traitName1 = values.get(1).getName();
                    String percentileScore1 = String.valueOf(values.get(1).getPercentile()).substring(0, 6);
                    String traitName2 = values.get(2).getName();
                    String percentileScore2 = String.valueOf(values.get(2).getPercentile()).substring(0, 6);
                    String traitName3 = values.get(3).getName();
                    String percentileScore3 = String.valueOf(values.get(3).getPercentile()).substring(0, 6);
                    String traitName4 = values.get(4).getName();
                    String percentileScore4 = String.valueOf(values.get(4).getPercentile()).substring(0, 6);

                    traitName_0 = traitName0;
                    percentileScore_0 = percentileScore0;
                    traitName_1 = traitName1;
                    percentileScore_1 = percentileScore1;
                    traitName_2 = traitName2;
                    percentileScore_2 = percentileScore2;
                    traitName_3 = traitName3;
                    percentileScore_3 = percentileScore3;
                    traitName_4 = traitName4;
                    percentileScore_4 = percentileScore4;

                    //Get all the
                    //Children
                    //For personality (Big Five) dimensions, an array of Trait objects that
                    //provides more detailed results for the facets of each dimension as inferred
                    //from the input text.
                    //Openness five facets
                    List<Trait> children0 = values.get(0).getChildren();
                    //Gets the user-visible name of the characteristic.
                    String child00 = children0.get(0).getName();
                    //Gets the normalized percentile score for the characteristic. The range is 0 to 1.
                    String facetPercentileScore00 = String.valueOf(children0.get(0).getPercentile()).substring(0, 6);

                    String child10 = children0.get(1).getName();
                    String facetPercentileScore10 = String.valueOf(children0.get(1).getPercentile()).substring(0, 6);
                    String child20 = children0.get(2).getName();
                    String facetPercentileScore20 = String.valueOf(children0.get(2).getPercentile()).substring(0, 6);
                    String child30 = children0.get(3).getName();
                    String facetPercentileScore30 = String.valueOf(children0.get(3).getPercentile()).substring(0, 6);
                    String child40 = children0.get(4).getName();
                    String facetPercentileScore40 = String.valueOf(children0.get(4).getPercentile()).substring(0, 6);
                    facetName_00 = child00;
                    facetPercentileScore_00 = facetPercentileScore00;
                    facetName_10 = child10;
                    facetPercentileScore_10 = facetPercentileScore10;
                    facetName_20 = child20;
                    facetPercentileScore_20 = facetPercentileScore20;
                    facetName_30 = child30;
                    facetPercentileScore_30 = facetPercentileScore30;
                    facetName_40 = child40;
                    facetPercentileScore_40 = facetPercentileScore40;

                    //Conscientiousness five facets
                    List<Trait> children1 = values.get(1).getChildren();
                    String child01 = children1.get(0).getName();
                    String facetPercentileScore01 = String.valueOf(children1.get(0).getPercentile()).substring(0, 6);
                    String child11 = children1.get(1).getName();
                    String facetPercentileScore11 = String.valueOf(children1.get(1).getPercentile()).substring(0, 6);
                    String child21 = children1.get(2).getName();
                    String facetPercentileScore21 = String.valueOf(children1.get(2).getPercentile()).substring(0, 6);
                    String child31 = children1.get(3).getName();
                    String facetPercentileScore31 = String.valueOf(children1.get(3).getPercentile()).substring(0, 6);
                    String child41 = children1.get(4).getName();
                    String facetPercentileScore41 = String.valueOf(children1.get(4).getPercentile()).substring(0, 6);
                    facetName_01 = child01;
                    facetPercentileScore_01 = facetPercentileScore01;
                    facetName_11 = child11;
                    facetPercentileScore_11 = facetPercentileScore11;
                    facetName_21 = child21;
                    facetPercentileScore_21 = facetPercentileScore21;
                    facetName_31 = child31;
                    facetPercentileScore_31 = facetPercentileScore31;
                    facetName_41 = child41;
                    facetPercentileScore_41 = facetPercentileScore41;

                    //Extraversion five facets
                    List<Trait> children2 = values.get(2).getChildren();
                    String child02 = children2.get(0).getName();
                    String facetPercentileScore02 = String.valueOf(children2.get(0).getPercentile()).substring(0, 6);
                    String child12 = children2.get(1).getName();
                    String facetPercentileScore12 = String.valueOf(children2.get(1).getPercentile()).substring(0, 6);
                    String child22 = children2.get(2).getName();
                    String facetPercentileScore22 = String.valueOf(children2.get(2).getPercentile()).substring(0, 6);
                    String child32 = children2.get(3).getName();
                    String facetPercentileScore32 = String.valueOf(children2.get(3).getPercentile()).substring(0, 6);
                    String child42 = children2.get(4).getName();
                    String facetPercentileScore42 = String.valueOf(children2.get(4).getPercentile()).substring(0, 6);
                    facetName_02 = child02;
                    facetPercentileScore_02 = facetPercentileScore02;
                    facetName_12 = child12;
                    facetPercentileScore_12 = facetPercentileScore12;
                    facetName_22 = child22;
                    facetPercentileScore_22 = facetPercentileScore22;
                    facetName_32 = child32;
                    facetPercentileScore_32 = facetPercentileScore32;
                    facetName_42 = child42;
                    facetPercentileScore_42 = facetPercentileScore42;

                    //Agreeableness five facets
                    List<Trait> children3 = values.get(3).getChildren();
                    String child03 = children3.get(0).getName();
                    String facetPercentileScore03 = String.valueOf(children3.get(0).getPercentile()).substring(0, 6);
                    String child13 = children3.get(1).getName();
                    String facetPercentileScore13 = String.valueOf(children3.get(1).getPercentile()).substring(0, 6);
                    String child23 = children3.get(2).getName();
                    String facetPercentileScore23 = String.valueOf(children3.get(2).getPercentile()).substring(0, 6);
                    String child33 = children3.get(3).getName();
                    String facetPercentileScore33 = String.valueOf(children3.get(3).getPercentile()).substring(0, 6);
                    String child43 = children3.get(4).getName();
                    String facetPercentileScore43 = String.valueOf(children3.get(4).getPercentile()).substring(0, 6);
                    facetName_03 = child03;
                    facetPercentileScore_03 = facetPercentileScore03;
                    facetName_13 = child13;
                    facetPercentileScore_13 = facetPercentileScore13;
                    facetName_23 = child23;
                    facetPercentileScore_23 = facetPercentileScore23;
                    facetName_33 = child33;
                    facetPercentileScore_33 = facetPercentileScore33;
                    facetName_43 = child43;
                    facetPercentileScore_43 = facetPercentileScore43;

                    //Emotional range five facets
                    List<Trait> children4 = values.get(4).getChildren();
                    String child04 = children4.get(0).getName();
                    String facetPercentileScore04 = String.valueOf(children4.get(0).getPercentile()).substring(0, 6);
                    String child14 = children4.get(1).getName();
                    String facetPercentileScore14 = String.valueOf(children4.get(1).getPercentile()).substring(0, 6);
                    String child24 = children4.get(2).getName();
                    String facetPercentileScore24 = String.valueOf(children4.get(2).getPercentile()).substring(0, 6);
                    String child34 = children4.get(3).getName();
                    String facetPercentileScore34 = String.valueOf(children4.get(3).getPercentile()).substring(0, 6);
                    String child44 = children4.get(4).getName();
                    String facetPercentileScore44 = String.valueOf(children4.get(4).getPercentile()).substring(0, 6);
                    facetName_04 = child04;
                    facetPercentileScore_04 = facetPercentileScore04;
                    facetName_14 = child14;
                    facetPercentileScore_14 = facetPercentileScore14;
                    facetName_24 = child24;
                    facetPercentileScore_24 = facetPercentileScore24;
                    facetName_34 = child34;
                    facetPercentileScore_34 = facetPercentileScore34;
                    facetName_44 = child44;
                    facetPercentileScore_44 = facetPercentileScore44;

                    //Set text in TextViews that were set in onResponse(profile profile) method above.
                    // //Keep these for displaying Trait scores above the FACETS buttons
//                txtViewTraitName0.setText(traitName_0 + " : " + percentileScore_0);
//                txtViewTraitName1.setText(traitName_1 + " : " + percentileScore_1);
//                txtViewTraitName2.setText(traitName_2 + " : " + percentileScore_2);
//                txtViewTraitName3.setText(traitName_3 + " : " + percentileScore_3);
//                txtViewTraitName4.setText(traitName_4 + " : " + percentileScore_4);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "* * * Error in PersonalityInsightsPopupWindow.java from onFaliure() in ServiceCallback");
                    e.printStackTrace();
                    e.getMessage();
                }
            });
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(PersonalityInsightsPopupWindow.this).create();
            alertDialog.setTitle("Watson Personality Insights can't Analyse");
            alertDialog.setMessage("Watson Personality Insights can't Analyse with less than 100 words. "
                    +"Please make sure your FIRST diary entry has at least 100 word."
            );
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        //TESTING
        // View Watson API info
        if (TESTINGActivity.turnOnTestingMode == true) {
            AlertDialog alertDialog = new AlertDialog.Builder(PersonalityInsightsPopupWindow.this).create();
            alertDialog.setTitle("Watson service data");
            alertDialog.setMessage("Refresh this activity to see this info!" +
                    "\n\nWatson service name: " + dataFromWatsonServiceName
                    + "\n\nEnd point for Watson Personality Insights: " + dataFromWatsonEndPoint
                    + "\nText word count: " + dataFromWatsonWordCount
                    + "\nText language: " + dataFromWatsonProcessedLanguage

            );
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        //END TESTING

    }
}
