package com.example.mylib.api.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("returnDate")
    private String mDate;

    @SerializedName("idAppointment")
    private String mId;

    @SerializedName("timestamp")
    private String mTimestamp;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("error")
    private String mError;

    public boolean getSuccess() {
        return mSuccess;
    }

    public boolean isSuccessful() {
        if(mSuccess) {
            return true;
        } else if(!mError.isEmpty()) {
            return false;
        }
        return false;
    }
}
