package com.example.wscubedemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Query("select * from contact_table")
    List<Contacts> getAllContact();

    @Insert
    void addContact(Contacts contacts);

    @Update
    void updateContact(Contacts contacts);

    @Delete
    void deleteContact(Contacts contacts);
}
