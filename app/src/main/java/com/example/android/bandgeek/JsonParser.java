package com.example.android.bandgeek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    private String mJsonStr;

    public JsonParser(String jsonStr) {
        this.mJsonStr = jsonStr;
    }

    public ArrayList<Article> parseNews() throws JSONException {
        JSONObject jsonObject = new JSONObject(mJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("articles");
        ArrayList<Article> articles = new ArrayList<>();

        String thumbnailUrl, headline, description, articleUrl;

        for (int i = 0; i < jsonArray.length(); i++) {
            headline = jsonArray.getJSONObject(i).getString("title");
            thumbnailUrl = jsonArray.getJSONObject(i).getString("urlToImage");
            description = jsonArray.getJSONObject(i).getString("description");
            articleUrl = jsonArray.getJSONObject(i).getString("url");

            articles.add(new Article(thumbnailUrl, headline, description, articleUrl));
        }

        return articles;
    }

    public ArrayList<Event> parseEvents() throws JSONException {
        JSONObject jsonObject = new JSONObject(mJsonStr);
        JSONObject embedded = jsonObject.getJSONObject("_embedded");
        JSONArray jsonArray = embedded.getJSONArray("events");

        System.out.println("jsonArray length: " + jsonArray.length());
        ArrayList<Event> events = new ArrayList<>();

        String imageUrl = "", title = "", date = "", venue = "", ticketsUrl = "";
        JSONObject event_i;

        for (int i = 0; i < jsonArray.length(); i++) {
            event_i = jsonArray.getJSONObject(i);

            title = jsonArray.getJSONObject(i).getString("name");

            imageUrl = event_i.getJSONArray("images")
                    .getJSONObject(6)
                    .getString("url");

            date = event_i.getJSONObject("dates")
                    .getJSONObject("start")
                    .getString("localDate");

            try {
                venue = event_i.getJSONObject("_embedded")
                        .getJSONArray("venues")
                        .getJSONObject(1)
                        .getString("name");
            } catch (Exception e) {
                e.printStackTrace();
            }

            ticketsUrl = event_i.getString("url");

            events.add(new Event(imageUrl, title, date, venue, ticketsUrl));
        }

        return events;
    }

    public ArrayList<Song> parseSongs() throws JSONException {
        JSONObject jsonObject = new JSONObject(mJsonStr);
        JSONObject feed = jsonObject.getJSONObject("feed");
        JSONArray results = feed.getJSONArray("results");
        JSONObject song_i;

        ArrayList<Song> songs = new ArrayList<>();

        String title, artist, imageUrl, rank, songUrl;

        for (int i = 0; i < results.length(); i++) {
            song_i = results.getJSONObject(i);

            artist = song_i.getString("artistName");
            title = song_i.getString("name");
            imageUrl = song_i.getString("artworkUrl100");
            rank = Integer.toString(i+1);
            songUrl = song_i.getString("url");

            songs.add(new Song(title, artist, imageUrl, rank, songUrl));
        }

        return songs;
    }
}
