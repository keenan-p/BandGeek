package com.example.android.bandgeek;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventsFragment extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean permissionGranted;

    private ArrayList<Event> mEvents;
    private View view;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_events, container, false);


        mEvents = new ArrayList<>();
        mEvents.add(new Event("https://images.sk-static.com/images/media/profile_images/artists/7286084/huge_avatar",
                "Anderson .Paak", "February 24, 2019", "The Fillmore", "https://www.songkick." +
                "com/concerts/36224879-anderson-paak-at-fillmore-philadelphia-presented-by-cricket-wireless"));

        setupRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            permissionGranted = true;
        }

        if (permissionGranted) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    System.out.println("success location");
                    setCity(location);
                }
            });
        }
    }

    private void setCity(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                System.out.println(city);
                TextView eventsHeader = getActivity().findViewById(R.id.events_header);
                eventsHeader.setText(city);
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
}
