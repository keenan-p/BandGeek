package com.example.android.bandgeek;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventsFragment extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation = null;
    private String mPrevCityName = null;
    private String mCurrCityName = null;

    private int request_interval = 10000;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean mLocationPermissionGranted = false;

    private ArrayList<Event> mEvents;
    private View view;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_events, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            buildLocationRequest();
            buildLocationCallback();

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

        mEvents = new ArrayList<>();

        setupRecyclerView();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                Toast.makeText(getContext(), "onRequestPermissionResult", Toast.LENGTH_SHORT).show();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        buildLocationRequest();
                        buildLocationCallback();

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }
                }
            }
        }
    }

    private void buildLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations() ){
                    setCity(location);
                }
            }
        };
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(request_interval);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!ConnectivityChecker.isConnectedToInternet(Objects.requireNonNull(getContext()))) {
            mEvents.clear();
            mEvents.add(new Event("https://upload.wikimedia.org/" +
                    "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                    "Font_Awesome_5_regular_frown.svg.png", "No internet connection." +
                    "", "", "", ""));
            mEventsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void setCity(Location location) {
        mLocation = location;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            if (addresses.size() > 0) {
                mCurrCityName = addresses.get(0).getLocality();
                System.out.println(mCurrCityName);
                TextView eventsHeader = Objects.requireNonNull(getActivity()).findViewById(R.id.events_header);
                eventsHeader.setText(mCurrCityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mPrevCityName == null || !(mPrevCityName.equals(mCurrCityName))) {
            Toast.makeText(getContext(), "RESET CITY", Toast.LENGTH_SHORT).show();
            mPrevCityName = mCurrCityName;
            FetchEventsTask fetchEventsTask = new FetchEventsTask();
            fetchEventsTask.execute();
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.events_recycler_view);
        mEventsRecyclerViewAdapter =
                new EventsRecyclerViewAdapter(getActivity(), mEvents);

        final int NUM_COLUMNS = 2;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mEventsRecyclerViewAdapter);
    }

    private class FetchEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        @Override
        protected ArrayList<Event> doInBackground(Void... voids) {
            TicketMasterApi tm = new TicketMasterApi(mCurrCityName);
            String eventsJsonStr = tm.getEventsJson();

            System.out.println(eventsJsonStr);

            try {
                JsonParser jsonParser = new JsonParser(eventsJsonStr);
                return jsonParser.parseEvents();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> apiResults) {
            if (apiResults != null) {
                mEvents.clear();

                mEvents.addAll(apiResults);

                System.out.println("number of articles: " + apiResults.size());

                mEventsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                mEvents.clear();
                mEvents.add(new Event("https://upload.wikimedia.org/" +
                        "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                        "Font_Awesome_5_regular_frown.svg.png", "No results", "", "", ""));
                mEventsRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
