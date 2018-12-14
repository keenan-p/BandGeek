package com.example.android.bandgeek;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mPermissionGranted;
    private Location mLocation = null;
    private String mCityName = "";

    private ArrayList<Event> mEvents;
    private View view;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_events, container, false);

        mEvents = new ArrayList<>();


        setupRecyclerView();
        getLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        getLocation();
        getLastDeviceLocation();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FetchEventsTask fetchEventsTask = new FetchEventsTask();
        fetchEventsTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(120);
        locationRequest.setFastestInterval(120);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void getLastDeviceLocation() {
        try {
            if (mPermissionGranted) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Toast.makeText(getContext(),
                                            "Successfully found current location.", Toast.LENGTH_SHORT).show();
                                    setCity(location);
                                } else {
                                    Toast.makeText(getContext(),
                                            "Location is null", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),
                                "Unable to find current location.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLocationPermission() {
        String permissions[] = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionGranted = false;

        switch(requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionGranted = true;
                }
            }
        }
    }

    private void setCity(Location location) {
        mLocation = location;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            if (addresses.size() > 0) {
                mCityName = addresses.get(0).getLocality();
                System.out.println(mCityName);
                TextView eventsHeader = Objects.requireNonNull(getActivity()).findViewById(R.id.events_header);
                eventsHeader.setText(mCityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class FetchEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        @Override
        protected ArrayList<Event> doInBackground(Void... voids) {
            TicketMasterApi tm = new TicketMasterApi(mCityName);
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
