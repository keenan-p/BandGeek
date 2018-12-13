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

import org.json.JSONException;

import java.util.ArrayList;


public class NewsFragment extends Fragment {

    private ArrayList<Article> mArticles;
    private NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);

        mArticles = new ArrayList<>();

        setupRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FetchArticlesTask fetchArticlesTask = new FetchArticlesTask();
        fetchArticlesTask.execute();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.news_recycler_view);
        mNewsRecyclerViewAdapter =
                new NewsRecyclerViewAdapter(getActivity(), mArticles);

        final int NUM_COLUMNS = 2;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mNewsRecyclerViewAdapter);
    }

    private class FetchArticlesTask extends AsyncTask<Void, Void, ArrayList<Article>> {

        @Override
        protected ArrayList<Article> doInBackground(Void... voids) {
            NewsAPI api = new NewsAPI();
            String articleJsonStr = api.getNewsJson();

            try {
                NewsJsonParser newsJsonParser = new NewsJsonParser(articleJsonStr);
                return newsJsonParser.parse();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> apiResults) {
            if (apiResults != null) {
                mArticles.clear();

                mArticles.addAll(apiResults);

                System.out.println("number of articles: " + apiResults.size());

                mNewsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                mArticles.clear();
                mArticles.add(new Article("https://upload.wikimedia.org/" +
                        "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                        "Font_Awesome_5_regular_frown.svg.png", "No results", "", ""));
                mNewsRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
