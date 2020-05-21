package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Client implements Serializable {

    private static final String CLIENT = "CLIENT";

    @SerializedName("keyword")
    private String mKeyword;

    public Client() {
        mKeyword = CLIENT;
    }

    public String getKeyword() {
        return mKeyword;
    }
}
