package com.example.mircea.moneymanager.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mircea.moneymanager.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class PlannerFragment extends Fragment{

    //Global Values
    //TODO(2) this needs to be a shared preferences variable
    private int budget;

    //Ui
    private PieChart placintaChart;
    private TextView budgetTextView;

    //Chart logic
    //Pie
    private PieData pieData;
    private PieDataSet pieDataSet;
    //Bar

    //Logic
    private ArrayList<Float> values;
    private List<PieEntry> chartEntries;

    public PlannerFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void populateList() {

        //TODO(1): actually populate the list
        values.add(20f);
        values.add(40f);
        values.add(10f);
        values.add(30f);

        pieChartInit();


        //TODO(3) Add the rest of the charts here
    }

    private void pieChartInit() {
        chartEntries.add(new PieEntry(values.get(0), "Blue"));
        chartEntries.add(new PieEntry(values.get(1), "Green"));
        chartEntries.add(new PieEntry(values.get(2), "Red"));
        chartEntries.add(new PieEntry(values.get(3), "Yellow"));

        pieDataSet = new PieDataSet(chartEntries, "Pie Distribution");
        pieDataSet.setColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        pieData = new PieData(pieDataSet);
        placintaChart.setData(pieData);
        placintaChart.invalidate();
    }

    private void arrayInit() {
        values = new ArrayList<>();
        chartEntries = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.planner_fragment, container, false);

        placintaChart = view.findViewById(R.id.placintaChart);
        budgetTextView = view.findViewById(R.id.budgetTextView);

        budget = 302;

        budgetTextView.setText(getResources().getString(R.string.money_left) + budget);

        arrayInit();
        populateList();

        return view;

    }
}
