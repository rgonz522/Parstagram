package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.models.Comment;

import com.example.parstagram.models.Like;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.util.ArrayList;
import java.util.List;


public class PostDetailActivity extends AppCompatActivity {

    private TextView tvCreatedAt;
    private TextView tvUserAuthor;
    private TextView tvDescription;
    private TextView tvAmtLikes;
    private EditText etNewComment;
    private ImageView ivPostPic;
    private ImageView ivProfilePic;
    private ImageView ivLike;
    private Button btnSendCmt;


    private RecyclerView rvComments;
    private CommentsAdapter adapter;
    private List<Comment> allComments;
    private ParseObject parse_post;
    private ProgressBar pbLoading;

    public static final String USER_PROFILE_IMG = "profile_pic";

    private boolean liked_by_current_user;
    private int amt_of_likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Log.i("PostDe", "onCreate: ");

        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUserAuthor = findViewById(R.id.tvUserAuthor);
        tvDescription = findViewById(R.id.tvPostDescription);

        ivPostPic = findViewById(R.id.ivPostPicture);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        etNewComment = findViewById(R.id.etNewComment);
        btnSendCmt = findViewById(R.id.btnSbmtCmt);

        pbLoading = findViewById(R.id.pbLoadingCmt);
        pbLoading.setVisibility(View.INVISIBLE);


        ivLike = findViewById(R.id.ivLike);
        tvAmtLikes = findViewById(R.id.tvAmtLikes);


        final Post post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());

        ParseUser user_of_post = post.getUser();
        parse_post = post;

        queryLikes();
        queryComments();

        tvAmtLikes.setText(amt_of_likes + "  likes");
        tvCreatedAt.setText(post.getRelativeTimeAgo());
        tvDescription.setText(post.getDescription());
        tvUserAuthor.setText(post.getUser().getUsername());

        if (liked_by_current_user) {

            ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else {
            ivLike.setImageResource(R.drawable.ufi_heart);
        }

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbLoading.setVisibility(View.VISIBLE);

                if (updateLiked_by_current_user()) {

                    unlikePost();
                } else {
                    likePost();

                }
                queryLikes();
                tvAmtLikes.setText((++amt_of_likes) + "  likes");


            }
        });

        ParseFile image = post.getImage();

        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostPic);
        }

        ParseFile profile_pic = post.getUser().getParseFile(USER_PROFILE_IMG);

        if (profile_pic != null) {
            Glide.with(this).load(profile_pic.getUrl()).circleCrop().into(ivProfilePic);
        }


        btnSendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newcomment = etNewComment.getText().toString();
                if (!newcomment.isEmpty()) {
                    pbLoading.setVisibility(ProgressBar.VISIBLE);
                    saveComment(newcomment, parse_post, ParseUser.getCurrentUser());
                }

            }
        });


        allComments = new ArrayList<>();

        rvComments = findViewById(R.id.rvcomments);
        adapter = new CommentsAdapter(this, allComments);

        rvComments.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvComments.setLayoutManager(layoutManager);


    }

    private void queryComments() {


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

                    allComments.clear();
                    allComments.addAll(comments);

                    adapter.notifyDataSetChanged();
                }

            }
        });


    }

    private void saveComment(String newcomment, ParseObject currentpost, ParseUser currentUser) {
        Comment comment = new Comment();

        comment.setMessage(newcomment);
        comment.setUser(currentUser);
        comment.setPost(currentpost);

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("PostDetails", "Issues  commenting ", e);
                    return;
                }
                etNewComment.setText("");

                pbLoading.setVisibility(ProgressBar.INVISIBLE);

                queryComments();
            }
        });

    }


    private void queryLikes() {

        amt_of_likes = 0;

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Like.KEY_POST, parse_post);


        query.findInBackground(new FindCallback<Like>() {


            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e("PostDetails", "Issues getting likes", e);
                    return;
                } else if (likes != null) {
                    amt_of_likes = likes.size();


                    for (Like like : likes) {

                        if (like.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {

                            liked_by_current_user = true;
                            ivLike.setImageResource(R.drawable.ufi_heart_active);
                        }
                        tvAmtLikes.setText(amt_of_likes + "  likes");
                    }
                } else {
                    return;
                }

            }
        });

    }

    private boolean updateLiked_by_current_user() {

        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);

        query.whereEqualTo(Like.KEY_POST, parse_post);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());


        query.findInBackground(new FindCallback<Like>() {


            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e("PostDetails", "Issues getting likes", e);
                    return;
                } else if (likes != null) {

                    liked_by_current_user = true;
                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                } else {
                    ivLike.setImageResource(R.drawable.ufi_heart);
                }

            }
        });
        return liked_by_current_user;
    }

    private void likePost() {


        Like newLike = new Like();
        newLike.setUser(ParseUser.getCurrentUser());
        newLike.setPost(parse_post);

        newLike.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                liked_by_current_user = true;
                ivLike.setImageResource(R.drawable.ufi_heart_active);

                pbLoading.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void unlikePost() {


        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);

        query.whereEqualTo(Like.KEY_POST, parse_post);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());


        query.findInBackground(new FindCallback<Like>() {

            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e("PostDetails", "Issues getting likes", e);
                    return;
                } else if (likes != null) {
                    likes.get(0).deleteInBackground();
                    liked_by_current_user = false;
                    ivLike.setImageResource(R.drawable.ufi_heart);


                }
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });

    }
}