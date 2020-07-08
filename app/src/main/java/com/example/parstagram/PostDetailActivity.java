package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvCreatedAt;
    private TextView tvUserAuthor;
    private TextView tvDescription;
    private ImageView ivPostPic;
    private ImageView ivProfilePic;


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

        tvCreatedAt.setText(post.getCreatedAt().toString());
        tvDescription.setText(post.getDescription());
        tvUserAuthor.setText(post.getUser().getUsername());

        ParseFile image = post.getImage();

        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPostPic);
        }

        ParseFile profile_pic = post.getUser().getParseFile(USER_PROFILE_IMG);

        if (profile_pic != null) {
            Glide.with(this).load(profile_pic.getUrl()).into(ivProfilePic);
        }

    }
}