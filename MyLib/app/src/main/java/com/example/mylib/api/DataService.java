package com.example.mylib.api;

import com.example.mylib.api.model.Book;
import com.example.mylib.api.model.BookCall;
import com.example.mylib.api.model.IdPost;
import com.example.mylib.api.model.IssueBookCall;
import com.example.mylib.api.model.Keyword;
import com.example.mylib.api.model.LoanCall;
import com.example.mylib.api.model.ReturnCall;
import com.example.mylib.api.model.User;
import com.example.mylib.api.response.IssueResponse;
import com.example.mylib.api.response.LoanResponse;
import com.example.mylib.api.response.LoanedBookResponse;
import com.example.mylib.api.response.LoginResponse;
import com.example.mylib.api.response.RegisterPost;
import com.example.mylib.api.response.RegisterResponse;
import com.example.mylib.api.response.ReturnResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @POST("login")
    Single<LoginResponse> loginUser(@Body User user);

    @POST("addUser")
    Single<RegisterResponse> registerUser(@Body RegisterPost registerPost);

    @GET("books")
    Single<ArrayList<Book>> getAllBooks();

    @POST("search")
    Single<ArrayList<Book>> getBookSearch(@Body Keyword keyword);

    @POST("getActiveLoans")
    Single<ArrayList<LoanedBookResponse>> getUserLoans(@Body IdPost id);

    @POST("loanBook")
    Single<LoanResponse> makeLoan(@Body LoanCall loanCall);

    @POST("returnBook")
    Single<ReturnResponse> returnBook(@Body ReturnCall returnCall);

    @POST("issueBook")
    Single<LoanResponse> issueBook(@Body IssueBookCall issueBookCall);

    @GET("getIssueBooks")
    Single<ArrayList<IssueResponse>> getAllBookIssues();

    @POST("deleteIssue")
    Single<LoanResponse> deleteBookIssue(@Body IdPost id);

    @POST("deleteBook")
    Single<LoanResponse> deleteBook(@Body IdPost id);

    @POST("addBook")
    Single<LoanResponse> addBook(@Body BookCall bookCall);
}
