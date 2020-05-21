package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookCall implements Serializable {

    @SerializedName("name")
    private String mName;

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("publishingHouse")
    private String mPublishingHouse;

    @SerializedName("edition")
    private int mEdition;

    @SerializedName("daysForLoaning")
    private int mDaysForLoaning;


    @SerializedName("pricePerDay")
    private float mPricePerDay;

    @SerializedName("available")
    private boolean mAvailable;

    public BookCall( String name, String author, String publishingHouse, int edition, int daysForLoaning, float pricePerDay) {
        mName = name;
        mAuthor = author;
        mPublishingHouse = publishingHouse;
        mEdition = edition;
        mDaysForLoaning = daysForLoaning;
        mPricePerDay = pricePerDay;
        mAvailable = true;
    }
}
