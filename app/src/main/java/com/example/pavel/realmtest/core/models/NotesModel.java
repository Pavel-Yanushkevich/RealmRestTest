package com.example.pavel.realmtest.core.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotesModel extends RealmObject{

    @PrimaryKey
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
