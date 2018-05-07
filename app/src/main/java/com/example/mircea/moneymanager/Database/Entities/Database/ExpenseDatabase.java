package com.example.mircea.moneymanager.Database.Entities.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mircea.moneymanager.Database.Entities.DAO.ExpenseDao;
import com.example.mircea.moneymanager.Database.Entities.DAO.WishDao;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Database.Entities.WishEntity;
import com.example.mircea.moneymanager.Raw.Expense;

@Database(entities = {ExpenseEntity.class, WishEntity.class}, version = 2)
public abstract class ExpenseDatabase extends RoomDatabase {

    private static final String DB_Name = "expenseDatabase.db";
    private static ExpenseDatabase expenseDatabase;

    public static ExpenseDatabase getInstance(Context context){

        if(expenseDatabase == null){
            expenseDatabase = Room.databaseBuilder(context, ExpenseDatabase.class, DB_Name).build();
        }

        return expenseDatabase;
    }

    public abstract ExpenseDao getExpenseDao();
    public abstract WishDao getWishDao();

}
