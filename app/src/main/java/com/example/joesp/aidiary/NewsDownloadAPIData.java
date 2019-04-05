package com.example.joesp.aidiary;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask for connecting to www.newsapi.org
 * Created by joesp on 11/2017.
 */
class NewsDownloadAPIData extends AsyncTask<String, Void, String> {

    static String newsArticleTitle1;
    static String newsArticleDescription1;
    static String imageNewsArticle1;
    static String newsArticleTitle2;
    static String newsArticleDescription2;
    static String imageNewsArticle2;
    static String newsArticleTitle3;
    static String newsArticleDescription3;
    static String imageNewsArticle3;
    static String newsArticleTitle4;
    static String newsArticleDescription4;
    static String imageNewsArticle4;
    static String newsArticleTitle5;
    static String newsArticleDescription5;
    static String imageNewsArticle5;

    //Run background process
    @Override
    protected String doInBackground(String... urls) {

        //Set up the internet connection with the remote server
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    //Run after execution is complete
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
//            Format of JSON Response from newsapi.org
//            "articles": [
//            -{
//            "author": "BBC News",
//            "title": "Huge Barcelona rally for Spanish unity",
//            "description": "Many of the thousands of protesters chanted that the sacked Catalan leader should be jailed.",
//            "url": "http://www.bbc.co.uk/news/world-europe-41794087",
//            "urlToImage": "https://ichef.bbci.co.uk/images/ic/1024x576/p05lckhh.jpg",
//            "publishedAt": "2017-10-29T18:26:28Z"
//            },

            JSONObject jsonObject = new JSONObject(result);
            //JSONArray optJSONArray (String name)
            //Returns the values mapped by JSON values if it exists and is a JSONArray, or null otherwise.
                       /*Note if you add any data here delete the database before testing*/
            //Historical news article 1
            JSONArray news = jsonObject.optJSONArray("articles");
            JSONObject dataArticle1 = news.optJSONObject(1);
            String newsTitle1 = dataArticle1.optString("title");
            String newsDescription1 = dataArticle1.optString("description");
            newsArticleTitle1 = newsTitle1;
            newsArticleDescription1 = newsDescription1;
            imageNewsArticle1 = dataArticle1.optString("urlToImage");
            //Historical news article 2
            JSONObject dataArticle2 = news.optJSONObject(2);
            String newsTitle2 = dataArticle2.optString("title");
            String newsDescription2 = dataArticle2.optString("description");
            newsArticleTitle2 = newsTitle2;
            newsArticleDescription2 = newsDescription2;
            imageNewsArticle2 = dataArticle2.optString("urlToImage");
            //Historical news article 3
            JSONObject dataArticle3 = news.optJSONObject(3);
            String newsTitle3 = dataArticle3.optString("title");
            String newsDescription3 = dataArticle3.optString("description");
            newsArticleTitle3 = newsTitle3;
            newsArticleDescription3 = newsDescription3;
            imageNewsArticle3 = dataArticle3.optString("urlToImage");
            //Historical news article 4
            JSONObject dataArticle4 = news.optJSONObject(4);
            String newsTitle4 = dataArticle4.optString("title");
            String newsDescription4 = dataArticle4.optString("description");
            newsArticleTitle4 = newsTitle4;
            newsArticleDescription4 = newsDescription4;
            imageNewsArticle4 = dataArticle4.optString("urlToImage");
            //Historical news article 5
            JSONObject dataArticle5 = news.optJSONObject(5);
            String newsTitle5 = dataArticle5.optString("title");
            String newsDescription5 = dataArticle5.optString("description");
            newsArticleTitle5 = newsTitle5;
            newsArticleDescription5 = newsDescription5;
            imageNewsArticle5 = dataArticle5.optString("urlToImage");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

