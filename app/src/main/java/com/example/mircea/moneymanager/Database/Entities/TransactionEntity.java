package com.example.mircea.moneymanager.Database.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class TransactionEntity {
    //TODO REFACTOR THIS AND THE RAW CLASS INTO ONE FOR EFFICIENCY AND SHIT

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


    public TransactionEntity(int categoryIconId, String categoryName, String transactionName, int transactionSum, Date transactionDate, String transactionDesc) {
        this.categoryIconId = categoryIconId;
        this.categoryName = categoryName;
        this.transactionName = transactionName;
        this.transactionSum = transactionSum;
        this.transactionDate = transactionDate;
        this.transactionDesc = transactionDesc;
    }

}
