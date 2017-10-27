package com.example.pavel.realmtest.core.rest;

import com.example.pavel.realmtest.core.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsService {
    @GET("articles?source=bloomberg&sortBy=top&apiKey=feb279f318fa45659201574012af2553")
    Call<ResponseModel> getNews();
}
