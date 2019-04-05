package com.example.joesp.aidiary;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * AsyncTask for connecting to www.openweatherapp.org
 * Created by joesp on 24/10/2017.
 */
  class WeatherDownloadAPIData extends AsyncTask<String, Void, String> {

    Date date;
    static  String sysCountry;
    static  String area;
    static String weatherDescription;
    static Double mainTemperature;
    static String windSpeed;
    static String mainPressure;
    static String mainHumidity;
    static String visibility;
    static Date epochSunriseToHumanDate;
    static Date epochSunsetToHumanDate;
    static String weatherId;
    static boolean runTheSetLayoutMethod;

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

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
