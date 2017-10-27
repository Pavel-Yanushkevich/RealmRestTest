package com.example.pavel.realmtest.modules.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pavel.realmtest.R;
import com.example.pavel.realmtest.core.models.NewsModel;
import com.example.pavel.realmtest.modules.news.NewsInteractor;
import com.example.pavel.realmtest.modules.news.adapter.NewsAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsInteractor.Callback {
    private SwipeRefreshLayout refreshLayout;
    private NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news,null);
        if (view != null){
            refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
            refreshLayout.setOnRefreshListener(this);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            final RecyclerView newsList = (RecyclerView)view.findViewById(R.id.newsListView);
            newsAdapter = new NewsAdapter(getActivity());
            newsList.setAdapter(newsAdapter);
            newsList.setLayoutManager(llm);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNews();

    }

    @Override
    public void onRefresh() {
        NewsInteractor.updateNews(getActivity(),this);
    }

    @Override
    public void onSuccess() {
        refreshLayout.setRefreshing(false);
        updateNews();
    }

    @Override
    public void onFailure(String error) {
        refreshLayout.setRefreshing(false);
    }

    private void updateNews() {
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<NewsModel> news = realm.where(NewsModel.class).findAllSorted("publishedAt", Sort.DESCENDING);
        newsAdapter.updateData(news);
    }
}

