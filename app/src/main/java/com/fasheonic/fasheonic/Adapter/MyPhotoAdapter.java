package com.fasheonic.fasheonic.Adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fasheonic.fasheonic.Model.Post;
import com.fasheonic.fasheonic.R;

import java.util.List;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder>{
    private Context context;
    private List<Post> nPosts;

    public MyPhotoAdapter(Context context, List<Post> nPosts) {
        this.context = context;
        this.nPosts = nPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.photos_item,viewGroup,false);
        return new MyPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Post post = nPosts.get(i);
        Glide.with(context).load(post.getPostimage()).into(viewHolder.post_image);
    }

    @Override
    public int getItemCount() {
        return nPosts.size();
        //return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView post_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);

        }
    }



}
