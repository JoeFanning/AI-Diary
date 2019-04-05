package com.example.joesp.aidiary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HistoricalLocationPopupWindow extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView txtViewLastUpdatedTime;
    private TextView txtViewLatitude;
    private TextView txtViewLongitude;
    String longitude = "";
    String latitude = "";
    String lastLocationUpdateTime = "";
    int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_popup_window);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //set the map pointer
        mapFragment.getMapAsync(this);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        txtViewLongitude = (TextView) findViewById(R.id.txtViewLogitude);
        txtViewLatitude = (TextView) findViewById(R.id.txtViewLatitude);
        txtViewLastUpdatedTime = (TextView) findViewById(R.id.txtViewLastUpdatedTime);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);
        int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case 100%/10*9 = 90%
        getWindow().setLayout(width / 10 * 9, height / 10 * 9);

        //Iterate through the locations database table rows
        LocationsSQLiteHelper locationsDb = new LocationsSQLiteHelper(HistoricalLocationPopupWindow.this);
        List<Locations> allLocationsRows = locationsDb.getAllLocations();

        //Iterate through the locations database table rows and if the date matches the historical date set the
        //HistoricalLocationPopupWindow.java layout with this rows data.
        for (int i = 0; i < allLocationsRows.size(); ++i) {
            if (HistoricalDiaryEntryActivity.dateToFindInDataBase == allLocationsRows.get(i).getDate()) {
                date = allLocationsRows.get(i).getDate();
                longitude = allLocationsRows.get(i).getLongitude();
                latitude = allLocationsRows.get(i).getLatitude();
                lastLocationUpdateTime = allLocationsRows.get(i).getLastLocationUpdateTime();
                //Listed as displayed on activity
                txtViewLongitude.setText("Longitude: " + 53.34);
                txtViewLatitude.setText("Latitude: " + -6.2603);
                txtViewLastUpdatedTime.setText("Last updated time: " + "16:50");
            }
        }

        //Button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng latLng = new LatLng(53.34, -6.2603);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}


