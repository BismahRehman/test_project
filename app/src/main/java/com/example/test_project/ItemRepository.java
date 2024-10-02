package com.example.test_project;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        itemDao = database.itemDao();
        allItems = itemDao.getAllItems();
    }

    public void insert(Item item) {
        new Thread(() -> itemDao.insert(item)).start();
    }

    public void update(Item item) {
        new Thread(() -> itemDao.update(item)).start();
    }

    public void delete(Item item) {
        new Thread(() -> itemDao.delete(item)).start();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
}
