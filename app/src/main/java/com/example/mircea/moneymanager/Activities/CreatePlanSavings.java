package com.example.mircea.moneymanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mircea.moneymanager.R;

public class CreatePlanSavings extends AppCompatActivity {

    private Button goToMainActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_savings);

        setupUi();
    }

    private void setupUi() {

        goToMainActivityButton = findViewById(R.id.goToMainActivityButton);
        goToMainActivityButton.setOnClickListener((View v) -> goToMainActivity());
    }

    private void goToMainActivity() {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
