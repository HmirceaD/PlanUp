package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Adapters.ExpenseRecyclerAdapter;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;
import com.example.mircea.moneymanager.RecyclerViewFluff.ShadowDecorator;
import com.example.mircea.moneymanager.RecyclerViewFluff.VerticalOffsetDecorator;

import java.util.ArrayList;

import static com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase.getInstance;


public class CreatePlanExpenses extends AppCompatActivity{

    //Room Database
    private static ExpenseDatabase expenseDatabase;

    //List Stuff
    private ExpenseRecyclerAdapter expenseRecyclerAdapter;
    private ArrayList<Expense> expenseArrayList;

    //Ui
    private Button goToSavingsButton;
    private RecyclerView expensesList;
    private FloatingActionButton addExpenseButton;
    private TextView remainingBudgetTextView;

    //Logic
    //TODO this should be initialized at run time from the db/shp
    private float budget = 1337;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_expenses);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(CreatePlanExpenses.this, R.layout.expense_list_item, expenseArrayList, 1337);
        expensesList.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerLayout = new LinearLayoutManager(this);
        int verticalSpace = 48;
        VerticalOffsetDecorator verticalDecorator = new VerticalOffsetDecorator(verticalSpace);
        ShadowDecorator shadowDecorator = new ShadowDecorator(this, R.drawable.recycler_shadow);

        expensesList.addItemDecoration(shadowDecorator);
        expensesList.addItemDecoration(verticalDecorator);

        expensesList.setLayoutManager(recyclerLayout);
        expensesList.setAdapter(expenseRecyclerAdapter);

    }

    private void addExpense() {

        expenseArrayList.add(new Expense(R.drawable.car_icon, "Car", 0f));
        expenseRecyclerAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
    }

    private void populateInitialExpenses() {

        expenseArrayList.add(new Expense(R.drawable.car_icon, "Car", 0f));
        expenseArrayList.add(new Expense(R.drawable.house_icon, "House", 0f));
        expenseArrayList.add(new Expense(R.drawable.food_icon, "Food", 0f));
    }

    private void goToSavings() {

        if(expenseArrayList.size() < 1){
            //EmptyList
            Toast.makeText(getApplicationContext(), getString(R.string.toast_expense_small_list_warning), Toast.LENGTH_SHORT).show();
        }else{

            new Thread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<ExpenseEntity> expenseEntityArrayList = new ArrayList<>();

                    for(Expense expense: expenseArrayList){

                        expenseEntityArrayList.add(new ExpenseEntity(expense.getExpenseIcon(), expense.getExpenseName(), expense.getExpenseBudget()));
                    }

                    ExpenseDatabase
                            .getInstance(getApplicationContext())
                            .getExpenseDao()
                            .insertExpense(expenseEntityArrayList);
                }
            }).start();

            startActivity(new Intent(getApplicationContext(), CreatePlanSavings.class));

        }
    }

    public float divideBudget(){

        float auxBudget = budget;

        for(Expense expense:expenseArrayList){
            auxBudget -= expense.getExpenseBudget();

        }

        return auxBudget;
    }

}
