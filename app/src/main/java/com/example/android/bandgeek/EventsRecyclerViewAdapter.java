package com.example.android.bandgeek;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder> {

    private ArrayList<Event> mEvents;
    private Context mContext;

    public EventsRecyclerViewAdapter(Context mContext, ArrayList<Event> mEvents) {
        this.mEvents = mEvents;
        this.mContext = mContext;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView mEventImageView;
        TextView mTitleTextView;
        TextView mDateTextView;
        TextView mVenueTextView;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mEventImageView = itemView.findViewById(R.id.event_image);
            this.mTitleTextView = itemView.findViewById(R.id.event_title);
            this.mDateTextView = itemView.findViewById(R.id.event_date);
            this.mVenueTextView = itemView.findViewById(R.id.event_venue);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mEvents.get(position).getImageUrl())
                .apply(requestOptions)
                .into(eventViewHolder.mEventImageView);

        eventViewHolder.mTitleTextView.setText(mEvents.get(position).getTitle());
        eventViewHolder.mDateTextView.setText(mEvents.get(position).getDate());
        eventViewHolder.mVenueTextView.setText(mEvents.get(position).getVenue());

        final int pos = position;
        eventViewHolder.mEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(mEvents.get(pos).getTicketsUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
