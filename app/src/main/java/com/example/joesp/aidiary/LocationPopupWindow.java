package com.example.joesp.aidiary;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;

/**
 * Popup window for Location button in DiaryEntryActivity
 * I have also applied a custom theme for this popup in values/styles
 * <p>
 * Created by joesp on 20/10/2017.
 */

public class LocationPopupWindow extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Code used in requesting runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    //related to the pop up dialog for asking the user to modify settings
    //https://stackoverflow.com/questions/31572323/what-is-the-value-of-request-check-settings
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    //The desired interval for location updates. Inexact. Updates may be more or less frequent.
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    //The fastest rate for active location updates. Exact. Updates will never be more frequent
    //than this value.
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    //Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    //Provides access to the Fused Location Provider API.
    private FusedLocationProviderClient mFusedLocationClient;

    //Provides access to the Location Settings API.
    private SettingsClient mSettingsClient;

    //Stores parameters for requests to the FusedLocationProviderApi.
    private LocationRequest mLocationRequest;

    //Stores the types of location services the client is interested in using. Used for checking
    // settings to determine if the device has optimal location settings.
    private LocationSettingsRequest mLocationSettingsRequest;

    //Callback for Location events.
    private LocationCallback mLocationCallback;

    private Location mCurrentLocation;

    private TextView txtViewLastUpdatedTime;
    private TextView txtViewLatitude;
    private TextView txtViewLongitude;

    //https://developer.android.com/training/location/receive-location-updates.html
    // refers to a boolean flag, mRequestingLocationUpdates, used to track whether the
    // user has turned location updates on or off.
    private Boolean mRequestingLocationUpdates;

    // Time when the location was updated represented as a String.
    private String mLastUpdateTime;

    //Used for Log debugging
    private static final String TAG = "HistoricalLocationPopupWindow: ";

    //Class for setting longitude and latitude for Google map marker
    LatLng dynamicLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_popup_window);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

        //button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        startLocationUpdates();
        Toast.makeText(LocationPopupWindow.this, "Location updates started", Toast.LENGTH_SHORT).show();
        //stopLocationUpdates();

    }

    // Updates fields based on data stored in the bundle.
    //@param savedInstanceState The activity state saved in the Bundle.
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    //STEP 1
    // Set Up a Location Request
    //this is the first step for getting location updates
    //This Google Location Services API,
    // part of Google Play Services, provides a more powerful, high-level framework than android.Location

    //https://developer.android.com/training/location/change-location-settings.html#location-request
    //To store parameters for requests to the fused location provider, create a LocationRequest.
    // The parameters determine the level of accuracy for location requests.
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        //This method sets the rate in milliseconds at which your app prefers
        // to receive location updates. Note that the location updates may be
        // faster than this rate if another app is receiving updates at a faster
        // rate, or slower than this rate, or there may be no updates at all (if
        // the device has no connectivity, for example).
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        //// This method sets the fastest rate in milliseconds at which your app can
        // handle location updates. You need to set this rate because other apps
        // also affect the rate at which updates are sent.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        //This method sets the priority of the request, which gives the Google Play
        // services location services a strong hint about which location sources to use.
        //PRIORITY_HIGH_ACCURACY - Use this setting to request the most precise location possible.
        // With this setting, the location services are more likely to use GPS to determine the location.
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //STEP 5
    //https://developer.android.com/training/location/receive-location-updates.html#stop-updates
    //The fused location provider invokes the LocationCallback.onLocationChanged() callback method. The incoming
    // argument contains a list Location object containing the location's latitude and longitude. The following
    // snippet shows how to implement the LocationCallback interface and define the method, then get the timestamp
    // of the location update and display the latitude, longitude and timestamp on your app's user interface:
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //set the longitude and latitude for the pointer/marker
                double longitude = locationResult.getLastLocation().getLongitude();
                double latitude = locationResult.getLastLocation().getLatitude();

                txtViewLongitude.setText(getString(R.string.longitude) +" "+ String.valueOf(longitude));
                txtViewLatitude.setText(getString(R.string.latitude) +" "+ String.valueOf(latitude));

                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                txtViewLastUpdatedTime.setText(getString(R.string.lastupdatedtime) +" "+mLastUpdateTime);

                dynamicLocationMarker = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(dynamicLocationMarker).title("Marker updates with every location update"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(dynamicLocationMarker));
            }
        };
    }

    //STEP 2
    //Get Current Location Settings
    //Once you have connected to Google Play services and the location services API,
    // you can get the current location settings of a user's device. To do this, create
    // a LocationSettingsRequest.Builder, and add one or more location requests.
    // Use location request that was created in Step 1:
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        System.out.println("User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        System.out.println("User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    //STEP 3
    //Prompt the User to Change Location Settings
    //To determine whether the location settings are appropriate for the location request,
    // check the status code from the LocationSettingsResponse object. A status code of RESOLUTION_REQUIRED
    // indicates that the settings must be changed. To prompt the user for permission to modify the location
    // settings, call startResolutionForResult(Activity, int). This method brings up a dialog asking for the
    // user's permission to modify location settings. The following code snippet shows how to check the
    // location settings, and how to call startResolutionForResult(Activity, int).
    private void startLocationUpdates() {

        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocationPopupWindow.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    System.out.println("PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(LocationPopupWindow.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    // Stores activity data in the Bundle.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            //Displaying permission rationale to provide additional context.
            //Location permission is needed for core functionality
            AlertDialog alertDialog = new AlertDialog.Builder(LocationPopupWindow.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("" + R.string.permission_rationale);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Request permission
                            ActivityCompat.requestPermissions(LocationPopupWindow.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
            alertDialog.show();

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LocationPopupWindow.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    // Callback received when a permissions request has been completed.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                //User interaction was cancelled.;
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    //Permission granted, updates requested, starting location updates;
                    startLocationUpdates();
                }
            } else {
                // Permission denied.
                // Build intent that displays the App settings screen.
                Intent intent = new Intent();
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
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

        //Map marker is not set here instead it is set in the createLocationCallback() method, so the
        // marker will change position with every location update.

    }
}

