package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

import fragments.ProfileFragment;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final int ROUNDED_RADIUS = 80;
    private Context context;
    private List<Post> posts;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);


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
        private TextView tvUsername;
        private TextView tvDescription;
        private TextView tvCreated;
        private ImageView ivPostPic;
        private ImageView ivProfilePostPic;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUser);
            tvDescription = itemView.findViewById(R.id.tvPostDescription);
            ivPostPic = itemView.findViewById(R.id.ivPostPic);
            ivProfilePostPic = itemView.findViewById(R.id.ivProfilePostPic);
            tvCreated = itemView.findViewById(R.id.tvCreatedAt);

        }

        public void bind(final Post post) {

            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvCreated.setText(post.getRelativeTimeAgo());
            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context).load(image.getUrl()).transform(new RoundedCorners(ROUNDED_RADIUS)).into(ivPostPic);
            } else {
                ivPostPic.setVisibility(View.GONE);
            }

            image = post.getUser().getParseFile(ProfileFragment.KEY_PROFILE_IMG);

            if (image != null) {
                Glide.with(context).load(image.getUrl()).circleCrop().into(ivProfilePostPic);
            }
            ivProfilePostPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startProfileFragment(post.getUser());

                }
            });



            ivPostPic.setOnClickListener(new View.OnClickListener() {
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

    public void startProfileFragment(ParseUser user){

        ProfileFragment profilefragment = new ProfileFragment(user, false);
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer,profilefragment);
        fragmentTransaction.commit();

    }
}
