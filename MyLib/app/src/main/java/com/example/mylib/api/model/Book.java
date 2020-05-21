package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    @SerializedName("id")
    private int mId;

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

    @SerializedName("clientList")
    private List<User> mClientList;

    @SerializedName("pricePerDay")
    private float mPricePerDay;

    @SerializedName("available")
    private boolean mAvailable;

    public Book(int id, String name, String author, String publishingHouse, int edition, int daysForLoaning, List<User> clientList, float pricePerDay, boolean available) {
        mId = id;
        mName = name;
        mAuthor = author;
        mPublishingHouse = publishingHouse;
        mEdition = edition;
        mDaysForLoaning = daysForLoaning;
        mClientList = clientList;
        mPricePerDay = pricePerDay;
        mAvailable = available;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishingHouse() {
        return mPublishingHouse;
    }

    public int getEdition() {
        return mEdition;
    }

    public int getDaysForLoaning() {
        return mDaysForLoaning;
    }

    public List<User> getClientList() {
        return mClientList;
    }

    public float getPricePerDay() {
        return mPricePerDay;
    }

    public boolean getAvailability() {
        return mAvailable;
    }
}
