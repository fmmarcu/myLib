package com.example.mylib.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mylib.MainActivity;
import com.example.mylib.R;
import com.example.mylib.api.model.User;
import com.example.mylib.api.response.LoginResponse;
import com.example.mylib.login.viewmodel.UserViewModel;

import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private static final String TAG = LoginFragment.class.getSimpleName();
    private EditText mUsername, mPassword;
    private TextView mLoginError;

    private static final String FILL_BLANKS = "Fill all the empty spaces";
    private final static String NONE = "None";
    private final static String SHARED_PREFERENCE_NAME= "USER";
    private static final String PREFERENCE_FILE_KEY_USERNAME= "user_username";
    private static final String PREFERENCE_FILE_KEY_ID_USER= "user_id";
    private static final String PREFERENCE_FILE_KEY_USER_ROLE="user_role";

    private View mRootView;
    private Disposable mDisposable;


    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.login_layout, container, false);
        }
        return mRootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.init();

        mLoginError = view.findViewById(R.id.loginErrorMessage);
        mUsername = view.findViewById(R.id.usernameEditText);
        mPassword = view.findViewById(R.id.passwordEditText);


        //navigation to register fragment on text click
        TextView createAccount = view.findViewById(R.id.registerAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        Button login = view.findViewById(R.id.login_button);
        //Login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                if (!mUsername.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                    User user = new User(mUsername.getText().toString().trim(), mPassword.getText().toString());
                    userLogin(user);
                    //in case username or password are empty
                } else {
                    mLoginError.setText(FILL_BLANKS);
                    mLoginError.setVisibility(View.VISIBLE);

                    Log.d(TAG, "Username and password null");
                }
            }
        });
    }


    private void userLogin(User user) {
        Log.d(TAG, "Login button clicked" + " " + user.getEmail() + " " + user.getPassword());
        mUserViewModel.loginUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        if(loginResponse.isValid()) {
                            Log.d("Login response: ", "SUCCESS!");
                            setSharedPreferencesId(loginResponse.getId());
                            setSharedPreferencesUserRole(loginResponse.getRole());
                            Navigation.findNavController(mRootView).navigate(R.id.action_loginFragment_to_bookList);
                        } else {
                            Log.d("Login response: ", "FAIL!");
                            mLoginError.setText(R.string.invalid_credentials);
                            mLoginError.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Login: ", "Server error! "+e.getMessage());
                    }
                });
    }

    //hide keyboard
    private void closeKeyboard() {
        if (getContext() != null) {
            InputMethodManager imm = (InputMethodManager) (getContext()).getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = getView();
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void setSharedPreferencesId(int id) {
        if (getActivity() != null && id != 0) {
            SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putInt(PREFERENCE_FILE_KEY_ID_USER, id);
            mEditor.apply();
        }
    }

    private void setSharedPreferencesUserRole(String role) {
        if (getActivity() != null && !role.isEmpty()) {
            SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString(PREFERENCE_FILE_KEY_USER_ROLE, role);
            mEditor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDisposable != null ) {
            mDisposable.dispose();
        }
    }


}

