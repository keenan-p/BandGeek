package com.example.android.bandgeek;

import android.location.Location;

import com.example.android.bandgeek.util.Keys;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TicketMasterApi {

    private String mCityName;

    public TicketMasterApi(String mCityName) {
        this.mCityName = mCityName.trim();
    }

    public String getEventsJson() {

        String eventsJsonStr = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://app.ticketmaster.com/discovery/v2/events.json?classificationName=music&city=" +
                        mCityName + "&apikey=" + Keys.ticketmasterApiKey)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            eventsJsonStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventsJsonStr;
    }

}
