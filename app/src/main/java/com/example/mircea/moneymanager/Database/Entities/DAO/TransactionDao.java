package com.example.mircea.moneymanager.Database.Entities.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mircea.moneymanager.Raw.BudgetTransaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM BudgetTransaction")
    LiveData<List<BudgetTransaction>> getTransactions();

    @Query("SELECT * FROM BudgetTransaction")
    List<BudgetTransaction> getTransactionsAsync();

    @Insert
    void insertTransactions(List<BudgetTransaction> budgetTransactions);

    @Insert
    void insertOneTransaction(BudgetTransaction budgetTransaction);

    @Update
    void updateTransactions(BudgetTransaction budgetTransaction);

    @Delete
    void deleteTransactions(BudgetTransaction budgetTransaction);
}
