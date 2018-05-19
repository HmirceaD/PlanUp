package com.example.mircea.moneymanager.Database.Entities.LiveData.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mircea.moneymanager.Database.Entities.DAO.ExpenseDao;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseDao expenseDao;
    private ExecutorService executorService;

    public ExpenseViewModel(@NonNull Application application) {

        super(application);
        expenseDao = ExpenseDatabase.getInstance(application).getExpenseDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ExpenseEntity>> getAllExepense(){
        return expenseDao.getExpensesAsync();
    }

    public void saveExpenses(ExpenseEntity expenseEntity){
        executorService.execute(()-> expenseDao.insertOneExpense(expenseEntity));
    }

    public void deledeExpense(ExpenseEntity expenseEntity){
        executorService.execute(()-> expenseDao.deleteExpense(expenseEntity));
    }

    public void updateExpense(ExpenseEntity expenseEntity){
        executorService.execute(()-> expenseDao.updateExpense(expenseEntity));
    }
}
