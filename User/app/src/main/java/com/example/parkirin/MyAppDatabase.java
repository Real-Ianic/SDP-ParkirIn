package com.example.parkirin;

import androidx.room.Database;

@Database(entities = {SearchHistory.class}, version = 1)
public abstract class MyAppDatabase extends androidx.room.RoomDatabase {
    public abstract MyDao myDao();
}
