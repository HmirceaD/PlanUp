package com.example.mircea.moneymanager.Fragments;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mircea.moneymanager.Activities.MainActivity;
import com.example.mircea.moneymanager.Adapters.TransactionRecyclerAdapter;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.BudgetTransaction;
import com.example.mircea.moneymanager.RecyclerViewFluff.ShadowDecorator;
import com.example.mircea.moneymanager.RecyclerViewFluff.VerticalOffsetDecorator;
import com.github.mikephil.charting.charts.PieChart;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TransactionsFragment extends Fragment {

    //UI
    private Spinner alertTransactionCategoryNameSpinner;
    private FloatingActionButton floatingActionButton;
    private RecyclerView transactionRecyclerView;

    //Expense
    private List<ExpenseEntity> expenseEntityList;

    //RecyclerView stuff
    private TransactionRecyclerAdapter transactionRecyclerAdapter;
    private List<BudgetTransaction> budgetTransactionArrayList;

    public TransactionsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.transaction_fragment, container, false);

        setupUi(view);

        return view;

    }

    private void setupUi(View view) {

        setupFloatingButton(view);

        recyclerInit(view);

        //setupExpenseList();
    }

    private void setupExpenseList() {

        expenseEntityList = new ArrayList<>();
        ExpensesBackgroundThread expenseTask = new ExpensesBackgroundThread();
        expenseTask.execute();

    }

    private void setupFloatingButton(View view) {

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener((View v)-> wishDialog());
    }

    private void recyclerInit(View view) {

        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);
        budgetTransactionArrayList = new ArrayList<>();
        transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), budgetTransactionArrayList);

        getArrayListTransactions();

        transactionRecyclerView.setHasFixedSize(true);

        int verticalSpace = 48;
        VerticalOffsetDecorator verticalDecorator = new VerticalOffsetDecorator(verticalSpace);
        ShadowDecorator shadowDecorator = new ShadowDecorator(getContext(), R.drawable.recycler_shadow);

        transactionRecyclerView.addItemDecoration(shadowDecorator);
        transactionRecyclerView.addItemDecoration(verticalDecorator);

        RecyclerView.LayoutManager recyclerLayout = new LinearLayoutManager(getContext());
        transactionRecyclerView.setLayoutManager(recyclerLayout);

        transactionRecyclerView.setAdapter(transactionRecyclerAdapter);
    }

    private void wishDialog() {
        /**Sets the dialog wish up**/

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.transaction_alert_dialog, null);
        dialogBuilder.setView(myView);

        AlertDialog alertDialog = dialogBuilder.create();

        setupExpenseList();

        setAlertListeners(alertDialog, myView);
        alertDialog.show();
        //===== </StackOverflow>
    }

    private void setAlertListeners(AlertDialog alert, View view) {
        //TODO ADD IT IN THE DATABASE

        //=========UI
        alertTransactionCategoryNameSpinner = view.findViewById(R.id.alertTransactionCategoryNameSpinner);
        ImageView alertTransactionCategoryIcon = view.findViewById(R.id.alertTransactionCategoryIcon);
        alertTransactionCategoryIcon.setImageResource(android.R.color.transparent);

        //=========Listeners
        alertTransactionCategoryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //SET THE ICON OF THE EXPENSE

                try{
                    alertTransactionCategoryIcon.setImageResource(expenseEntityList.get(position).expenseDrawableId);
                }catch (Resources.NotFoundException ex){
                    alertTransactionCategoryIcon.setImageResource(android.R.color.transparent);
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<EditText> alertTransactionEditTextGroup = new ArrayList<>();

        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionName));
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionSum));
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionDate));
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionDescription));

        //Add Button
        view.findViewById(R.id.alertTransactionAddButton).setOnClickListener((View v)-> {

            //CHECK IF EVERYTHING IS GOOD THEN SAVE TO THE DB AND UPDATE STUFF I GUESS
            if(checkAlertDialog(alertTransactionEditTextGroup)){
                saveTransactionInDatabase(alertTransactionEditTextGroup);
            }

        });

        //Cancel Button
        view.findViewById(R.id.alertTransactionCancelButton).setOnClickListener((View v)-> alert.cancel());
    }

    private void saveTransactionInDatabase(List<EditText> alertTransactionEditTextGroup) {

        SaveTransactionBackgroundThread saveThread = new SaveTransactionBackgroundThread();

        //saveThread.execute(new BudgetTransaction(alertTransactionEditTextGroup.get(0).getText().toString()))

    }

    private boolean checkAlertDialog(List<EditText> alertTransactionEditTextGroup) {

        boolean isEmpty = true;

        for(int i = 0; i < 3; i++){

            if(alertTransactionEditTextGroup.get(i).getText().toString().equals("")){
                isEmpty = false;

            }else{
                Toast.makeText(getContext(), "Please put something in the first 3 fields", Toast.LENGTH_SHORT).show();
            }
        }

        return isEmpty;

    }

    private void getArrayListTransactions() {

        TransactionBackgroundThread task = new TransactionBackgroundThread();
        task.execute(budgetTransactionArrayList);
    }

    private class SaveTransactionBackgroundThread extends AsyncTask<BudgetTransaction, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {

            ((MainActivity)getContext()).finish();
            startActivity(((MainActivity)getContext()).getIntent());
        }

        @Override
        protected Void doInBackground(BudgetTransaction... budgetTransactions) {
            ExpenseDatabase.getInstance(getContext()).getTransactionDao().insertOneTransaction(budgetTransactions[0]);
            return null;
        }
    }

    private class TransactionBackgroundThread extends AsyncTask<Object, Object, List<BudgetTransaction>>{

        @Override
        protected void onPostExecute(List<BudgetTransaction> budgetTransactions) {

            budgetTransactionArrayList = budgetTransactions;
            transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), budgetTransactionArrayList);
            transactionRecyclerView.setAdapter(transactionRecyclerAdapter);
        }

        @Override
        protected List<BudgetTransaction> doInBackground(Object... objects) {

            return ExpenseDatabase.getInstance(getContext()).getTransactionDao().getTransactions();
        }
    }

    private class ExpensesBackgroundThread extends AsyncTask<Void, Void, List<ExpenseEntity>>{

        @Override
        protected void onPostExecute(List<ExpenseEntity> expenses) {

            expenseEntityList = expenses;

            ArrayList<String> expenseNames = new ArrayList<>();

            for(ExpenseEntity expenseEntity:expenses){

                expenseNames.add(expenseEntity.expenseName);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
            android.R.layout.simple_spinner_item, expenseNames);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            alertTransactionCategoryNameSpinner.setAdapter(dataAdapter);
        }

        @Override
        protected List<ExpenseEntity> doInBackground(Void... objects) {

            return ExpenseDatabase.getInstance(getContext()).getExpenseDao().getExpenses();

        }
    }
}
