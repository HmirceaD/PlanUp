package com.example.mircea.moneymanager.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.mircea.moneymanager.R;

public class CreatePlanSavings extends AppCompatActivity {

    private Button goToMainActivityButton;
    private Button savingsAddWishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_savings);

        setupUi();
    }

    private void setupUi() {

        savingsAddWishButton = findViewById(R.id.savingsAddWishButton);

        savingsAddWishButton.setOnClickListener((View v)-> wishDialog());

        goToMainActivityButton = findViewById(R.id.goToMainActivityButton);
        goToMainActivityButton.setOnClickListener((View v) -> goToMainActivity());
    }

    private void wishDialog() {

        //DisplayMetrics metrics = getResources().getDisplayMetrics();
        //int width = metrics.widthPixels;
        //int height = metrics.heightPixels;

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        LayoutInflater inflater = LayoutInflater.from(this);
        View builderView = inflater.inflate(R.layout.wish_alert_dialog, null);
        builderView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        builderView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(builderView);
        AlertDialog alert = builder.create();
        alert.show();

        /*final Dialog dialog = new Dialog(getApplicationContext(), android.R.style.Theme_Light);
        //dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wish_alert_dialog);
        dialog.show();*/
    }

    private void goToMainActivity() {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
