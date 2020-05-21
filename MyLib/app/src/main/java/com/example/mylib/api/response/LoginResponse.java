package com.example.mylib.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("role")
    private String mRole;

    @SerializedName("token")
    private String mToken;

    @SerializedName("email")
    private String mEmail;

    public int getId() {
        return mId;
    }

    public String getRole() {
        return mRole;
    }

    public String getToken() {
        return mToken;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean isValid() {
        return mRole != null && mToken != null && mEmail != null;
    }
}
