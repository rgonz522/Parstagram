package com.example.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.PostDetailActivity;
import com.example.parstagram.R;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class GridPostsAdapter extends RecyclerView.Adapter<GridPostsAdapter.ViewHolder>{

    public static final int ROUNDED_RADIUS = 100;
    private Context context;
    private List<Post> posts;


    public GridPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public GridPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_picture, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = posts.get(position);

        holder.bind(post);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivpostPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivpostPic = itemView.findViewById(R.id.ivPostPic);

        }

        public void bind(final Post post) {

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new RoundedCorners(ROUNDED_RADIUS)).into(ivpostPic);
            } else {
                ivpostPic.setVisibility(View.GONE);
            }

            ivpostPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the post at the position
                        Post post = posts.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        // pass the post[already parceable] , use its short name as a key
                        intent.putExtra(Post.class.getSimpleName(), post);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
