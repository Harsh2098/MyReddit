package com.hmproductions.myreddit.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmproductions.myreddit.R;
import com.hmproductions.myreddit.data.RedditPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harsh Mahajan on 15/7/2017.
 */

public class RedditPostsRecyclerAdapter extends RecyclerView.Adapter<RedditPostsRecyclerAdapter.PostViewHolder> {

    private List<RedditPost> mData = new ArrayList<>();
    private Context mContext;

    public RedditPostsRecyclerAdapter(Context context, List<RedditPost> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public RedditPostsRecyclerAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.posts_list_item, parent, false);
        return new PostViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(RedditPostsRecyclerAdapter.PostViewHolder holder, int position) {

        holder.date_textView.setText(mData.get(position).getDate());
        holder.title_textView.setText(mData.get(position).getTitle());
        holder.numOfComments_textView.setText("Number of Comments : " + mData.get(position).getNumOfComments());

        if(mData.get(position).getThumbnailURL() == null) {
            holder.thumbnail_imageView.setVisibility(View.GONE);
        } else {
            holder.thumbnail_imageView.setVisibility(View.VISIBLE);
            Glide
                    .with(mContext)
                    .load(mData.get(position).getThumbnailURL())
                    .into(holder.thumbnail_imageView);
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView date_textView, title_textView, numOfComments_textView;
        ImageView thumbnail_imageView;

        PostViewHolder(View itemView) {
            super(itemView);

            title_textView = (TextView)itemView.findViewById(R.id.title_textView);
            numOfComments_textView = (TextView)itemView.findViewById(R.id.numberOfComments_textView);
            date_textView = (TextView)itemView.findViewById(R.id.date_textView);
            thumbnail_imageView = (ImageView)itemView.findViewById(R.id.thumbnail_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mData.get(getAdapterPosition()).getContentURL() == null)
                        Toast.makeText(mContext, "You are offline. Websites will not open.", Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(getAdapterPosition()).getContentURL()));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
