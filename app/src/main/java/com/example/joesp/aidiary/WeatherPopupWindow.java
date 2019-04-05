package com.example.joesp.aidiary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class WeatherPopupWindow extends DiaryEntryActivity {

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

        //Set time to display on top of page
        Date date = new Date();
        txtViewCurrentDateTime.setText(date.toString());

        if (date.equals(date)) {
            //Call JSON data from openweatherapi.org endpoint
            WeatherDownloadAPIData task = new WeatherDownloadAPIData();
            task.execute("http://api.openweathermap.org/data/2.5/weather?lat=53.350140&lon=-6.266155&appid=24b33237d7920999ebb42bedc591472c");
        } else {
            setHistoricalWeatherLayout();
        }

        //Button to close popup window
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeatherPopupWindow.this, DiaryEntryActivity.class));
            }
        });
    }

    //Collect the historical weather data from the database and set it on the weather layout.
    private void setHistoricalWeatherLayout() {

        WeatherSQLiteHelper db = new WeatherSQLiteHelper(WeatherPopupWindow.this);
        //Set up a List to collect the rows/tuples in the database table
        List<Weather> allWeatherEntries = db.getAllWeather();

        //Iterate through the database table rows and if they match pull that rows attributes
        // and display it on the diary entry TextView.
        for (int i = 0; i < allWeatherEntries.size(); ++i) {
            String date = allWeatherEntries.get(i).getDisplayDate();
            String country = allWeatherEntries.get(i).getCountry();
            String area = allWeatherEntries.get(i).getArea();
            String weatherDescription = allWeatherEntries.get(i).getWeatherDescription();
            String weatherImage = allWeatherEntries.get(i).getWeatherImage();
            String temperature = allWeatherEntries.get(i).getTemperature();
            String windSpeed = allWeatherEntries.get(i).getWindSpeed();
            String pressure = allWeatherEntries.get(i).getPressure();
            String humidity = allWeatherEntries.get(i).getHumidity();
            String visibility = allWeatherEntries.get(i).getVisibility();
            String sunrise = allWeatherEntries.get(i).getSunrise();
            String sunset = allWeatherEntries.get(i).getSunset();

            //Listed as displayed on activity
             txtViewNameOfCountry.setText("Country: " + country + "running historical date");
             txtViewNameOfArea.setText(area);
             txtViewWeatherDescription.setText(weatherDescription);
            //Set the Double to an int, set int to String, then add degree symbol, and add "C".
             txtViewTemperature.setText(temperature);//String.valueOf((temp.intValue())+""+(char) 0x00B0)+"Celsius");//temperature/*.intValue())+""+(char) 0x00B0)*/+"Celsius")));//degree symbol researched this link: https://stackoverflow.com/questions/3439517/android-set-degree-symbol-to-textview
             txtViewWindSpeed.setText("Wind speed:  " + windSpeed + " km/h");
             txtViewPressure.setText("Pressure:  " + pressure + " mBar");
             txtViewHumidity.setText(getString(R.string.Humidity) + humidity + " percent");
             txtViewVisibility.setText("Visibility:  " + visibility + " metres");
             txtViewSunrise.setText(getString(R.string.Sunrise) + sunrise);
             txtViewSunset.setText(getString(R.string.Sunset) + sunset);
        }
    }

    /**
     * AsyncTask for connecting to www.openweatherapp.org
     * Created by joesp on 24/10/2017.
     */

    private class WeatherDownloadAPIData extends AsyncTask<String, Void, String> {

        private String mainTempMin;
        private String mainTempMax;

        Date date;
          String sysCountry;
          String area;
          String weatherDescription;
          Double mainTemperature;
          String windSpeed;
          String mainPressure;
          String mainHumidity;
          String visibility;
          Date epochSunriseToHumanDate;
          Date epochSunsetToHumanDate;
          String weatherId;
          boolean runTheSetLayoutMethod = true;

        //Run the background process
        protected String doInBackground(String... urls){

            //Set up the internet connection with the remote server
            String result="";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            }catch (Exception e){
                e.printStackTrace();
            }

            return  null;

        }

        //Run after execution is complete
        @Override
        protected  void onPostExecute(String result){
            super.onPostExecute(result);

            try{

//            example of JSON data from openweatherapp.org
//            {"coord":{"lon":-6.27,"lat":53.35},
//                "weather":  [{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}],
//                "base":  "stations",
//                "main":  {"temp":284.58,"pressure":1020,"humidity":81,"temp_min":284.15,"temp_max":285.15},
//                "visibility":  10000,
//                "wind":  {"speed":3.6,"deg":210},
//                "clouds":  {"all":75},
//                "dt":  1508956200,
//                "sys":  {"type":1,"id":5237,"message":0.0195,"country":"IE","sunrise":1508915  530,"sunset":1508951101},
//                "id":  2964574,
//                "name":  "Dublin",
//                "cod":  200}
//

                JSONObject jsonObject = new JSONObject(result);

                //Get JSON object data from: "weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}],
                JSONArray weather = jsonObject.getJSONArray("weather");
                JSONObject weatherIndex = weather.getJSONObject(0);
                weatherId = weatherIndex.getString("id");

                weatherDescription = weatherIndex.getString("description");

                //Get JSON object data from: "main":{"temp":284.58,"pressure":1020,"humidity":81,"temp_min":284.15,"temp_max":285.15},
                JSONObject main = new JSONObject(jsonObject.getString("main"));
                mainTemperature = Double.parseDouble(main.getString("temp"));
                //Convert kelvin to celcius: http://www.rapidtables.com/convert/temperature/how-kelvin-to-celsius.htm
                mainTemperature = mainTemperature - 273.15;
                mainPressure = main.getString("pressure");
                mainHumidity = main.getString("humidity");
                mainTempMin = main.getString("temp_min");//not used
                mainTempMax = main.getString("temp_max");//not used
                //Get JSON object data from: "visibility":10000,
                visibility = jsonObject.getString("visibility");

                //Get JSON object data from: "wind": {"speed":3.6,"deg":210}
                JSONObject wind = new JSONObject(jsonObject.getString("wind"));
                windSpeed =  wind.getString("speed");

                //Get JSON object data from: "sys": {"type":1,"id":5237,"message":0.0195,"country":"IE","sunrise":1508915  530,"sunset":1508951101}
                JSONObject sys = new JSONObject(jsonObject.getString("sys"));
                sysCountry = sys.getString("country");
                //also parse the epoch time into human readable time.
                String sysSunrise = sys.getString("sunrise");
                epochSunriseToHumanDate = new Date(Long.parseLong(sysSunrise) * 1000);
                String sysSunset = sys.getString("sunset");
                epochSunsetToHumanDate = new Date(Long.parseLong(sysSunset) * 1000);

                //Get JSON object data from:  "name": "Dublin"
                area = jsonObject.getString("name");

                //this variable has been set to true from the button onClickListener method on the DiaryEntryActivity.java
                if(runTheSetLayoutMethod == true){
                    setWeatherLayout();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Set the data for the Weather popup window layout
        private void setWeatherLayout(){

            //Listed as displayed on activity
             txtViewNameOfCountry.setText("Country: "+sysCountry);
             txtViewNameOfArea.setText(area);
             txtViewWeatherDescription.setText(weatherDescription);
            //Set the Double to an int, set int to String, then add degree symbol, and add "C".
             txtViewTemperature.setText(String.valueOf((mainTemperature.intValue())+""+(char) 0x00B0)+"Celsius");//degree symbol researched this link: https://stackoverflow.com/questions/3439517/android-set-degree-symbol-to-textview
             txtViewWindSpeed.setText("Wind speed:  "+windSpeed+" km/h");
             txtViewPressure.setText("Pressure:  "+mainPressure+" mBar");
             txtViewHumidity.setText("Humidity:  "+mainHumidity+" percent");
             txtViewVisibility.setText("Visibility:  "+visibility+" metres");
             txtViewSunrise.setText("Sunrise:  "+epochSunriseToHumanDate.toString());
             txtViewSunset.setText("Sunset:  "+epochSunsetToHumanDate.toString());

            //Set up the dynamic weather icon/image for display with weather  description
            switch (weatherId){
                case "200": // 200	thunderstorm with light rain
                case "201": // 201	thunderstorm with rain
                case "202": //202	thunderstorm with heavy rain
                case "210": //210	light thunderstorm
                case "211": //211	thunderstorm
                case "212": //212	heavy thunderstorm
                case "221": //221	ragged thunderstorm
                case "230": //230	thunderstorm with light drizzle
                case "231": //231	thunderstorm with drizzle
                case "232":   imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_night_stormy);
                    break;
                case "300": //300	light intensity drizzle
                case "301": //301	drizzle
                case "302": //302	heavy intensity drizzle
                case "310": //310	light intensity drizzle rain
                case "311": //311	drizzle rain
                case "312": //312	heavy intensity drizzle rain
                case "313": //313	shower rain and drizzle
                case "314": //314	heavy shower rain and drizzle
                case "321": imageViewWeatherIcon.setImageResource(R.drawable.weather_icon_rainy);
                    break;
                case "500": //500	light rain
                case "501": //501 moderate rain
                case "502":	//502 heavy intensity rain
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
                default:  imageViewWeatherIcon.setImageResource(R.drawable.weather);
            }

            runTheSetLayoutMethod = false;
        }
    }
}

