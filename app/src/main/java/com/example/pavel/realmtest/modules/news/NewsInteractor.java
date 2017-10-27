package com.example.pavel.realmtest.modules.news;

import android.content.Context;

import com.example.pavel.realmtest.core.models.ResponseModel;
import com.example.pavel.realmtest.core.rest.NewsService;
import com.example.pavel.realmtest.core.rest.RetrofitAbstractBuilder;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

public class NewsInteractor {

    public interface Callback{
        void onSuccess();
        void onFailure(final String error);
    }

    public static void updateNews(final Context context, final Callback callback){

        final RetrofitAbstractBuilder builder = new RetrofitAbstractBuilder(context);
        final NewsService service = builder.getNewsService();

        service.getNews().enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(final Call<ResponseModel> call, final Response<ResponseModel> response) {
                final Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(response.body().getArticles());
                        callback.onSuccess();
                    }
                });

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
