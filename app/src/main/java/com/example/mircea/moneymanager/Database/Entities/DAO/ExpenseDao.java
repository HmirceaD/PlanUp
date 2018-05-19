package com.example.mircea.moneymanager.Database.Entities.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Raw.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM ExpenseEntity")
    List<ExpenseEntity> getExpenses();

    @Query("SELECT * FROM ExpenseEntity")
    LiveData<List<ExpenseEntity>> getExpensesAsync();

    @Insert
    void insertExpense(List<ExpenseEntity> expenses);

    @Insert
    void insertOneExpense(ExpenseEntity expenses);

    @Update
    void updateExpense(ExpenseEntity expense);

    @Delete
    void deleteExpense(ExpenseEntity expense);
}
