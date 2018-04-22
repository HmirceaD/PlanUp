package com.example.mircea.moneymanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        this.lI = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        if(convertView == null){
            convertView = lI.inflate(R.layout.expense_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.expenseIcon = convertView.findViewById(R.id.expenseIcon);
            viewHolder.expenseName = convertView.findViewById(R.id.expenseName);
            viewHolder.expenseBudget = convertView.findViewById(R.id.expenseBudget);
            viewHolder.expenseColor = convertView.findViewById(R.id.expenseColor);
            viewHolder.expenseDelete = convertView.findViewById(R.id.expenseDelete);

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder)convertView.getTag();
        }

        Expense expense = expenseArrayList.get(position);

        viewHolder.expenseIcon.setImageDrawable(expense.getExpenseIcon());
        viewHolder.expenseName.setText(expense.getExpenseName());
        viewHolder.expenseBudget.setText("");
        viewHolder.expenseColor.setBackgroundColor(expense.getExpenseColor());
        viewHolder.expenseDelete.setImageResource(R.drawable.x_icon);
        viewHolder.expenseDelete.setOnClickListener((View v) -> deletePost(position));

        return convertView;
    }

    public void deletePost(int position){
        /*Delete the post int the list*/
        expenseArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Expense getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Expense item) {
        return super.getPosition(item);
    }


    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    public static class ViewHolder{

        ImageButton expenseIcon;
        TextView expenseName;
        EditText expenseBudget;
        ImageButton expenseColor;
        ImageButton expenseDelete;

    }
}
