package com.example.mircea.moneymanager.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mircea.moneymanager.Activities.CreatePlanActivity;
import com.example.mircea.moneymanager.Activities.MainActivity;
import com.example.mircea.moneymanager.Adapters.TransactionRecyclerAdapter;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Database.Entities.LiveData.ViewModels.BudgetTransactionViewModel;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.BudgetTransaction;
import com.example.mircea.moneymanager.RecyclerViewFluff.ShadowDecorator;
import com.example.mircea.moneymanager.RecyclerViewFluff.VerticalOffsetDecorator;
import com.github.mikephil.charting.charts.PieChart;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionsFragment extends Fragment {

    private BudgetTransactionViewModel budgetTransactionViewModel;

    //Storage
    private SharedPreferences sharedPreferences;

    //Logic
    private float budget;
    private Date transactionDate;
    private Calendar myCalendar;
    private int iconId = -1;

    //UI
    private Spinner alertTransactionCategoryNameSpinner;
    private FloatingActionButton floatingActionButton;
    private RecyclerView transactionRecyclerView;

    //UI grouping
    private List<View> alertTransactionEditTextGroup;

    //Expense
    private List<ExpenseEntity> expenseEntityList;

    //RecyclerView stuff
    private TransactionRecyclerAdapter transactionRecyclerAdapter;
    private List<BudgetTransaction> budgetTransactionArrayList;
    private Date minCalendarDate;
    private Date maxCalendarDate;
    private DatePickerDialog datePickerDialog;

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

        setupSharePreferences();

        setupFloatingButton(view);

        recyclerInit(view);

        myCalendar = Calendar.getInstance();
    }

    private void setupSharePreferences() {

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        minCalendarDate = new Date(sharedPreferences.getLong(getString(R.string.shared_preferences_start_date_key), 0));
        maxCalendarDate = new Date(sharedPreferences.getLong(getString(R.string.shared_preferences_end_date_key), 0));
    }

    private void setupExpenseList() {

        budgetTransactionViewModel = ViewModelProviders.of(this).get(BudgetTransactionViewModel.class);

        expenseEntityList = new ArrayList<>();
        ExpensesBackgroundThread expenseTask = new ExpensesBackgroundThread();
        expenseTask.execute();

    }

    private void setupFloatingButton(View view) {

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener((View v)-> wishDialog());
    }

    private void recyclerInit(View view) {

        budgetTransactionViewModel = ViewModelProviders.of(this).get(BudgetTransactionViewModel.class);

        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);
        budgetTransactionArrayList = new ArrayList<>();
        transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), budgetTransactionArrayList, budgetTransactionViewModel);

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

        budgetTransactionViewModel.getAllTransactions().observe(this, transactions -> transactionRecyclerAdapter.setData(transactions));

    }


    @Override
    public void onPause() {
        super.onPause();
        //budgetSave();
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
    }

    private void setAlertListeners(AlertDialog alert, View view) {

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
                    iconId = expenseEntityList.get(position).expenseDrawableId;

                }catch (Resources.NotFoundException ex){
                    alertTransactionCategoryIcon.setImageResource(android.R.color.transparent);
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertTransactionEditTextGroup = new ArrayList<>();

        //0 NAME
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionName));

        //1 SUM
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionSum));

        //2 DATE
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionDate));

        //3 DESC
        alertTransactionEditTextGroup.add(view.findViewById(R.id.alertTransactionDescription));

        //4 CATEGORY NAME
        alertTransactionEditTextGroup.add(alertTransactionCategoryNameSpinner);

        //5 CATEGORY ID
        alertTransactionEditTextGroup.add(alertTransactionCategoryIcon);

        view.findViewById(R.id.alertTransactionDate).setOnClickListener((View v)-> showCalendar());

        //Add Button
        view.findViewById(R.id.alertTransactionAddButton).setOnClickListener((View v)-> {

            //CHECK IF EVERYTHING IS GOOD THEN SAVE TO THE DB AND UPDATE STUFF I GUESS
            if(checkAlertDialog(alertTransactionEditTextGroup)){
                //TODO DO THIS FOR REST OF STUFF
                /**Save this with live data***/
                alert.cancel();
                budgetTransactionViewModel.saveTransaction(buildBudgetTransaction(alertTransactionEditTextGroup));
            }

        });

        ((CheckBox)view.findViewById(R.id.alertTransactionTodayCheckbox))
                .setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> todayChecked(isChecked));

        //Cancel Button
        view.findViewById(R.id.alertTransactionCancelButton).setOnClickListener((View v)-> alert.cancel());
    }

    private void todayChecked(boolean isChecked) {

        if(isChecked){
            transactionDate = new Date();
            setDateInDateTextView(transactionDate);
        }else{
            //transactionDate = null;
            ((TextView)alertTransactionEditTextGroup.get(2)).setText("");
        }
    }

    private void showCalendar() {

        datePickerDialog = new DatePickerDialog(getActivity().getApplicationContext(), new DatePickerListener(),
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(minCalendarDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(maxCalendarDate.getTime());

        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        datePickerDialog.show();
    }

    private void saveTransactionInDatabase(List<View> alertTransactionEditTextGroup) {

        SaveTransactionBackgroundThread saveThread = new SaveTransactionBackgroundThread();

        saveThread.execute(buildBudgetTransaction(alertTransactionEditTextGroup));

    }


    private BudgetTransaction buildBudgetTransaction(List<View> alertTransactionEditTextGroup) {

        //WTF IS WRONG with me :( vvvvvvvvv
        return new BudgetTransaction(iconId,
                expenseEntityList.get(((Spinner)alertTransactionEditTextGroup.get(4)).getSelectedItemPosition()).expenseName,
                ((EditText)alertTransactionEditTextGroup.get(0)).getText().toString(),
                Integer.parseInt(((EditText)alertTransactionEditTextGroup.get(1)).getText().toString()),
                transactionDate,
                ((EditText)alertTransactionEditTextGroup.get(3)).getText().toString());
    }

    private boolean checkAlertDialog(List<View> alertTransactionEditTextGroup) {

            if(!((EditText)alertTransactionEditTextGroup.get(0)).getText().toString().equals("") &&
                    !((EditText)alertTransactionEditTextGroup.get(1)).getText().toString().equals("") &&
                    !((TextView)alertTransactionEditTextGroup.get(2)).getText().toString().equals("") &&
                    iconId != -1){
                return true;

            }else{
                Toast.makeText(getContext(), "Please put something in the first 3 fields", Toast.LENGTH_SHORT).show();
                return false;
            }
    }

    private void getArrayListTransactions() {

        TransactionBackgroundThread task = new TransactionBackgroundThread();
        task.execute(budgetTransactionArrayList);


    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            //SHOW A HOUR THING
            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);

                        transactionDate = myCalendar.getTime();
                        setDateInDateTextView(transactionDate);
                }
            }, hour, minute, true);//Yes 24 hour time


            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

    private void setDateInDateTextView(Date transactionDate) {

        String myFormat = "E/dd/MM/yy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        ((TextView)alertTransactionEditTextGroup.get(2)).setText(sdf.format(transactionDate));
    }

    private class SaveTransactionBackgroundThread extends AsyncTask<BudgetTransaction, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {

            Toast.makeText(getContext(), getString(R.string.toast_transaction_added), Toast.LENGTH_SHORT).show();

            Intent it = new Intent(getContext(), MainActivity.class);
            it.putExtra("CURRENT_PAGE", 1);

            ((MainActivity)getContext()).finish();
            getContext().startActivity(it);

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
            transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), budgetTransactionArrayList, budgetTransactionViewModel);
            transactionRecyclerView.setAdapter(transactionRecyclerAdapter);
        }

        @Override
        protected List<BudgetTransaction> doInBackground(Object... objects) {

            return ExpenseDatabase.getInstance(getContext()).getTransactionDao().getTransactionsAsync();
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
