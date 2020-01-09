package com.example.parkirin;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchhistory")
public class SearchHistory {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "judul")
    public String judul;

    public SearchHistory(String judul)
    {
        this.judul = judul;
    }
}
