package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Adapters.ExpenseListAdapter;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;

import java.util.ArrayList;


public class CreatePlanExpenses extends AppCompatActivity{

    //List Stuff
    private ExpenseListAdapter expenseArrayAdapter;
    private ArrayList<Expense> expenseArrayList;

    //Ui
    private Button goToSavingsButton;
    private ListView expensesList;
    private FloatingActionButton addExpenseButton;
    private TextView remainingBudgetTextView;

    //Logic
    //TODO this should be initialized at run time from the db/shp
    private float budget = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_expenses);

        setupUi();
    }

    @Override
    public void stopLocalVoiceInteraction() {
        super.stopLocalVoiceInteraction();
    }

    private void setupUi() {

        goToSavingsButton = findViewById(R.id.goToSavingsButton);
        goToSavingsButton.setOnClickListener((View v) -> goToSavings());

        remainingBudgetTextView = findViewById(R.id.remainingBudgetTextView);
        //TODO change this to not be harcoded
        changeBudgetTextView(budget);

        expensesList = findViewById(R.id.expensesList);
        setupList();

        addExpenseButton = findViewById(R.id.addExpense);
        addExpenseButton.setOnClickListener((View v) -> addExpense());
    }

    public void changeBudgetTextView(float dividedBudget) {
        remainingBudgetTextView.setText(Float.toString(dividedBudget));
    }

    private void setupList() {

        expenseArrayList = new ArrayList<>();

        populateInitialExpenses();

        expenseArrayAdapter = new ExpenseListAdapter(expenseArrayList, CreatePlanExpenses.this, budget);

        expensesList.setAdapter(expenseArrayAdapter);

    }

    private void addExpense() {

        expenseArrayList.add(new Expense(getDrawable(R.drawable.car_icon), "Car", 0f));
        expenseArrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
    }

    private void populateInitialExpenses() {

        expenseArrayList.add(new Expense(getDrawable(R.drawable.car_icon), "Car", 0f));
        expenseArrayList.add(new Expense(getDrawable(R.drawable.house_icon), "House", 0f));
        expenseArrayList.add(new Expense(getDrawable(R.drawable.food_icon), "Food", 0f));
    }

    private void goToSavings() {

        startActivity(new Intent(getApplicationContext(), CreatePlanSavings.class));
    }

    public float divideBudget(){

        float auxBudget = budget;

        for(Expense expense:expenseArrayList){
            auxBudget -= expense.getExpenseBudget();

        }

        return auxBudget;
    }

}
