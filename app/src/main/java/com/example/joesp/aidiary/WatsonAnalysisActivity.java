package com.example.joesp.aidiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class WatsonAnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watson_analysis);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        Button btnNaturalLanguageUnderstanding = (Button) findViewById(R.id.btnNaturalLanguageUnderstanding);
        Button btnPersonalityInsights = (Button) findViewById(R.id.btnPersonalityInsights);
        Button btnEnvironmentalEffects = (Button) findViewById(R.id.btnEnvironmentalEffects);

        //Show a dialog if the user tries to uses Watson services without a database.
        if(doesDatabaseExist(WatsonAnalysisActivity.this, DiaryEntrySQLLiteHelper.DATABASE_NAME) == false) {
            AlertDialog alrtDialog = new AlertDialog.Builder(WatsonAnalysisActivity.this).create();
            alrtDialog.setTitle("No diary entries for analysis!\nDatabase empty.");
            alrtDialog.setMessage("Watson analytics cannot run unless you have entered a diary entry. " +
                    "Please go to the diary entry page and enter your first diary with a minimum of a 100 words" +
                    ". Then you can run Watason analytics");
            alrtDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alrtDialog.show();
        }

        //Button to close this popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Button to open Natural Language popup window
        btnNaturalLanguageUnderstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatsonAnalysisActivity.this,
                        NLUPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        //Button to open Personality Insights popup window
        btnPersonalityInsights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatsonAnalysisActivity.this,
                        PersonalityInsightsPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        //Button to open Environmental Effects popup window
        btnEnvironmentalEffects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatsonAnalysisActivity.this,
                        EnviornmentalEffectsPopupWindow.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

     //Check if the Diary entry database exist and can be read.
     static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(DiaryEntrySQLLiteHelper.DATABASE_NAME);
        return dbFile.exists();
    }
}


