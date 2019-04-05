package com.example.joesp.aidiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PIFacet3PopupWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pifacet3_popup_window);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        TextView txtViewFacet0 = (TextView) findViewById(R.id.txtViewFacet0);
        TextView txtViewFacet1 = (TextView) findViewById(R.id.txtViewFacet1);
        TextView txtViewFacet2 = (TextView) findViewById(R.id.txtViewFacet2);
        TextView txtViewFacet3 = (TextView) findViewById(R.id.txtViewFacet3);
        TextView txtViewFacet4 = (TextView) findViewById(R.id.txtViewFacet4);

        txtViewFacet0.setText(PersonalityInsightsPopupWindow.facetName_03 + " : " + PersonalityInsightsPopupWindow.facetPercentileScore_03);
        txtViewFacet1.setText(PersonalityInsightsPopupWindow.facetName_13 + " : " + PersonalityInsightsPopupWindow.facetPercentileScore_13);
        txtViewFacet2.setText(PersonalityInsightsPopupWindow.facetName_23 + " : " + PersonalityInsightsPopupWindow.facetPercentileScore_23);
        txtViewFacet3.setText(PersonalityInsightsPopupWindow.facetName_33 + " : " + PersonalityInsightsPopupWindow.facetPercentileScore_33);
        txtViewFacet4.setText(PersonalityInsightsPopupWindow.facetName_43 + " : " + PersonalityInsightsPopupWindow.facetPercentileScore_43);

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
                finish();
            }
        });
    }
}
