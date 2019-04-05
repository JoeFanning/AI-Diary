package com.example.joesp.aidiary;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TESTINGActivity extends AppCompatActivity {

    static boolean turnOnTestingMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Button btnDeleteAllDatabases = (Button) findViewById(R.id.btnDeleteAllDatabase);
        Button btnShowALLTestingDialogs = (Button) findViewById(R.id.btnShowAllTestingDialogs);
        Button btnHideALLTestingDialogs = (Button) findViewById(R.id.btnHideAllTestingDialogs);
        Button btnClose = (Button) findViewById(R.id.btnX);

        btnDeleteAllDatabases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = TESTINGActivity.this;
                context.deleteDatabase(DiaryEntrySQLLiteHelper.DATABASE_NAME);
                context.deleteDatabase(WeatherSQLiteHelper.DATABASE_NAME);
                context.deleteDatabase(NewsSQLiteHelper.DATABASE_NAME);
                context.deleteDatabase(LocationsSQLiteHelper.DATABASE_NAME);
            }
        });

        btnShowALLTestingDialogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TESTINGActivity.turnOnTestingMode = true;
            }
        });

        btnHideALLTestingDialogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TESTINGActivity.turnOnTestingMode= false;
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
