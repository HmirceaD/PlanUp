package com.example.mircea.moneymanager.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.ExpenseEntity;
import com.example.mircea.moneymanager.Database.Entities.LiveData.ViewModels.ExpenseViewModel;
import com.example.mircea.moneymanager.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class PlannerFragment extends Fragment{

    //ExpenseStuff
    private List<ExpenseEntity> expenseEntityList;
    private List<String> expenseNamesList;

    //Global Values
    private float budget;

    //Storage
    private SharedPreferences sharedPreferences;

    //Ui
    private PieChart placintaChart;
    private TextView budgetTextView;
    private BarChart barChart;

    //Chart logic
    //Pie
    private PieData pieData;
    private PieDataSet pieDataSet;
    //Bar

    //Logic
    private ArrayList<Float> values;
    private List<PieEntry> chartEntries;

    private ExpenseDatabase expenseDatabase;
    private List<BarEntry> barEntries;

    public PlannerFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.planner_fragment, container, false);

        initExpenseList();

        setupUi(view);

        initSharedPreferences();

        initBudget();

        return view;

    }

    private void initExpenseList() {

        //Ui entries
        //Bar chart
        barEntries = new ArrayList<>();
        //Pie Chart
        chartEntries = new ArrayList<>();

        expenseNamesList = new ArrayList<>();
        expenseEntityList = new ArrayList<>();

        expenseDatabase = ExpenseDatabase.getInstance(getContext());

        ExpensesBackgroundThread expenseTask = new ExpensesBackgroundThread();

        try {/**TODO SEE HOW TO HANDLE **/
            expenseTask.get(1000, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        expenseTask.execute();

    }

    private void setupUi(View view) {

        placintaChart = view.findViewById(R.id.placintaChart);
        budgetTextView = view.findViewById(R.id.budgetTextView);
        barChart = view.findViewById(R.id.barChart);

        //populatePie();
    }

    private void populateBar() {

        //Populate with the expense list

        BarDataSet set = new BarDataSet(barEntries, "BarDataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        //Setup the names for the expense stuff
        setupNames();
    }

    private void setupNames() {

        expenseNamesList.add("");

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return expenseNamesList.get((int)value);
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initBudget();

    }

    private void initBudget() {
        budget = sharedPreferences.getFloat(getString(R.string.shared_preferences_remaining_budget_key), 0f);

        budgetTextView.setText(getResources().getString(R.string.money_left) + budget);
    }

    private void initSharedPreferences() {

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);
    }

    private class ExpensesBackgroundThread extends AsyncTask<Void, Void, List<ExpenseEntity>> {

        @Override
        protected void onPostExecute(List<ExpenseEntity> expenses) {

            expenseEntityList = expenses;

            populateNameExpenses(expenses);

            populateBarEntries(expenses);

            populatePie(expenses);

        }

        @Override
        protected List<ExpenseEntity> doInBackground(Void... objects) {

            return expenseDatabase.getExpenseDao().getExpenses();
        }
    }

    private void populatePie(List<ExpenseEntity> expenses) {

        populatePieEntries(expenses);

        pieDataSet = new PieDataSet(chartEntries, "Pie Distribution");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieData = new PieData(pieDataSet);
        placintaChart.setData(pieData);
        placintaChart.invalidate();

    }

    private void populatePieEntries(List<ExpenseEntity> expenses) {
        for(ExpenseEntity expenseEntity:expenses){
            chartEntries.add(new PieEntry(expenseEntity.expenseBudget - expenseEntity.expenseSpent, expenseEntity.expenseName));
        }

        populateBar();
    }

    private void pieChartInit() {

        chartEntries.add(new PieEntry(values.get(0), "Blue"));
        chartEntries.add(new PieEntry(values.get(1), "Green"));
        chartEntries.add(new PieEntry(values.get(2), "Red"));
        chartEntries.add(new PieEntry(values.get(3), "Yellow"));

    }

    private void populateBarEntries(List<ExpenseEntity> expenses) {
        for(int i = 0; i < expenses.size(); i++){
            barEntries.add(new BarEntry((float)i, (float)expenseEntityList.get(i).expenseSpent));
        }
    }

    private void populateNameExpenses(List<ExpenseEntity> expenses) {
        for(ExpenseEntity expenseEntity:expenses){
            expenseNamesList.add(expenseEntity.expenseName);
        }
    }
}
