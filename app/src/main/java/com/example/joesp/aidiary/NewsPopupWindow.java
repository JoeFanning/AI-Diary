package com.example.joesp.aidiary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsPopupWindow extends DiaryEntryActivity {

      TextView txtViewNewsArticleTitle1;
      TextView txtViewNewsArticleDescription1;
      ImageView imageViewNewsArticle1;
      TextView txtViewNewsArticleTitle2;
      TextView txtViewNewsArticleDescription2;
      ImageView imageViewNewsArticle2;
      TextView txtViewNewsArticleTitle3;
      TextView txtViewNewsArticleDescription3;
      ImageView imageViewNewsArticle3;
      TextView txtViewNewsArticleTitle4;
      TextView txtViewNewsArticleDescription4;
      ImageView imageViewNewsArticle4;
      TextView txtViewNewsArticleTitle5;
      TextView txtViewNewsArticleDescription5;
      ImageView imageViewNewsArticle5;

    String imageNewsArticle1;
    String imageNewsArticle2;
    String imageNewsArticle3;
    String imageNewsArticle4;
    String imageNewsArticle5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_popup_window);

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
        //used for dynamic weather icon
        imageViewNewsArticle1 = (ImageView) findViewById(R.id.imgViewNewsArticleImage1);
        imageViewNewsArticle2 = (ImageView) findViewById(R.id.imgViewNewsArticleImage2);
        imageViewNewsArticle3 = (ImageView) findViewById(R.id.imgViewNewsArticleImage3);
        imageViewNewsArticle4 = (ImageView) findViewById(R.id.imgViewNewsArticleImage4);
        imageViewNewsArticle5 = (ImageView) findViewById(R.id.imgViewNewsArticleImage5);

        //Set up connection to www.newsapi.org
        NewsDownloadAPIData task = new NewsDownloadAPIData();
        task.execute("https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=d232f226cdfd49ad90597fc0064a72b7");

        Button btnClose = (Button) findViewById(R.id.btnClose);

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
                startActivity(new Intent(NewsPopupWindow.this, DiaryEntryActivity.class));
            }
        });
    }

    //AsyncTask for connecting to www.newsapi.org API.
    private class NewsDownloadAPIData extends AsyncTask<String, Void, String> {

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

                //Set the layout for NewsPopupWindow
                JSONObject jsonObject = new JSONObject(result);
                JSONArray news = jsonObject.optJSONArray("articles");
                //News article 1
                JSONObject data1 = news.optJSONObject(1);
                String newsArticleTitleJSON1 = data1.optString("title");
                String newsArticleDescriptionJSON1 = data1.optString("description");
                txtViewNewsArticleTitle1.setText(newsArticleTitleJSON1);
                txtViewNewsArticleDescription1.setText(newsArticleDescriptionJSON1);
                imageNewsArticle1 = data1.optString("urlToImage");
                //News article 2
                JSONObject data2 = news.optJSONObject(2);
                String newsArticleTitleJSON2 = data2.optString("title");
                String newsArticleDescriptionJSON2 = data2.optString("description");
                txtViewNewsArticleTitle2.setText(newsArticleTitleJSON2);
                txtViewNewsArticleDescription2.setText(newsArticleDescriptionJSON2);
                imageNewsArticle2 = data2.optString("urlToImage");
                //News article 3
                JSONObject data3 = news.optJSONObject(3);
                String newsArticleTitleJSON3 = data3.optString("title");
                String newsArticleDescriptionJSON3 = data3.optString("description");
                txtViewNewsArticleTitle3.setText(newsArticleTitleJSON3);
                txtViewNewsArticleDescription3.setText(newsArticleDescriptionJSON3);
                imageNewsArticle3 = data3.optString("urlToImage");
                //News article 4
                JSONObject data4 = news.optJSONObject(4);
                String newsArticleTitleJSON4 = data4.optString("title");
                String newsArticleDescriptionJSON4 = data4.optString("description");
                txtViewNewsArticleTitle4.setText(newsArticleTitleJSON4);
                txtViewNewsArticleDescription4.setText(newsArticleDescriptionJSON4);
                imageNewsArticle4 = data4.optString("urlToImage");
                ////News article 5
                JSONObject data5 = news.optJSONObject(5);
                String newsArticleTitleJSON5 = data5.optString("title");
                String newsArticleDescriptionJSON5 = data5.optString("description");
                txtViewNewsArticleTitle5.setText(newsArticleTitleJSON5);
                txtViewNewsArticleDescription5.setText(newsArticleDescriptionJSON5);
                imageNewsArticle5 = data5.optString("urlToImage");

                //https://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en
                //Glide, Image Loader Library for Android, recommended by Google
                //Used to load the newsapi.org JSON file images into the ImageView's
                Glide.with(NewsPopupWindow.this)
                        .load(imageNewsArticle1)
                        .into(imageViewNewsArticle1);
                Glide.with(NewsPopupWindow.this)
                        .load(imageNewsArticle2)
                        .into(imageViewNewsArticle2);
                Glide.with(NewsPopupWindow.this)
                        .load(imageNewsArticle3)
                        .into(imageViewNewsArticle3);
                Glide.with(NewsPopupWindow.this)
                        .load(imageNewsArticle4)
                        .into(imageViewNewsArticle4);
                Glide.with(NewsPopupWindow.this)
                        .load(imageNewsArticle5)
                        .into(imageViewNewsArticle5);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

