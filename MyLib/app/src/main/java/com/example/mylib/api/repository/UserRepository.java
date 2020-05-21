package com.example.mylib.api.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.mylib.api.DataService;
import com.example.mylib.api.RetrofitClient;
import com.example.mylib.api.model.Client;
import com.example.mylib.api.model.User;
import com.example.mylib.api.response.LoginResponse;
import com.example.mylib.api.response.RegisterPost;
import com.example.mylib.api.response.RegisterResponse;

import io.reactivex.Single;

public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    private DataService mDataService;

    private final static String LOGIN_SUCCESS = "Login successful!";
    private final static String WRONG_USER = "Username or password are incorrect.";

    private UserRepository(){
        mDataService = RetrofitClient.createService(DataService.class);
    }

    public static UserRepository getInstance() {
        return new UserRepository();
    }

    public Single<LoginResponse> loginUser(User user) {
        return mDataService.loginUser(user);
    }

    public Single<RegisterResponse> registerUser(User user) {
        return mDataService.registerUser(new RegisterPost(user, new Client()));
    }
}
