package com.example.mircea.moneymanager.Adapters;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.BudgetTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionHolder> {

    private Context mContext;
    private List<BudgetTransaction> budgetTransactionArrayList;

    public TransactionRecyclerAdapter(Context mContext, List<BudgetTransaction> budgetTransactionArrayList) {
        this.mContext = mContext;
        this.budgetTransactionArrayList = budgetTransactionArrayList;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_recycler_item, parent, false);

        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));


        TransactionHolder transactionHolder = new TransactionHolder(this.mContext, view);
        return transactionHolder;
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {

        BudgetTransaction budgetTransaction = this.budgetTransactionArrayList.get(position);

        holder.bindExpense(budgetTransaction);
    }

    @Override
    public int getItemCount() {
        return this.budgetTransactionArrayList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout transitionLayout;
        private final LinearLayout transitionDummyLayout;
        private final ImageView transitionIcon;
        private final TextView transitionCategoryName;
        private final TextView transitionName;
        private final TextView transitionSum;
        private final TextView transitionDate;
        private final TextView transitionDesc;

        private Context mContext;
        //private BudgetTransaction transaction;

        public TransactionHolder(Context mContext, View itemView) {

            super(itemView);

            this.mContext = mContext;

            this.transitionLayout = itemView.findViewById(R.id.transactionItemLayout);
            this.transitionDummyLayout = itemView.findViewById(R.id.transactionDummyLayout);
            this.transitionIcon = itemView.findViewById(R.id.categoryValueImageView);
            this.transitionCategoryName = itemView.findViewById(R.id.categoryValueTextView);
            this.transitionName = itemView.findViewById(R.id.nameValueTextView);
            this.transitionSum = itemView.findViewById(R.id.sumValueTextView);
            this.transitionDate = itemView.findViewById(R.id.dateValueTextView);
            this.transitionDesc = itemView.findViewById(R.id.descValueTextView);

        }

        public void bindExpense(BudgetTransaction budgetTransaction) {

            this.transitionIcon.setImageResource(budgetTransaction.getCategoryIconId());
            this.transitionCategoryName.setText(budgetTransaction.getCategoryName());
            this.transitionName.setText(budgetTransaction.getTransactionName());
            this.transitionSum.setText(Integer.toString(budgetTransaction.getTransactionSum()));

            String myFormat = "E/dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            this.transitionDate.setText(sdf.format(budgetTransaction.getTransactionDate()));
            this.transitionDesc.setText(budgetTransaction.getTransactionDesc());

        }

    }
}
