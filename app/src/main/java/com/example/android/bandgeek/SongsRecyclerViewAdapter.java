package com.example.android.bandgeek;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SongsRecyclerViewAdapter extends RecyclerView.Adapter<SongsRecyclerViewAdapter.SongViewHolder>{

    private ArrayList<Song> mSongs;
    private Context mContext;

    public SongsRecyclerViewAdapter(Context mContext, ArrayList<Song> mSongs) {
        this.mSongs = mSongs;
        this.mContext = mContext;
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        CardView mSongCardView;
        ImageView mSongImageView;
        TextView mRankTextView;
        TextView mTitleTextView;
        TextView mArtistTextView;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mSongCardView = itemView.findViewById(R.id.song_card);
            this.mSongImageView = itemView.findViewById(R.id.song_image);
            this.mTitleTextView = itemView.findViewById(R.id.song_title);
            this.mRankTextView = itemView.findViewById(R.id.song_rank);
            this.mArtistTextView = itemView.findViewById(R.id.song_artist);
        }
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent,false);
        return new SongsRecyclerViewAdapter.SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mSongs.get(position).getImageUrl())
                .apply(requestOptions)
                .into(songViewHolder.mSongImageView);

        songViewHolder.mTitleTextView.setText(mSongs.get(position).getTitle());
        songViewHolder.mArtistTextView.setText(mSongs.get(position).getArtist());
        songViewHolder.mRankTextView.setText(mSongs.get(position).getRank());

        final int pos = position;
        songViewHolder.mSongCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(mSongs.get(pos).getSongUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }
}
