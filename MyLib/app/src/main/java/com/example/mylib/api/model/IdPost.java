package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IdPost implements Serializable {

    @SerializedName("id")
    private int mId;

    public IdPost(int id) {
        mId = id;
    }
}
