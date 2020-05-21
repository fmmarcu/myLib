package com.example.mylib.booklist.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylib.R;
import com.example.mylib.api.Constants;
import com.example.mylib.api.model.Book;
import com.example.mylib.api.response.LoanResponse;
import com.example.mylib.booklist.viewmodel.BooksViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.mylib.api.Constants.CLIENT;
import static com.example.mylib.api.Constants.PREFERENCE_FILE_KEY_USER_ROLE;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Book> mBooksList = new ArrayList<>();
    private Context mContext;
    private final static String SHARED_PREFERENCE_NAME = "USER";
    private static final String PREFERENCE_FILE_KEY_ID_USER = "user_id";
    private BooksViewModel mViewModel;
    private Disposable mDisposable;

    public BooksAdapter(Context applicationContext, BooksViewModel viewModel) {
        mLayoutInflater = LayoutInflater.from(applicationContext);
        mContext = applicationContext;
        mViewModel = viewModel;
    }

    public void setBooksList(List<Book> list) {
        mBooksList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Book book = mBooksList.get(position);
        String bookname = book.getName() + ",  " + book.getPublishingHouse();
        holder.mAuthor.setText(book.getAuthor());
        holder.mBookName.setText(bookname);
        if (book.getAvailability()) {
            holder.mAvailability.setImageResource(R.drawable.available_circle);

            //handle user role
            if (getSharedPreferenceUserRole().equals(CLIENT)) {
                holder.mLoanButton.setImageResource(R.drawable.loan_book_icon);
                holder.mLoanButton.setVisibility(View.VISIBLE);
                holder.mLoanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.loanBook(getSharedPreferenceUserId(), book.getId())
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
                                            holder.mLoanButton.setVisibility(View.GONE);
                                            holder.mAvailability.setImageResource(R.drawable.not_available_red_circle);
                                            Snackbar.make(holder.itemView, R.string.book_loaned_successfully, BaseTransientBottomBar.LENGTH_SHORT)
                                                    .show();
                                            Log.d("Loan Call : ", "Book Loaned succesfully!");
                                        } else {
                                            Log.d("Loan Call :", "Book loan failed!");
                                            Log.d("Loan Call :", "User id: " + getSharedPreferenceUserId());
                                            Log.d("Loan Call :", "Book id: " + book.getId());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d("Loan Call :", "Server error");

                                    }
                                });
                    }
                });
            } else {
                holder.mLoanButton.setImageResource(R.drawable.ic_remove_circle);
                holder.mLoanButton.setVisibility(View.VISIBLE);

                holder.mLoanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.deleteBook(book.getId())
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
                                            String message = "Book deleted successfully";
                                            mBooksList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemChanged(position);
                                            Snackbar.make(holder.itemView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            String message = "Cannot delete book";
                                            Snackbar.make(holder.itemView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }
                });

            }
        } else {
            holder.mAvailability.setImageResource(R.drawable.not_available_red_circle);
            holder.mLoanButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private int getSharedPreferenceUserId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(PREFERENCE_FILE_KEY_ID_USER, 0) != 0) {
            return sharedPreferences.getInt(PREFERENCE_FILE_KEY_ID_USER, 0);
        }
        return 0;
    }

    private String getSharedPreferenceUserRole() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT).isEmpty()) {
            return sharedPreferences.getString(PREFERENCE_FILE_KEY_USER_ROLE, Constants.CLIENT);
        }
        return Constants.CLIENT;
    }

    @Override
    public int getItemCount() {
        return mBooksList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_name)
        protected TextView mBookName;

        @BindView(R.id.author_static)
        protected TextView mAuthorStatic;

        @BindView(R.id.author)
        protected TextView mAuthor;

        @BindView(R.id.availability_icon)
        protected ImageView mAvailability;

        @BindView(R.id.bookaction_icon)
        protected ImageView mLoanButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
