package com.example.countdetector;

import com.example.countdetector.RetrofitRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET(".")
// path to of the url to which the data is to be send
    Call<ResponseBody> sendCount(RetrofitRequest sendCount);
}
