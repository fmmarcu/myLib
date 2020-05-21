package com.example.mylib.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IssueResponse implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("client")
    private String mClient;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAuthor() {
        return mAuthor;
    }

}
