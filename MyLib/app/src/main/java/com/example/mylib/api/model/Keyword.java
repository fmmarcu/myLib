package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Keyword implements Serializable {

    @SerializedName("keyword")
    private String mKeyword;

    public Keyword(String keyword) {
        mKeyword = keyword;
    }
    public String getKeyword() {
        return mKeyword;
    }

}
