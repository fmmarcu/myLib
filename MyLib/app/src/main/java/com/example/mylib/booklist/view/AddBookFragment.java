package com.example.mylib.booklist.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mylib.R;
import com.example.mylib.api.Constants;
import com.example.mylib.api.model.BookCall;
import com.example.mylib.api.response.LoanResponse;
import com.example.mylib.booklist.adapter.LoanedBooksAdapter;
import com.example.mylib.booklist.viewmodel.BooksViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.mylib.api.Constants.CLIENT;
import static com.example.mylib.api.Constants.PREFERENCE_FILE_KEY_USER_ROLE;

public class AddBookFragment extends Fragment {

    @BindView(R.id.issueBook_button)
    protected Button mIssueBookButton;

    @BindView(R.id.issue_book_author)
    protected EditText mIssueBookAuthor;

    @BindView(R.id.issue_book_title)
    protected EditText mIssueBookTitle;

    @BindView(R.id.addBook_container)
    protected RelativeLayout mAddBookContainer;

    @BindView(R.id.issueBook_fields_container)
    protected RelativeLayout mIssueBookContainer;

    @BindView(R.id.book_author)
    protected EditText mAddBookAuthor;

    @BindView(R.id.book_title)
    protected EditText mAddBookTitle;

    @BindView(R.id.bookEdition)
    protected EditText mBookEdition;

    @BindView(R.id.book_publishing_house)
    protected EditText mBookPublishingHouse;

    @BindView(R.id.bookPricePerDay)
    protected EditText mBookPrice;

    @BindView(R.id.bookDaysForLoaning)
    protected EditText mBookDays;

    @BindView(R.id.addBook_button)
    protected Button mAddBookButton;

    @BindView(R.id.page_title)
    protected TextView mPageTitle;

    private View mRootView;

    private BooksViewModel mViewModel;

    private final static String SHARED_PREFERENCE_NAME = "USER";
    private static final String PREFERENCE_FILE_KEY_ID_USER = "user_id";
    private Disposable mDisposable;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.add_book_layout, parent, false);

            ButterKnife.bind(this, mRootView);

            mViewModel = new ViewModelProvider(this).get(BooksViewModel.class);


            //handle user role
            if(getSharedPreferenceUserRole().equals(CLIENT)) {
                String title="Issue a book";
                mPageTitle.setText(title);
                mAddBookContainer.setVisibility(View.GONE);
                mIssueBookContainer.setVisibility(View.VISIBLE);
                mIssueBookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (!mIssueBookAuthor.getText().toString().isEmpty() && !mIssueBookTitle.getText().toString().isEmpty()) {
                            mViewModel.issueBook(mIssueBookTitle.getText().toString(), mIssueBookAuthor.getText().toString(), getSharedPreferenceUserId())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new SingleObserver<LoanResponse>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            mDisposable = d;
                                        }

                                        @Override
                                        public void onSuccess(LoanResponse loanResponse) {
                                            if (loanResponse.isSusccessful()) {
                                                Log.d("IssueBook:", "IssueBook successful");
                                                Snackbar.make(v, R.string.book_issued_successfully, BaseTransientBottomBar.LENGTH_LONG)
                                                        .show();
                                            } else {
                                                Log.d("IssueBook:", "IssueBook failed");
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.d("IssueBook:", "IssueBook error");
                                        }
                                    });
                        }
                    }
                });
            } else {
                String title="Create a book";
                mPageTitle.setText(title);
                mIssueBookContainer.setVisibility(View.GONE);
                mAddBookContainer.setVisibility(View.VISIBLE);
                mAddBookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkFieldsAdd()) {
                            mViewModel.addBook(new BookCall(mAddBookTitle.getText().toString(), mAddBookAuthor.getText().toString(), mBookPublishingHouse.getText().toString(),
                                    Integer.parseInt(mBookEdition.getText().toString()), Integer.parseInt(mBookDays.getText().toString()), Float.parseFloat(mBookPrice.getText().toString())))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new SingleObserver<LoanResponse>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            mDisposable = d;
                                        }

                                        @Override
                                        public void onSuccess(LoanResponse loanResponse) {
                                            if(loanResponse.isSusccessful()) {
                                                String message = "Book created successfully!";
                                                Snackbar.make(mRootView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                                .show();
                                            } else {
                                                String message = "Cannot create the book!";
                                                Snackbar.make(mRootView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                                .show();
                                            }

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("Add book", "Server error! "+e.getMessage());
                                        }
                                    });
                        }

                    }
                });

            }

        }

        return mRootView;
    }

    private boolean checkFieldsIssue() {
        return !mIssueBookAuthor.getText().toString().isEmpty() && !mIssueBookTitle.getText().toString().isEmpty();
    }

    private boolean checkFieldsAdd() {
        return !mAddBookAuthor.getText().toString().isEmpty() && !mAddBookTitle.getText().toString().isEmpty() && !mBookPublishingHouse.getText().toString().isEmpty()
                && !mBookEdition.getText().toString().isEmpty() && !mBookPrice.getText().toString().isEmpty() && !mBookDays.getText().toString().isEmpty();
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
        if(mDisposable != null ) {
            mDisposable.dispose();
        }
    }
}
