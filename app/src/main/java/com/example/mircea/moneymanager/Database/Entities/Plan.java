package com.example.mircea.moneymanager.Database.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "activeplan")
public class Plan {

    @PrimaryKey(autoGenerate = true)
    private int planId;

    @ColumnInfo(name = "budget")
    private float budget;

    @ColumnInfo(name = "startDate")
    private Date startDate;

    @ColumnInfo(name = "endDate")
    private Date endDate;

    public int getPlanId() {return planId;}

    public void setPlanId(int planId) {this.planId = planId;}

    public float getBudget() {return budget;}

    public void setBudget(float budget) {this.budget = budget;}

    public Date getStartDate() {return startDate;}

    public void setStartDate(Date startDate) {this.startDate = startDate;}

    public Date getEndDate() {return endDate;}

    public void setEndDate(Date endDate) {this.endDate = endDate;}
}
