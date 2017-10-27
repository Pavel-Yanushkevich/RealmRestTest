package com.example.pavel.realmtest.modules.notes.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavel.realmtest.R;

import com.example.pavel.realmtest.core.models.NotesModel;
import com.example.pavel.realmtest.modules.notes.Adapter.NotesAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotesFragment extends Fragment {
    private NotesAdapter notesAdapter;
    private EditText title_edt, description_edt;
    private RealmResults<NotesModel> notes;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notes, null);
        if (view != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            final RecyclerView notesList = (RecyclerView) view.findViewById(R.id.notesList);
            notesAdapter = new NotesAdapter(getActivity());
            notesList.setAdapter(notesAdapter);
            notesList.setLayoutManager(llm);

            //recyclerview swipe to delete
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    if (direction == ItemTouchHelper.LEFT) {
                                notesAdapter.notifyItemRemoved(position);
                                final Realm realm = Realm.getDefaultInstance();
                                notes = realm.where(NotesModel.class).findAll();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        NotesModel notesModel = notes.get(position);
                                        notesModel.deleteFromRealm();
                                    }
                                });
                                updateNotes();
                                return;
                            }
                    }

            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(notesList);

            //floating action button with dialog
            final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    final View dialogView = inflater.inflate(R.layout.addnote, null);
                    title_edt = (EditText) dialogView.findViewById(R.id.title_edt);
                    description_edt = (EditText) dialogView.findViewById(R.id.description_edt);
                    final Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
                    final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
                    final TextView addPhoto_txv= (TextView) dialogView.findViewById(R.id.addPhoto_txv);
                    final TextView title_txv= (TextView) dialogView.findViewById(R.id.title_txv);
                    final ImageView camera_iv = (ImageView) dialogView.findViewById(R.id.camera_iv);
                    final ImageView gallery_iv = (ImageView) dialogView.findViewById(R.id.gallery_iv);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    addPhoto_txv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(camera_iv.isClickable()){
                                addPhoto_txv.setBackgroundColor(Color.WHITE);
                                camera_iv.setVisibility(dialogView.INVISIBLE);
                                camera_iv.setClickable(false);
                                gallery_iv.setVisibility(dialogView.INVISIBLE);
                                gallery_iv.setClickable(false);
                            }
                            else{
                                addPhoto_txv.setBackgroundResource(R.color.colorAccent);
                                camera_iv.setVisibility(dialogView.VISIBLE);
                                camera_iv.setClickable(true);
                                gallery_iv.setVisibility(dialogView.VISIBLE);
                                gallery_iv.setClickable(true);

                                camera_iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getActivity(), "is working with camera...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                gallery_iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getActivity(), "is working with gallery...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (title_edt.getText().toString().isEmpty()) {
                                title_txv.setVisibility(dialogView.VISIBLE);
                            }
                            else {
                                saveNote();
                                dialog.cancel();
                            }
                            updateNotes();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                }
            });
        }
        return view;
    }


    private void saveNote() {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                    NotesModel notesModel = new NotesModel();
                    notesModel.setTitle(title_edt.getText().toString());
                    notesModel.setDescription(description_edt.getText().toString());
                    notes = realm.where(NotesModel.class).findAll();
                    realm.copyToRealmOrUpdate(notesModel);

            }
        });
    }


    private void updateNotes() {
        final Realm realm = Realm.getDefaultInstance();
        notes = realm.where(NotesModel.class).findAll();
        notesAdapter.updateData(notes);
    }



    @Override
    public void onResume() {
        super.onResume();
        updateNotes();
    }
}
