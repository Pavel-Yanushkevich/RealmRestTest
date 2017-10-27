package com.example.pavel.realmtest.core.rest;

import android.content.Context;

import com.example.pavel.realmtest.R;
import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import retrofit2.Retrofit;

public class RetrofitAbstractBuilder {
    private Retrofit retrofit;
    private NewsService newsService;

    public RetrofitAbstractBuilder(final Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(LoganSquareConverterFactory.create()).baseUrl(context.getString(R.string.base_url))
                .build();
        newsService = retrofit.create(NewsService.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public NewsService getNewsService() {
        return newsService;
    }
}
