package com.example.mircea.moneymanager.Database.Entities.LiveData.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mircea.moneymanager.Database.Entities.DAO.TransactionDao;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Raw.BudgetTransaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mircea on 15.05.2018.
 */

public class BudgetTransactionViewModel extends AndroidViewModel {

    private TransactionDao transactionDao;
    private ExecutorService executorService;

    public BudgetTransactionViewModel(@NonNull Application application) {

        super(application);
        transactionDao = ExpenseDatabase.getInstance(application).getTransactionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<BudgetTransaction>> getAllTransactions() {
        return transactionDao.getTransactions();
    }

    public void saveTransaction(BudgetTransaction transaction) {
        executorService.execute(() -> transactionDao.insertOneTransaction(transaction));
    }

    public void deleteTransaction(BudgetTransaction transaction) {
        executorService.execute(() -> transactionDao.deleteTransactions(transaction));
    }
}
