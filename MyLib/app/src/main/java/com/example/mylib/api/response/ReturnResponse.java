package com.example.mylib.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReturnResponse implements Serializable {

    @SerializedName("amount")
    private float mAmount;

    @SerializedName("success")
    private boolean mSuccess;

    public boolean isSuccessful() {
        return mSuccess;
    }

    public String getAmount() {
        return Float.toString(mAmount).substring(0, 3);
    }

    public boolean isPenalty() {
        return mAmount > 0;
    }
}
