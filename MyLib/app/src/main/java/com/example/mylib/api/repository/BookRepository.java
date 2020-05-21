package com.example.mylib.api.repository;

import com.example.mylib.api.DataService;
import com.example.mylib.api.RetrofitClient;
import com.example.mylib.api.model.Book;
import com.example.mylib.api.model.BookCall;
import com.example.mylib.api.model.IdPost;
import com.example.mylib.api.model.IssueBookCall;
import com.example.mylib.api.model.Keyword;
import com.example.mylib.api.model.LoanCall;
import com.example.mylib.api.model.ReturnCall;
import com.example.mylib.api.response.IssueResponse;
import com.example.mylib.api.response.LoanResponse;
import com.example.mylib.api.response.LoanedBookResponse;
import com.example.mylib.api.response.ReturnResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class BookRepository {

    private static final String TAG = BookRepository.class.getSimpleName();

    private DataService mDataService;

    private BookRepository(){
        mDataService = RetrofitClient.createService(DataService.class);
    }

    public static BookRepository getInstance() {
        return new BookRepository();
    }

    public Single<ArrayList<Book>> getBookList() {
        return mDataService.getAllBooks();
    }

    public Single<ArrayList<Book>> getBookSearch(Keyword keyword) {
        return mDataService.getBookSearch(keyword);
    }

    public Single<ArrayList<LoanedBookResponse>> getUserLoanedBooks(int id) {
        return mDataService.getUserLoans(new IdPost(id));
    }

    public Single<LoanResponse> loanBook(int userId, int bookId) {
        return mDataService.makeLoan(new LoanCall(userId, bookId));
    }

    public Single<ReturnResponse> returnBook(int loanId, int userId, int bookId) {
        return mDataService.returnBook(new ReturnCall(loanId, userId, bookId));
    }

    public Single<LoanResponse> issueBook(String bookName, String author, int id) {
        return mDataService.issueBook(new IssueBookCall(bookName, author, id));
    }

    public Single<ArrayList<IssueResponse>> getAllIssues() {
        return mDataService.getAllBookIssues();
    }

    public Single<LoanResponse> deleteBookIssue(int id) {
        return mDataService.deleteBookIssue(new IdPost(id));
    }

    public Single<LoanResponse> deleteBook(int id) {
        return mDataService.deleteBook(new IdPost(id));
    }

    public Single<LoanResponse> addBook(BookCall bookCall) {
        return mDataService.addBook(bookCall);
    }
}
