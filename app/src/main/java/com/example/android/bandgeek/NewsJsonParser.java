package com.example.android.bandgeek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsJsonParser {

    private String mNewsJsonStr;

    public NewsJsonParser(String newsJsonStr) {
        this.mNewsJsonStr = newsJsonStr;
    }

    public ArrayList<Article> parse() throws JSONException {
        JSONObject jsonObject = new JSONObject(mNewsJsonStr);
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
}
