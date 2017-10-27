package com.example.pavel.realmtest.modules.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pavel.realmtest.R;
import com.example.pavel.realmtest.core.models.NewsModel;

import java.text.SimpleDateFormat;

import io.realm.RealmResults;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private RealmResults<NewsModel> news;
    private SimpleDateFormat dateFormat;
    private Context context;

    public NewsAdapter(final Context context) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.context = context;
    }

    public void updateData(final RealmResults<NewsModel> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final NewsModel newsModel = news.get(position);
        holder.title.setText(newsModel.getTitle());
        holder.description.setText(newsModel.getDescription());
        if(newsModel.getPublishedAt() != null){
            holder.date.setText(dateFormat.format(newsModel.getPublishedAt()));
        } else{
            holder.date.setText("");
        }
        holder.author.setText(newsModel.getAuthor());
        Glide.with(context).load(newsModel.getUrlToImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (news != null){
            return news.size();
        } else {
            return 0;
        }
    }


    class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private TextView description;
        private TextView author;
        private TextView date;

        NewsViewHolder(View itemView){
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.image_iv);
            this.title = (TextView) itemView.findViewById(R.id.titleTxv);
            this.description = (TextView) itemView.findViewById(R.id.descriptionTxv);
            this.author = (TextView) itemView.findViewById(R.id.author_txv);
            this.date = (TextView) itemView.findViewById(R.id.date_txv);
        }
    }
}
