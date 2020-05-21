package com.example.mylib.booklist.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylib.R;
import com.example.mylib.api.Constants;
import com.example.mylib.api.model.Book;
import com.example.mylib.api.model.Keyword;
import com.example.mylib.booklist.adapter.BooksAdapter;
import com.example.mylib.booklist.viewmodel.BooksViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookListFragment extends Fragment {

    private View mRootView;

    @BindView(R.id.book_list)
    protected RecyclerView mBooksList;

    @BindView(R.id.books_search_bar)
    protected SearchView mSearchView;

    private BooksAdapter mBooksAdapter;

    private BooksViewModel mViewModel;

    private Disposable mDisposable;
    private Disposable mQuerryDisposable;

    private final static String SHARED_PREFERENCE_NAME= "USER";
    private static final String PREFERENCE_FILE_KEY_USER_ROLE="user_role";


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.booklist_layout, parent, false);

            ButterKnife.bind(this, mRootView);

            mViewModel = new ViewModelProvider(this).get(BooksViewModel.class);

            mBooksAdapter = new BooksAdapter(this.getContext(), mViewModel);

            mBooksList.setAdapter(mBooksAdapter);
            mBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));

            mViewModel.mGetAllBooks()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<ArrayList<Book>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable = d;
                        }

                        @Override
                        public void onSuccess(ArrayList<Book> books) {
                            mBooksAdapter.setBooksList(books);
                            mBooksAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    });

            if(getSharedPreferenceUserRole().equals(Constants.CLIENT)) {

                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        mViewModel.getBookSearch(new Keyword(query))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new SingleObserver<ArrayList<Book>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mQuerryDisposable = d;
                                    }

                                    @Override
                                    public void onSuccess(ArrayList<Book> books) {
                                        mBooksAdapter.setBooksList(books);
                                        mBooksAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                        closeKeyboard();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            } else {
                mSearchView.setVisibility(View.GONE);
            }

        }
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        if(mQuerryDisposable != null) {
            mQuerryDisposable.dispose();
        }
    }

    private String getSharedPreferenceUserRole() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT ).isEmpty()) {
            return sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT);
        }
        return Constants.CLIENT;
    }

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

}
