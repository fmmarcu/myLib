package com.example.mylib.register.view;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mylib.R;
import com.example.mylib.api.model.User;
import com.example.mylib.api.response.RegisterResponse;
import com.example.mylib.login.viewmodel.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterFragment extends Fragment {

    private View mRootView;
    private UserViewModel mUserViewModel;

    @BindView(R.id.error_message)
    protected TextView mErrorMessage;

    @BindView(R.id.first_name)
    protected EditText mFirstName;

    @BindView(R.id.last_name)
    protected EditText mLastName;

    @BindView(R.id.register_button)
    protected Button mRegisterButton;

    @BindView(R.id.address)
    protected EditText mAddress;

    @BindView(R.id.phone_number)
    protected EditText mPhoneNumber;

    @BindView(R.id.password)
    protected EditText mPassword;

    @BindView(R.id.confirm_password)
    protected EditText mConfirmPassword;

    @BindView(R.id.user_name)
    protected EditText mEmail;

    private static final String ERROR_EMAIL_TAKEN="This e-mail is taken.";
    private static final String ERROR_EMPTY_FIELDS="Please fill all the fields";
    private static final String ERROR_PASSWORDS_MISMATCH="The passwords introduced don't match";
    private static final String REGISTER_SUCCESSFUL = "Your registration was successful!";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.register_layout, parent, false);
            ButterKnife.bind(this, mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.init();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private boolean checkPasswords() {
        return !mPassword.getText().toString().isEmpty() && !mConfirmPassword.getText().toString().isEmpty() &&
                mPassword.getText().toString().equals(mConfirmPassword.getText().toString());
    }

    private boolean checkEmptyFields() {
        return !mFirstName.getText().toString().isEmpty() && !mLastName.getText().toString().isEmpty() && !mPhoneNumber.getText().toString().isEmpty() &&
                !mAddress.getText().toString().isEmpty() && !mEmail.getText().toString().isEmpty();
    }

    private void register() {
        if(!checkEmptyFields()) {
            mErrorMessage.setText(ERROR_EMPTY_FIELDS);
            mErrorMessage.setVisibility(View.VISIBLE);
        } else if(!checkPasswords()) {
            mErrorMessage.setText(ERROR_PASSWORDS_MISMATCH);
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            User user = new User(mEmail.getText().toString(), mPassword.getText().toString(), mFirstName.getText().toString(),
                    mLastName.getText().toString(), mPhoneNumber.getText().toString(), mAddress.getText().toString());
            mUserViewModel.register(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new SingleObserver<RegisterResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(RegisterResponse registerResponse) {
                    if(registerResponse.isSuccessful()) {
                        Log.d("marcu", "mam registrat");
                        mErrorMessage.setText(REGISTER_SUCCESSFUL);
                        mErrorMessage.setTextColor(getResources().getColor(R.color.green));
                        mErrorMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mErrorMessage.setText(ERROR_EMAIL_TAKEN);
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
