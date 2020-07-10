package com.example.parstagram.models;

import com.parse.ParseClassName;

import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {


    public static final String KEY_MESSAGE = "Message";
    public static final String KEY_USER = "User";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_POST = "Post";

    public String getMessage() { return getString(KEY_MESSAGE); }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

    public ParseUser getUser() { return getParseUser(KEY_USER); }

    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }


    public ParseObject getPost() { return getParseObject(KEY_POST); }

    public void setPost(ParseObject parseObject) { put(KEY_POST, parseObject); }
}
