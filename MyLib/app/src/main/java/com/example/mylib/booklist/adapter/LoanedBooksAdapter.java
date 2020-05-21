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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylib.R;
import com.example.mylib.api.model.Book;
import com.example.mylib.api.response.LoanedBookResponse;
import com.example.mylib.api.response.ReturnResponse;
import com.example.mylib.booklist.viewmodel.BooksViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoanedBooksAdapter extends RecyclerView.Adapter<LoanedBooksAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Book> mBooksList = new ArrayList<>();
    private Context mContext;
    private BooksViewModel mViewModel;
    private List<LoanedBookResponse> mLoanedBooks = new ArrayList<>();
    private Disposable mDisposable;

    private final static String SHARED_PREFERENCE_NAME = "USER";
    private static final String PREFERENCE_FILE_KEY_ID_USER = "user_id";

    public LoanedBooksAdapter(Context applicationContext, BooksViewModel viewModel) {
        mLayoutInflater = LayoutInflater.from(applicationContext);
        mContext = applicationContext;
        mViewModel = viewModel;
    }

    public void setBooksList(List<LoanedBookResponse> list) {
        mLoanedBooks = list;
    }

    @NonNull
    @Override
    public LoanedBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoanedBooksAdapter.ViewHolder holder, final int position) {
        final LoanedBookResponse loanedBookDetails = mLoanedBooks.get(position);
        final Book book = loanedBookDetails.getBook();

        String bookname = book.getName() + ",  " + book.getPublishingHouse();
        holder.mAuthor.setText(book.getAuthor());
        holder.mBookName.setText(bookname);

        if (loanedBookDetails.isOverdue()) {
            holder.mAvailabilityIcon.setImageResource(R.drawable.not_available_red_circle);
        } else {
            holder.mAvailabilityIcon.setImageResource(R.drawable.available_circle);
        }

        holder.mReturnIcon.setImageResource(R.drawable.return_book_icon);
        holder.mReturnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.returnBook(loanedBookDetails.getId(), getSharedPreferenceUserId(), book.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new SingleObserver<ReturnResponse>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onSuccess(ReturnResponse returnResponse) {
                                if (returnResponse.isSuccessful()) {
                                    String message = "Book returned successfully. Penalty Fee: "+returnResponse.getAmount();
                                    if(returnResponse.isPenalty()) {
                                        message+=" Please be more careful in the future!";
                                    }

                                    mLoanedBooks.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position);

                                    Snackbar.make(holder.itemView, message, BaseTransientBottomBar.LENGTH_LONG)
                                            .show();

//                                    }
                                }
                                Log.d("Book return call : ", returnResponse.isSuccessful() ? "Return successful" : "Return failed");

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("Book return call : ", "Server Error");

                            }
                        });
            }
        });


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

    @Override
    public int getItemCount() {
        Log.d("LoanedBooksSize", "size " + mLoanedBooks.size());
        return mLoanedBooks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_name)
        protected TextView mBookName;

        @BindView(R.id.author_static)
        protected TextView mAuthorStatic;

        @BindView(R.id.author)
        protected TextView mAuthor;

        @BindView(R.id.availability_icon)
        protected ImageView mAvailabilityIcon;

        @BindView(R.id.bookaction_icon)
        protected ImageView mReturnIcon;

        protected View mView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public View returnMyView() {
            return mView;
        }

    }
}
