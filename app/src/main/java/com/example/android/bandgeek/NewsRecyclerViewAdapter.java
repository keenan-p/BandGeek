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


public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Article> mArticles;
    private Context mContext;

    public NewsRecyclerViewAdapter(Context mContext, ArrayList<Article> mArticles) {
        this.mContext = mContext;
        this.mArticles = mArticles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mArticles.get(position).getThumbnailUrl())
                .apply(requestOptions)
                .into(myViewHolder.mThumbnailImageView);

        myViewHolder.mHeadlineTextView.setText(mArticles.get(position).getHeadline());
        myViewHolder.mDescriptionTextView.setText(mArticles.get(position).getDescription());

        final int pos = position;
        myViewHolder.mThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(mArticles.get(pos).getArticleUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mThumbnailImageView;
        public TextView mHeadlineTextView;
        public TextView mDescriptionTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mThumbnailImageView = itemView.findViewById(R.id.thumbnail);
            this.mHeadlineTextView = itemView.findViewById(R.id.headline);
            this.mDescriptionTextView = itemView.findViewById(R.id.description);
        }
    }
}
