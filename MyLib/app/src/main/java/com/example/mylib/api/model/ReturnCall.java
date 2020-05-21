package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReturnCall implements Serializable {

    @SerializedName("id")
    private int mLoanId;

    @SerializedName("idUser")
    private int mUserId;

    @SerializedName("idBook")
    private int mBookId;

    public ReturnCall(int loanId, int userId, int bookId) {
        mLoanId = loanId;
        mUserId = userId;
        mBookId = bookId;
    }
}
