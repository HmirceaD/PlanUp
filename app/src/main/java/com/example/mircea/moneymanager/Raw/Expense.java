package com.example.mircea.moneymanager.Raw;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class Expense {

    private Drawable expenseIcon;
    private String expenseName;
    private float expenseBudget;

    public Expense(){}

    public Expense(Drawable expenseIcon, String expenseName, float expenseBudget){

        this.expenseIcon = expenseIcon;
        this.expenseName = expenseName;
        this.expenseBudget = expenseBudget;

    }

    public Drawable getExpenseIcon() {return expenseIcon;}

    public void setExpenseIcon(Drawable expenseIcon) {this.expenseIcon = expenseIcon;}

    public String getExpenseName() {return expenseName;}

    public void setExpenseName(String expenseName) {this.expenseName = expenseName;}

    public float getExpenseBudget() {return expenseBudget;}

    public void setExpenseBudget(float expenseBudget) {this.expenseBudget = expenseBudget;}

}
