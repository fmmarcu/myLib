package com.example.mylib.login.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mylib.api.model.User;
import com.example.mylib.api.repository.UserRepository;
import com.example.mylib.api.response.LoginResponse;
import com.example.mylib.api.response.RegisterResponse;

import java.util.List;

import io.reactivex.Single;

public class UserViewModel extends ViewModel {

    private UserRepository mUserRepository;
    private static final String TAG = UserViewModel.class.getSimpleName();
    private MutableLiveData<List<User>> mAllUserLiveData;

    public UserViewModel() {
        super();
        mUserRepository = UserRepository.getInstance();
    }

    public void init() { mUserRepository = UserRepository.getInstance(); }

    public Single<LoginResponse> loginUser(User user) {
        return mUserRepository.loginUser(user);
    }

    public Single<RegisterResponse> register(User user) {
        return mUserRepository.registerUser(user);
    }

}
