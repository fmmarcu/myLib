package com.example.mylib.booklist.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylib.R;
import com.example.mylib.api.Constants;
import com.example.mylib.api.model.Book;
import com.example.mylib.api.response.IssueResponse;
import com.example.mylib.api.response.LoanedBookResponse;
import com.example.mylib.booklist.adapter.IssuedBooksAdapter;
import com.example.mylib.booklist.adapter.LoanedBooksAdapter;
import com.example.mylib.booklist.viewmodel.BooksViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.mylib.api.Constants.CLIENT;
import static com.example.mylib.api.Constants.PREFERENCE_FILE_KEY_USER_ROLE;

public class LoanedBookListFragment extends Fragment {

    @BindView(R.id.reservations_list)
    protected RecyclerView mLoanedBooksList;

    @BindView(R.id.page_title)
    protected TextView mPageTitle;

    private View mRootView;

    private BooksViewModel mViewModel;

    private LoanedBooksAdapter mLoanedBooksAdapter;
    private final static String SHARED_PREFERENCE_NAME= "USER";
    private static final String PREFERENCE_FILE_KEY_ID_USER= "user_id";
    private Disposable mDisposable;
    private IssuedBooksAdapter mIssuedBooksAdapter;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.reservations_layout, parent, false);

            ButterKnife.bind(this, mRootView);

            mViewModel = new ViewModelProvider(this).get(BooksViewModel.class);

            //handle user role
            if(getSharedPreferenceUserRole().equals(CLIENT)) {
                String title = "Your loaned books";
                mPageTitle.setText(title);

                mLoanedBooksAdapter = new LoanedBooksAdapter(this.getContext(), mViewModel);

                mLoanedBooksList.setAdapter(mLoanedBooksAdapter);
                mLoanedBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));

                mViewModel.getUserLoanedBooks(getSharedPreferenceUserId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new SingleObserver<ArrayList<LoanedBookResponse>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onSuccess(ArrayList<LoanedBookResponse> loanedBookResponses) {
                                Log.d("Loaned books ", "Number of successful responses " + loanedBookResponses.size());

                                mLoanedBooksAdapter.setBooksList(loanedBookResponses);
                                mLoanedBooksAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Error on loaned books fetch : ", e.getMessage() != null ? e.getMessage() : "No error message");
                            }
                        });
            } else {

                String title = "Issues created by the users";
                mPageTitle.setText(title);
                mIssuedBooksAdapter = new IssuedBooksAdapter(this.getContext(), mViewModel);

                mLoanedBooksList.setAdapter(mIssuedBooksAdapter);
                mLoanedBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));

                mViewModel.getAllBookIssues()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new SingleObserver<ArrayList<IssueResponse>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onSuccess(ArrayList<IssueResponse> issueResponses) {
                                mIssuedBooksAdapter.setBooksList(issueResponses);
                                mIssuedBooksAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }

        }

        return mRootView;
    }

    private int getSharedPreferenceUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(PREFERENCE_FILE_KEY_ID_USER, 0) != 0) {
            return sharedPreferences.getInt(PREFERENCE_FILE_KEY_ID_USER, 0);
        }
        return 0;
    }

    private String getSharedPreferenceUserRole() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT ).isEmpty()) {
            return sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT);
        }
        return Constants.CLIENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDisposable != null) {
            mDisposable.dispose();
        }
    }

}