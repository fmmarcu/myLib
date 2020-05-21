package com.example.mylib.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class LoanResponse implements Serializable {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("returnDate")
    private Date mReturnDate;

    @SerializedName("idAppointment")
    private int mIdAppointment;

    public boolean isSusccessful() {
        return mSuccess;
    }

    public String getSuccess() {
        if(mSuccess) {
            return "Successful loan";
        } else {
            return "Failed loan";
        }
    }

}
