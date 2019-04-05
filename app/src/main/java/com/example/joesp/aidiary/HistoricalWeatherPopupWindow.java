package com.example.joesp.aidiary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HistoricalWeatherPopupWindow extends HistoricalDiaryEntryActivity {

    //declared as for position on activity
    TextView txtViewCurrentDateTime;
    TextView txtViewNameOfCountry;
    TextView txtViewNameOfArea;
    TextView txtViewWeatherDescription;
    ImageView imageViewWeatherIcon;
    TextView txtViewTemperature;
    TextView txtViewWindSpeed;
    TextView txtViewPressure;
    TextView txtViewHumidity;
    TextView txtViewVisibility;
    TextView txtViewSunrise;
    TextView txtViewSunset;
    int date;
    String displayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_popup_window);

        txtViewCurrentDateTime = (TextView) findViewById(R.id.txtViewCurrentDateTime);
        txtViewNameOfCountry = (TextView) findViewById(R.id.txtViewNameOfCountry);
        txtViewNameOfArea = (TextView) findViewById(R.id.txtViewNameOfArea);
        imageViewWeatherIcon = (ImageView) findViewById(R.id.imgViewNewsArticleImage3);
        txtViewWeatherDescription = (TextView) findViewById(R.id.txtViewWeatherDescription);
        txtViewTemperature = (TextView) findViewById(R.id.txtViewTemperature);
        txtViewWindSpeed = (TextView) findViewById(R.id.txtViewWindSpeed);
        txtViewPressure = (TextView) findViewById(R.id.txtViewPressure);
        txtViewHumidity = (TextView) findViewById(R.id.txtViewHumidity);
        txtViewVisibility = (TextView) findViewById(R.id.txtTextViewVisibility);
        txtViewSunrise = (TextView) findViewById(R.id.txtViewSunrise);
        txtViewSunset = (TextView) findViewById(R.id.txtViewSunset);
        Button btnClose = (Button) findViewById(R.id.btnClose);

        //Set size of popup window
        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;
        //The number in the argument is the percent size compared to screen size
        //in this case 100%/10*9 = 90%
        getWindow().setLayout(width / 10 * 9, height / 10 * 9);

        //Iterate through the weather database table rows
        WeatherSQLiteHelper weatherDb = new WeatherSQLiteHelper(HistoricalWeatherPopupWindow.this);
        List<Weather> allWeatherRows = weatherDb.getAllWeather();

        if(TESTINGActivity.turnOnTestingMode == true) {
            //TESTING
            if (!allWeatherRows.isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(HistoricalWeatherPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Number of rows in Weather table: " + allWeatherRows.size()
                        + "\n\nWeather row 0 date = " + allWeatherRows.get(0).getDate() + ",  displayDate = " + allWeatherRows.get(0).getDisplayDate()
                        + "\nsunrise = " + allWeatherRows.get(0).getSunrise()
                        + "\nsunset = " + allWeatherRows.get(0).getSunset());
//                     +"\nWeather row 1 date = "+allWeatherRows.get(1).getDate()+",  displayDate = "+allWeatherRows.get(1).getDisplayDate()
//                     +"\nWeather row 2 date: "+allWeatherRows.get(2).getDate()+",  displayDate: "+allWeatherRows.get(2).getDisplayDate()
//                     +"\nWeather row 3 date: "+allWeatherRows.get(3).getDate()+",  displayDate: "+allWeatherRows.get(3).getDisplayDate());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(HistoricalWeatherPopupWindow.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Number of rows in weather table = " + allWeatherRows.size());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            //TESTING END
        }

        //iterate through the weather database table rows and if the date matches the historical date set the
        //HistoricalWeatherPopupWindow.java layout with this rows data.
        boolean databaseDateFound = false;
        for (int i = 0; i < allWeatherRows.size(); ++i) {
            if (HistoricalDiaryEntryActivity.dateToFindInDataBase == allWeatherRows.get(i).getDate()) {
                date = allWeatherRows.get(i).getDate();
                displayDate = allWeatherRows.get(i).getDisplayDate();
                String country = allWeatherRows.get(i).getCountry();
                String area = allWeatherRows.get(i).getArea();
                String weatherDescription = allWeatherRows.get(i).getWeatherDescription();
                String weatherImage = allWeatherRows.get(i).getWeatherImage();
                String temperature = allWeatherRows.get(i).getTemperature();
                String windSpeed = allWeatherRows.get(i).getWindSpeed();
                String pressure = allWeatherRows.get(i).getPressure();
                String humidity = allWeatherRows.get(i).getHumidity();
                String visibility = allWeatherRows.get(i).getVisibility();
                String sunrise = allWeatherRows.get(i).getSunrise();
                String sunset = allWeatherRows.get(i).getSunset();

                //Listed as displayed on activity
                txtViewCurrentDateTime.setText(displayDate);
                txtViewNameOfCountry.setText(getString(R.string.country) +" "+ country);
                txtViewNameOfArea.setText(area);
                txtViewWeatherDescription.setText(weatherDescription);
                txtViewTemperature.setText(temperature.substring(0, 3) + ((char) 0x00B0) + getString(R.string.celcius));//degree symbol researched this link: https://stackoverflow.com/questions/3439517/android-set-degree-symbol-to-textview
                txtViewWindSpeed.setText(getString(R.string.windspeed)+" " + windSpeed + " km/h");
                txtViewPressure.setText(getString(R.string.pressure) +" "+ pressure + " mBar");
                txtViewHumidity.setText(getString(R.string.humidity) +" "+ humidity + getString(R.string.percent));
                txtViewVisibility.setText(getString(R.string.visibility)+" " + visibility + getString(R.string.meters));
                txtViewSunrise.setText(getString(R.string.sunrise)+" " + sunrise);
                txtViewSunset.setText(getString(R.string.sunset) +" "+ sunset);

                //Set up the dynamic weather icon/image for display with weather  description
                switch (weatherImage) {
                    case "200": // 200	thunderstorm with light rain
                    case "201": // 201	thunderstorm with rain
                    case "202": //202	thunderstorm with heavy rain
                    case "210": //210	light thunderstorm
                    case "211": //211	thunderstorm
                    case "212": //212	heavy thunderstorm
                    case "221": //221	ragged thunderstorm
                    case "230": //230	thunderstorm with light drizzle
                    case "231": //231	thunderstorm with drizzle
                    case "232":
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_night_stormy);
                        break;
                    case "300": //300	light intensity drizzle
                    case "301": //301	drizzle
                    case "302": //302	heavy intensity drizzle
                    case "310": //310	light intensity drizzle rain
                    case "311": //311	drizzle rain
                    case "312": //312	heavy intensity drizzle rain
                    case "313": //313	shower rain and drizzle
                    case "314": //314	heavy shower rain and drizzle
                    case "321":
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_rainy);
                        break;
                    case "500": //500	light rain
                    case "501": //501 moderate rain
                    case "502":    //502 heavy intensity rain
                    case "503": //503	very heavy rain
                    case "504": //504	extreme rain
                    case "511": //511	freezing rain
                    case "520": //520	light intensity shower rain
                    case "521": //521	shower rain
                    case "522": //522	heavy intensity shower rain
                    case "531": //531	ragged shower rain
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_rainy2);
                        break;
                    case "600": //600	light snow
                    case "601": //601	Snow
                    case "602": //602	heavy snow
                    case "611": //611	Sleet
                    case "612": //612	shower sleet
                    case "615": //615	light rain and snow
                    case "616": //616	rain and snow
                    case "620": //620	light shower snow
                    case "621": //621	shower snow
                    case "622": //622	heavy shower snow
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_snowy);
                        break;
                    case "701": //701	Mist
                    case "711": //711	Smoke
                    case "721": //721	Haze
                    case "731": //731	sand, dust whirls
                    case "741": //741	Fog
                    case "751": //751	Sand
                    case "761": //761	Dust
                    case "762": //762	volcanic ash
                    case "771": //771	Squalls
                    case "781": //781	Tornado
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_cloudy3);
                        break;
                    case "800": //800 clear sky
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_night_clear);
                        break;
                    case "801": //801	few clouds
                    case "802": //802	scattered clouds
                    case "803": //803	broken clouds
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_cloudy);
                        break;
                    case "804": //804	overcast clouds
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_cloudy3);
                        break;
                    case "900": //900	tornado
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_tornado);
                        break;
                    case "901": //901	tropical storm
                    case "902": //902	hurricane
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_stormy);
                        break;
                    case "903": //903	Cold
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_cold);
                        break;
                    case "904": //904	Hot
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_sunny);
                        break;
                    case "905": //905	windy
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_windy_weather);
                        break;
                    case "906": //906	Hail
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_rainy2);
                        break;
                    case "951": //951	Calm
                    case "952": //952	light breeze
                    case "953": //953	gentle breeze
                    case "954": //954	moderate breeze
                    case "955": //955	fresh breeze
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_calm_breeze);
                        break;
                    case "956": //956	strong breeze
                    case "957": //957	high wind, near gale
                    case "958": //958	Gale
                    case "959": //959	severe gale
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_windy_weather);
                        break;
                    case "960": //960	Storm
                    case "961": //961	violent storm
                    case "962": //962	Hurricane
                        imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_night_stormy);
                        break;
                    default:
                        imageViewWeatherIcon.setImageResource(R.drawable.weather);
                }
                databaseDateFound = true;
                break;
            } else {
                if (databaseDateFound == false) {
                    txtViewWeatherDescription.setText(R.string.no_weather_documented_for_this_date);
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

    }//end of onCreate()
}


