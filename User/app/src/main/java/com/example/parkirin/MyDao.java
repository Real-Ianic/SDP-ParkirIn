package com.example.parkirin;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {

    @Insert
    public void addSearchhistory(SearchHistory x);

    @Query("select * from searchhistory")
    public List<SearchHistory> getHistory();
}
