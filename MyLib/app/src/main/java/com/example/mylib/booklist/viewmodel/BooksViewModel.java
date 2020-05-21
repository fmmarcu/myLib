package com.example.mylib.booklist.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.mylib.api.model.Book;
import com.example.mylib.api.model.BookCall;
import com.example.mylib.api.model.Keyword;
import com.example.mylib.api.repository.BookRepository;
import com.example.mylib.api.response.IssueResponse;
import com.example.mylib.api.response.LoanResponse;
import com.example.mylib.api.response.LoanedBookResponse;
import com.example.mylib.api.response.ReturnResponse;

import java.util.ArrayList;

import io.reactivex.Single;

public class BooksViewModel extends ViewModel {

    private BookRepository mBookRepository;
    private static final String TAG = BooksViewModel.class.getSimpleName();

    public BooksViewModel() {
        super();
        mBookRepository = BookRepository.getInstance();
    }

    public void init() {
        mBookRepository = BookRepository.getInstance();
    }

    public Single<ArrayList<Book>> mGetAllBooks() {
        return mBookRepository.getBookList();
    }

    public Single<ArrayList<Book>> getBookSearch(Keyword keyword) {
        return mBookRepository.getBookSearch(keyword);
    }

    public Single<ArrayList<LoanedBookResponse>> getUserLoanedBooks(int id) {
        return mBookRepository.getUserLoanedBooks(id);
    }

    public Single<LoanResponse> loanBook(int userId, int bookId) {
        return mBookRepository.loanBook(userId, bookId);
    }

    public Single<ReturnResponse> returnBook(int loanId, int userId, int bookId) {
        return mBookRepository.returnBook(loanId, userId, bookId);
    }

    public Single<LoanResponse> issueBook(String bookName, String author, int id) {
        return mBookRepository.issueBook(bookName, author, id);
    }

    public Single<ArrayList<IssueResponse>> getAllBookIssues() {
        return mBookRepository.getAllIssues();
    }

    public Single<LoanResponse> deleteBookIssue(int id) {
        return mBookRepository.deleteBookIssue(id);
    }

    public Single<LoanResponse> deleteBook(int id) {
        return mBookRepository.deleteBook(id);
    }

    public Single<LoanResponse> addBook(BookCall bookCall) {
        return mBookRepository.addBook(bookCall);
    }

}
