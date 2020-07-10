package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rebeca-parstagram") // should correspond to APP_ID env variable
                .clientKey("CodePathMoveFastParsestagram")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://rebeca-parstagram.herokuapp.com/parse/").build());

    }
}
