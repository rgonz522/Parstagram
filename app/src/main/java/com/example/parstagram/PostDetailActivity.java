package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.models.Comment;

import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvCreatedAt;
    private TextView tvUserAuthor;
    private TextView tvDescription;
    private ImageView ivPostPic;
    private ImageView ivProfilePic;



    protected RecyclerView rvComments;
    protected CommentsAdapter adapter;
    protected List<Comment> allComments;
    protected ParseObject parse_post;


    public static final String USER_PROFILE_IMG = "profile_pic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUserAuthor = findViewById(R.id.tvUserAuthor);
        tvDescription = findViewById(R.id.tvPostDescription);
        ivPostPic = findViewById(R.id.ivPostPicture);
        ivProfilePic = findViewById(R.id.ivProfilePic);


        Post post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());
        Log.i("PostDetails", "onCreate: " + post.getDescription());
        ParseUser user_of_post = post.getUser();

        tvCreatedAt.setText(post.getRelativeTimeAgo());
        tvDescription.setText(post.getDescription());
        tvUserAuthor.setText(post.getUser().getUsername());

        parse_post = post;

        ParseFile image = post.getImage();

        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostPic);
        }

        ParseFile profile_pic = post.getUser().getParseFile(USER_PROFILE_IMG);

        if (profile_pic != null) {
            Glide.with(this).load(profile_pic.getUrl()).circleCrop().into(ivProfilePic);
        }



        allComments = new ArrayList<>();

        rvComments = findViewById(R.id.rvcomments);
        adapter = new CommentsAdapter(this, allComments);

        rvComments.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvComments.setLayoutManager(layoutManager);

        queryPosts();

    }

    protected void queryPosts() {


        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_POST, parse_post);
        query.addDescendingOrder(Post.KEY_CREATEDAT);

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e("PostDetails", "Issues getting comments", e);
                } else if (comments != null) {

                    allComments.addAll(comments);


                    adapter.notifyDataSetChanged();
                }

            }
        });


    }
}