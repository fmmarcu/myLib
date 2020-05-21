package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IssueBookCall implements Serializable {

    @SerializedName("name")
    private String mName;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("idUser")
    private int mUserId;

    public IssueBookCall(String name, String author, int userId) {
        mName = name;
        mAuthor = author;
        mUserId = userId;
    }
}
