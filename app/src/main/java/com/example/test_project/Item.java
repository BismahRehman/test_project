package com.example.test_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private boolean checked;


    public Item(String name) {
        this.name = name;
        this.checked = false;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
