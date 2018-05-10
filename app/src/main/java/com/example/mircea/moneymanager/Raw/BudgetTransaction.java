package com.example.mircea.moneymanager.Raw;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class BudgetTransaction {

//    private int categoryIconId;
//    private String categoryName;
//    private String transactionName;
//    private int transactionSum;
//    private Date transactionDate;
//    private String transactionDesc;
//
//    public BudgetTransaction(int categoryIconId, String categoryName, String transactionName, int transactionSum, Date transactionDate, String transactionDesc) {
//        this.categoryIconId = categoryIconId;
//        this.categoryName = categoryName;
//        this.transactionName = transactionName;
//        this.transactionSum = transactionSum;
//        this.transactionDate = transactionDate;
//        this.transactionDesc = transactionDesc;
//    }

    @PrimaryKey(autoGenerate = true)
    public int transactionId;

    @ColumnInfo(name = "CategoryIconId")
    public int categoryIconId;

    @ColumnInfo(name = "CategoryName")
    public String categoryName;

    @ColumnInfo(name = "TransactionName")
    public String transactionName;

    @ColumnInfo(name = "TransactionSum")
    public int transactionSum;

    @ColumnInfo(name = "TransactionDate")
    public Date transactionDate;

    @ColumnInfo(name = "TransactionDesc")
    public String transactionDesc;


    public BudgetTransaction(int categoryIconId, String categoryName, String transactionName, int transactionSum, Date transactionDate, String transactionDesc) {
        this.categoryIconId = categoryIconId;
        this.categoryName = categoryName;
        this.transactionName = transactionName;
        this.transactionSum = transactionSum;
        this.transactionDate = transactionDate;
        this.transactionDesc = transactionDesc;
    }


    public int getTransactionSum() {
        return transactionSum;
    }

    public void setTransactionSum(int transactionSum) {
        this.transactionSum = transactionSum;
    }

    public int getCategoryIconId() {
        return categoryIconId;
    }

    public void setCategoryIconId(int categoryIconId) {
        this.categoryIconId = categoryIconId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }
}
