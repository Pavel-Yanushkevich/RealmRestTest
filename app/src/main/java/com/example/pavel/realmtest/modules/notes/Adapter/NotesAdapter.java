package com.example.pavel.realmtest.modules.notes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.pavel.realmtest.R;
import com.example.pavel.realmtest.core.models.NotesModel;
import java.io.File;
import io.realm.RealmResults;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    private RealmResults<NotesModel> notes;
    private Context context;
    private File imageFile;
    public NotesAdapter(final Context context) {
        this.context = context;
    }

    public void updateData(final RealmResults<NotesModel> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, final int position) {
        final NotesModel notesModel = notes.get(position);
        holder.title.setText(notesModel.getTitle());
        holder.description.setText(notesModel.getDescription());
        Glide.with(context)
                .load(R.mipmap.ic_launcher)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (notes != null){
            return notes.size();
        } else {
            return 0;
        }
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title, description;

        NotesViewHolder(View itemView){
            super(itemView);
            this.image = (ImageView)itemView.findViewById(R.id.image_iv);
            this.title = (TextView) itemView.findViewById(R.id.titleTxv);
            this.description = (TextView) itemView.findViewById(R.id.descriptionTxv);
        }
    }
}
