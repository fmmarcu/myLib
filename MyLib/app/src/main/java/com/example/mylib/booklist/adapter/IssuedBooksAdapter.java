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
import com.example.mylib.api.response.IssueResponse;
import com.example.mylib.api.response.LoanResponse;
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

import static android.view.View.GONE;

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<IssueResponse> mIssuedBooks = new ArrayList<>();
    private Context mContext;
    private BooksViewModel mViewModel;

    private Disposable mDisposable;

    private final static String SHARED_PREFERENCE_NAME = "USER";
    private static final String PREFERENCE_FILE_KEY_ID_USER = "user_id";

    public IssuedBooksAdapter(Context applicationContext, BooksViewModel viewModel) {
        mLayoutInflater = LayoutInflater.from(applicationContext);
        mContext = applicationContext;
        mViewModel = viewModel;
    }

    public void setBooksList(List<IssueResponse> list) {
        mIssuedBooks = list;
    }

    @NonNull
    @Override
    public IssuedBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IssuedBooksAdapter.ViewHolder holder, final int position) {
        final IssueResponse bookIssue = mIssuedBooks.get(position);

        String bookname = bookIssue.getName() + ", issue no. " + bookIssue.getId();
        holder.mAuthor.setText(bookIssue.getAuthor());
        holder.mBookName.setText(bookname);

        holder.mAvailabilityIcon.setVisibility(GONE);

        holder.mDeleteIcon.setVisibility(View.VISIBLE);
        holder.mDeleteIcon.setImageResource(R.drawable.ic_remove_circle);
        holder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteBookIssue(bookIssue.getId())
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
                                    String message = "Book issue deleted successfully";
                                    mIssuedBooks.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position);
                                    Snackbar.make(holder.itemView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                            .show();
                                } else {
                                    String message = "Cannot delete issue";
                                    Snackbar.make(holder.itemView, message, BaseTransientBottomBar.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                ;
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
        Log.d("Issued books", "size " + mIssuedBooks.size());
        return mIssuedBooks.size();
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
        protected ImageView mDeleteIcon;

        View mView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }
}
