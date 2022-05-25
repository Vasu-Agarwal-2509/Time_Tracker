package com.example.wscubedemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contacts {

    @PrimaryKey(autoGenerate = true)
    protected int id;

    @ColumnInfo(name = "start")
    protected String start;

    @ColumnInfo(name = "task")
    protected String task;

    @ColumnInfo(name = "end")
    protected String end;

    Contacts(String start, String task, String end){
        this.start = start;
        this.task = task;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
