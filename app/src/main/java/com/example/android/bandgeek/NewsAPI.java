package com.example.android.bandgeek;

import com.example.android.bandgeek.util.Keys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsAPI {

    NewsAPI() {}

    public String getNewsJson() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String newsJsonStr = null;

        try {
            URL url = new URL(getURL());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            newsJsonStr = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                try {
                    reader.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newsJsonStr;
    }

    // will be used later if search is implemented
    public String getURL(){
        return "https://newsapi.org/v2/everything?domains=pitchfork.com&sortBy=popularity&apiKey="
                + Keys.newsApiKey;
    }
}
