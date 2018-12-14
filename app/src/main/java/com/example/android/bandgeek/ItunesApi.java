package com.example.android.bandgeek;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ItunesApi {

    public ItunesApi() {

    }

    public String getSongsJson() {

        String spotifyJsonStr = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://rss.itunes.apple.com/api/v1/us/apple-music/top-songs/all/100/explicit.json")
                .get()
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                spotifyJsonStr = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return spotifyJsonStr;
    }
}
