package com.example.mircea.moneymanager.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Adapters.ExpandableWishListAdapter;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Database.Entities.WishEntity;
import com.example.mircea.moneymanager.Listeners.HideKeyboardListener;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;
import com.example.mircea.moneymanager.Raw.Wish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePlanSavings extends AppCompatActivity {

    //Storage
    private SharedPreferences sharedPreferences;

    //Adapters
    private ExpandableWishListAdapter expandableWishListAdapter;

    //Wish
    private ArrayList<Wish> wishList;
    private ArrayList<String> wishHeader;
    private HashMap<String, List<String>> mapDataChild;

    //Ui
    private Button goToMainActivityButton;
    private Button savingsAddWishButton;
    private ExpandableListView expandableListView;
    private ConstraintLayout createSavingsLayout;
    private EditText savingsSavingEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_savings);

        setupUi();
    }

    private void setupUi() {

        setupSharePref();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        createSavingsLayout = findViewById(R.id.create_savings_layout);
        new HideKeyboardListener(createSavingsLayout, getApplicationContext());

        expandableListView = findViewById(R.id.wishExpandableListView);
        initWishList();

        savingsAddWishButton = findViewById(R.id.savingsAddWishButton);

        savingsAddWishButton.setOnClickListener((View v)-> wishDialog());

        savingsSavingEditText = findViewById(R.id.savingsSavingEditText);

        goToMainActivityButton = findViewById(R.id.goToMainActivityButton);
        goToMainActivityButton.setOnClickListener((View v) -> goToMainActivity());
    }

    private void setupSharePref() {
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);
    }

    private void initWishList() {

        wishList = new ArrayList<>();
        wishHeader = new ArrayList<>();
        mapDataChild = new HashMap<>();

        expandableWishListAdapter = new ExpandableWishListAdapter(this, wishHeader, mapDataChild);
        expandableListView.setAdapter(expandableWishListAdapter);
    }

    private void wishDialog() {

        /**Sets the dialog wish up**/
        //===== <StackOverflow>
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
        setAlertListeners(alert);
        //===== </StackOverflow>

    }

    private void setAlertListeners(AlertDialog alert) {
        //wish name
        TextView nameText = alert.findViewById(R.id.wishNameEditText);
        //Wish description
        TextView descText = alert.findViewById(R.id.wishDescriptionEditText);

        //Add Button
        alert.findViewById(R.id.wishAddButton).setOnClickListener((View v)-> {
            addButtonDialogListener(alert, nameText, descText);
        });
        //Cancel Button
        alert.findViewById(R.id.wishCancelButton).setOnClickListener((View v)-> alert.cancel());

    }

    private void addButtonDialogListener(AlertDialog alert, TextView nameText, TextView descText) {

        if(!nameText.getText().toString().equals("")){
            /**Adds a new wish from the dialog**/
            wishList.add(new Wish(nameText.getText().toString(), descText.getText().toString()));
            wishHeader.add(nameText.getText().toString());
            //TODO this kinda retarded cuz we dont need the hashmap value to be a list but a string so get on that boi
            List<String> auxChild = new ArrayList<>();
            auxChild.add(descText.getText().toString());

            mapDataChild.put(nameText.getText().toString(), auxChild);
            expandableWishListAdapter.notifyDataSetChanged();

            View v = getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            alert.cancel();

        }else{

            Toast.makeText(getApplicationContext(), getString(R.string.toast_wish_list_add_no_name), Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainActivity() {

        if(!savingsSavingEditText.getText().toString().equals("")){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    sharedPreferences.edit().putFloat(getString(R.string.shared_preferences_wish_savings_key),
                            Float.parseFloat(savingsSavingEditText.getText().toString())).apply();

                    ArrayList<WishEntity> wishEntityArrayList = new ArrayList<>();

                    for(Wish wish: wishList){

                        wishEntityArrayList.add(new WishEntity(wish.getName(), wish.getName()));
                    }

                    ExpenseDatabase
                            .getInstance(getApplicationContext())
                            .getWishDao()
                            .insertWishes(wishEntityArrayList);
                }
            }).start();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{

            Toast.makeText(getApplicationContext(), getString(R.string.toast_wish_no_savings_warning), Toast.LENGTH_SHORT).show();
        }
    }
}
