package com.example.android.bandgeek;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ChartsFragment extends Fragment {

    private ArrayList<Song> mSongs;
    private SongsRecyclerViewAdapter mSongsRecyclerViewAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_charts, container, false);

        mSongs = new ArrayList<>();

        setupRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ConnectivityChecker.isConnectedToInternet(Objects.requireNonNull(getContext()))) {
            FetchSongsTask fetchSongsTask = new FetchSongsTask();
            fetchSongsTask.execute();
        }
        else {
            mSongs.clear();
            mSongs.add(new Song("https://upload.wikimedia.org/" +
                    "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                    "Font_Awesome_5_regular_frown.svg.png", "No internet connection.", "", "", ""));
            mSongsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.songs_recycler_view);
        mSongsRecyclerViewAdapter =
                new SongsRecyclerViewAdapter(getActivity(), mSongs);

        final int NUM_COLUMNS = 1;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mSongsRecyclerViewAdapter);
    }


    private class FetchSongsTask extends AsyncTask<Void, Void, ArrayList<Song>> {

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            ItunesApi it = new ItunesApi();
            String songsJsonStr = it.getSongsJson();

            try {
                JsonParser jsonParser = new JsonParser(songsJsonStr);
                return jsonParser.parseSongs();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Song> apiResults) {
            if (apiResults != null) {
                mSongs.clear();

                mSongs.addAll(apiResults);

                mSongsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                mSongs.clear();
                mSongs.add(new Song("https://upload.wikimedia.org/" +
                        "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                        "Font_Awesome_5_regular_frown.svg.png", "No results", "", "", ""));
                mSongsRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
