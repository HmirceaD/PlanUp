package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mircea.moneymanager.R;

public class CreatePlanExpenses extends AppCompatActivity {

    private Button goToSavingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_expenses);

        setupUi();
    }

    private void setupUi() {

        goToSavingsButton = findViewById(R.id.goToSavingsButton);
        goToSavingsButton.setOnClickListener((View v) -> goToSavings());
    }

    private void goToSavings() {

        startActivity(new Intent(getApplicationContext(), CreatePlanSavings.class));
    }
}
