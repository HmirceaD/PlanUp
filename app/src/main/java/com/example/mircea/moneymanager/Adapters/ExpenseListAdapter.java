package com.example.mircea.moneymanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;

import java.util.ArrayList;


public class ExpenseListAdapter extends ArrayAdapter<Expense> {

    //TODO(7) FINISH THIS

    private ArrayList<Expense> expenseArrayList;
    private Context mContext;

    private LayoutInflater lI;

    public ExpenseListAdapter(ArrayList<Expense> fA, Context c){
        super(c, R.layout.expense_list_item, fA);

        this.expenseArrayList = fA;
        this.mContext = c;
    }

    public static class ViewHolder{

        ImageButton expenseIcon;
        TextView expenseName;
        EditText expenseBudget;
        ImageButton expenseColor;


    }
}
