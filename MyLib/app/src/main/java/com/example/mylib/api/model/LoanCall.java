package com.example.mylib.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoanCall implements Serializable {

    @SerializedName("idUser")
    private int mIdUser;

    @SerializedName("idBook")
    private int mIdBook;

    public LoanCall(int idUser, int idBook) {
        mIdUser = idUser;
        mIdBook = idBook;
    }

}
