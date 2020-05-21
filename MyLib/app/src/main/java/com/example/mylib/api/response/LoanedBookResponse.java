package com.example.mylib.api.response;

import com.example.mylib.api.model.Book;
import com.example.mylib.api.model.Client;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class LoanedBookResponse implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("startDate")
    private Date mStartDate;

    @SerializedName("endDate")
    private Date mEndDate;

    @SerializedName("client")
    private String mClient;

    @SerializedName("book")
    private Book mBook;

    @SerializedName("returned")
    private boolean mReturned;

    @SerializedName("amount")
    private float mAmount;

    public int getId() {
        return mId;
    }

    public Book getBook() {
        return mBook;
    }

    public boolean isOverdue() {
        return mAmount > 0;
    }

    public String getAmount() {
        return String.valueOf(mAmount).substring(0, 3);
    }
}
