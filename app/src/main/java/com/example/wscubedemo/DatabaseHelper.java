package com.example.wscubedemo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Contacts.class, exportSchema = false, version = 3)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "contactdb";
    private static DatabaseHelper instance;
    public static synchronized DatabaseHelper getDB(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

//        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//            @Override
//            public void migrate(SupportSQLiteDatabase database) {
//                // Since we didn't alter the table, there's nothing else to do here.
//            }
//        };
        return instance;
    }

    public abstract ContactDAO contactDAO();
}
