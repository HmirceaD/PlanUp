package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mircea.moneymanager.R;

public class CreatePlanActivity extends AppCompatActivity {

    private Button goToExpensesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        setupUi();

    }

    private void setupUi() {

        goToExpensesButton = findViewById(R.id.goToExpenses);

        goToExpensesButton.setOnClickListener((View v) -> goToExpenses());
    }

    public void goToExpenses(){

        startActivity(new Intent(getApplicationContext(), CreatePlanExpenses.class));
    }
}
