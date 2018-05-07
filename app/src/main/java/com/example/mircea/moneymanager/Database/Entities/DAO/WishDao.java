package com.example.mircea.moneymanager.Database.Entities.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Database.Entities.WishEntity;

import java.util.List;

@Dao
public interface WishDao {

    @Query("SELECT * FROM WishEntity")
    List<WishEntity> getWishes();

    @Insert
    void insertWishes(List<WishEntity> expenses);

    @Update
    void updateWishes(WishEntity expense);

    @Delete
    void deleteWishes(WishEntity expense);
}
