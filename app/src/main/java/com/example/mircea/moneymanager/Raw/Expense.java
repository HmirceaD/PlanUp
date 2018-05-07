package com.example.mircea.moneymanager.Raw;

import android.graphics.drawable.Drawable;

public class Expense {

    private int expenseIconId;
    private String expenseName;
    private float expenseBudget;

    public Expense(){}

    public Expense(int expenseIconId, String expenseName, float expenseBudget){

        this.expenseIconId = expenseIconId;
        this.expenseName = expenseName;
        this.expenseBudget = expenseBudget;

    }

    public int getExpenseIcon() {return expenseIconId;}

    public void setExpenseIcon(int expenseIcon) {this.expenseIconId = expenseIcon;}

    public String getExpenseName() {return expenseName;}

    public void setExpenseName(String expenseName) {this.expenseName = expenseName;}

    public float getExpenseBudget() {return expenseBudget;}

    public void setExpenseBudget(float expenseBudget) {this.expenseBudget = expenseBudget;}

}
