package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoricalNewsPopupWindow extends DiaryEntryActivity {

    static TextView txtViewNewsArticleTitle1;
    static TextView txtViewNewsArticleDescription1;
    static TextView txtViewNewsArticleTitle2;
    static TextView txtViewNewsArticleDescription2;
    static TextView txtViewNewsArticleTitle3;
    static TextView txtViewNewsArticleDescription3;
    static TextView txtViewNewsArticleTitle4;
    static TextView txtViewNewsArticleDescription4;
    static TextView txtViewNewsArticleTitle5;
    static TextView txtViewNewsArticleDescription5;
    int date;
    String displayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_news_popup_window);

        txtViewNewsArticleTitle1 = (TextView) findViewById(R.id.txtViewNewsTitle1);
        txtViewNewsArticleDescription1 = (TextView) findViewById(R.id.txtViewNewsArticleDescription1);
        txtViewNewsArticleTitle2 = (TextView) findViewById(R.id.txtViewNewsTitle2);
        txtViewNewsArticleDescription2 = (TextView) findViewById(R.id.txtViewNewsDescription2);
        txtViewNewsArticleTitle3 = (TextView) findViewById(R.id.txtViewNewsTitle3);
        txtViewNewsArticleDescription3 = (TextView) findViewById(R.id.txtViewNewsDescription3);
        txtViewNewsArticleTitle4 = (TextView) findViewById(R.id.txtViewNewsTitle4);
        txtViewNewsArticleDescription4 = (TextView) findViewById(R.id.txtViewNewsDescription4);
        txtViewNewsArticleTitle5 = (TextView) findViewById(R.id.txtViewNewsTitle5);
        txtViewNewsArticleDescription5 = (TextView) findViewById(R.id.txtViewNewsDescription5);
        Button btnClose = (Button) findViewById(R.id.btnClose);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);
        int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case 100%/10*9 = 90%
        getWindow().setLayout(width / 10 * 9, height / 10 * 9);

        NewsSQLiteHelper db = new NewsSQLiteHelper(HistoricalNewsPopupWindow.this);
        //Set up a List to collect the rows/tuples in the database table
        List<News> allNewsRows = db.getAllNews();

        if (TESTINGActivity.turnOnTestingMode == true) {
            //TESTING
            if (!allNewsRows.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(HistoricalNewsPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("HistoricalNewsPopupWindow.java"
                        + "\n\nNo of News tables = " + allNewsRows.size()
                        + "\n\ndateToFindInDataBase = " + HistoricalDiaryEntryActivity.dateToFindInDataBase
                        + " historical_displayDate =" + HistoricalDiaryEntryActivity.historical_DisplayDate + "\n"
                        + "\nDate 0 = " + allNewsRows.get(0).getDate() + " Display date = " + allNewsRows.get(0).getDisplayDate());
                //                        +"\nDate 1 = "+allNewsRows.get(1).getDate()+" Display date =  "+allNewsRows.get(1).getDisplayDate()
                //                        +"\nDate 2 = "+allNewsRows.get(2).getDate()+" Display date =  "+allNewsRows.get(2).getDisplayDate()
                //                        +"\nDate 3 = "+allNewsRows.get(3).getDate()+" Display date =  "+allNewsRows.get(3).getDisplayDate()
                //                        +"\nDate 4 = "+allNewsRows.get(4).getDate()+" Display date =  "+allNewsRows.get(4).getDisplayDate()
                //                        +"\nDate 5 = "+allNewsRows.get(5).getDate()+" Display date =  "+allNewsRows.get(5).getDisplayDate());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(HistoricalNewsPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("HistoricalNewsPopupWindow.java \n\n" +
                        "Number of rows in news table = " + allNewsRows.size());
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

        //Iterate through the database table rows and if they match pull that rows attributes
        // and display it on the news entry TextView.
        boolean databaseDateFound = false;
        for (int i = 0; i < allNewsRows.size(); ++i) {
            if (HistoricalDiaryEntryActivity.dateToFindInDataBase == allNewsRows.get(i).getDate()) {
                date = allNewsRows.get(i).getDate();
                displayDate = allNewsRows.get(i).getDisplayDate();
                String articleTitle1 = allNewsRows.get(i).getArticleTitle1();
                String articleTitle2 = allNewsRows.get(i).getArticleTitle2();
                String articleTitle3 = allNewsRows.get(i).getArticleTitle3();
                String articleTitle4 = allNewsRows.get(i).getArticleTitle4();
                String articleTitle5 = allNewsRows.get(i).getArticleTitle5();
                String articleDescription1 = allNewsRows.get(i).getArticleDescription1();
                String articleDescription2 = allNewsRows.get(i).getArticleDescription2();
                String articleDescription3 = allNewsRows.get(i).getArticleDescription3();
                String articleDescription4 = allNewsRows.get(i).getArticleDescription4();
                String articleDescription5 = allNewsRows.get(i).getArticleDescription5();

                //Listed as displayed on activity
                txtViewNewsArticleTitle1.setText(articleTitle1);
                txtViewNewsArticleDescription1.setText(articleDescription1);
                txtViewNewsArticleTitle2.setText(articleTitle2);
                txtViewNewsArticleDescription2.setText(articleDescription2);
                txtViewNewsArticleTitle3.setText(articleTitle3);
                txtViewNewsArticleDescription3.setText(articleDescription3);
                txtViewNewsArticleTitle4.setText(articleTitle4);
                txtViewNewsArticleDescription4.setText(articleDescription4);
                txtViewNewsArticleTitle5.setText(articleTitle5);
                txtViewNewsArticleDescription5.setText(articleDescription5);
                databaseDateFound = true;
                break;
            } else {
                if (databaseDateFound == false) {
//                    Toast.makeText(HistoricalNewsPopupWindow.this, "Number of rows in News table: " + allNewsRows.size() +
//                                    "" + "News database row " + i + " date = " + allNewsRows.get(i).getDate()
//                                    + "  does not match HistoricalDiaryEntryActivity.dateToFindInDataBase: " + HistoricalDiaryEntryActivity.dateToFindInDataBase
//                            , Toast.LENGTH_LONG).show();
                }
            }
            databaseDateFound = false;
        }

        //Button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}



