package com.example.mircea.moneymanager.Database.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    public int expenseId;

    @ColumnInfo(name = "ExpenseDrawable")
    public int expenseDrawableId;

    @ColumnInfo(name = "ExpenseName")
    public String expenseName;

    @ColumnInfo(name = "ExpenseSpent")
    public float expenseSpent;

    @ColumnInfo(name = "ExpenseBudget")
    public float expenseBudget;

    public ExpenseEntity(int expenseDrawableId, String expenseName, float expenseBudget){

        this.expenseDrawableId = expenseDrawableId;
        this.expenseName = expenseName;
        this.expenseBudget = expenseBudget;
        this.expenseSpent = 0f;
    }
}
