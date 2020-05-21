package com.example.mylib.api.response;

import com.example.mylib.api.model.Client;
import com.example.mylib.api.model.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterPost implements Serializable {

    @SerializedName("user")
    private User mUser;

    @SerializedName("string")
    private Client mClient;

    public RegisterPost(User user, Client client) {
        mUser = user;
        mClient = client;
    }

}
