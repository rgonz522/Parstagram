package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Likes")
public class Like extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }


    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }

    public void setPost(ParseObject parseObject) {
        put(KEY_POST, parseObject);
    }
}
