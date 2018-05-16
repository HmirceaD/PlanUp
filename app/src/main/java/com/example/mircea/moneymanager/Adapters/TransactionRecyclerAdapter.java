package com.example.mircea.moneymanager.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Activities.MainActivity;
import com.example.mircea.moneymanager.Database.Entities.Database.ExpenseDatabase;
import com.example.mircea.moneymanager.Database.Entities.LiveData.ViewModels.BudgetTransactionViewModel;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.BudgetTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionHolder> {

    private Context mContext;
    private List<BudgetTransaction> budgetTransactionArrayList;
    private SharedPreferences sharedPreferences;
    private BudgetTransactionViewModel budgetTransactionViewModel;

    public TransactionRecyclerAdapter(Context mContext, List<BudgetTransaction> budgetTransactionArrayList, BudgetTransactionViewModel budgetTransactionViewModel) {
        this.mContext = mContext;
        this.budgetTransactionArrayList = budgetTransactionArrayList;
        this.budgetTransactionViewModel = budgetTransactionViewModel;

        sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);
    }

    public void setData(List<BudgetTransaction> newData){
        if(budgetTransactionArrayList != null){

            TransDiffCallback transDiffCallback = new TransDiffCallback(budgetTransactionArrayList, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(transDiffCallback);

            budgetTransactionArrayList.clear();
            budgetTransactionArrayList.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }

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
    //TODO CHANGE LAYOUT SO IT DISPLAY THE DATE AND IF THE DESC IS TO LARGE go ...

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {

        BudgetTransaction budgetTransaction = this.budgetTransactionArrayList.get(position);

        holder.bindExpense(budgetTransaction);

    }

    @Override
    public int getItemCount() {
        return this.budgetTransactionArrayList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private final ConstraintLayout transitionLayout;
        private final LinearLayout transitionDummyLayout;
        private final ImageView transitionIcon;
        private final TextView transitionCategoryName;
        private final TextView transitionName;
        private final TextView transitionSum;
        private final TextView transitionDate;
        private final TextView transitionDesc;

        private Context mContext;
        private AlertDialog alertDialog;
        //private BudgetTransaction transaction;

        public TransactionHolder(Context mContext, View itemView) {

            super(itemView);

            itemView.setOnLongClickListener(this);
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

            String myFormat = "E/dd/MM/yy hh:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            this.transitionDate.setText(sdf.format(budgetTransaction.getTransactionDate()));
            this.transitionDesc.setText(budgetTransaction.getTransactionDesc());

        }

        @Override
        public boolean onLongClick(View v) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = ((MainActivity)mContext).getLayoutInflater();
            final View myView = inflater.inflate(R.layout.delete_transaction_dialog, null);
            dialogBuilder.setView(myView);

            alertDialog = dialogBuilder.create();
            //Listeners

            myView.findViewById(R.id.deleteTransactionAlertNo).setOnClickListener((View view)-> alertDialog.cancel());
            myView.findViewById(R.id.deleteTransactionAlertYes).setOnClickListener((View view)-> {
                //DeleteItemBackgroundThread deleteItemBackgroundThread = new DeleteItemBackgroundThread();
                //deleteItemBackgroundThread.execute(getAdapterPosition());
                alertDialog.cancel();
                budgetTransactionViewModel.deleteTransaction(budgetTransactionArrayList.get(0));

            });

            alertDialog.show();

            return false;
        }

        private class DeleteItemBackgroundThread extends AsyncTask<Integer, Integer, Void>{
            @Override
            protected void onPostExecute(Void aVoid){

                alertDialog.cancel();

                notifyDataSetChanged();

                Intent it = new Intent(mContext, MainActivity.class);
                it.putExtra("CURRENT_PAGE", 1);


                ((MainActivity)mContext).finish();
                mContext.startActivity(it);
            }

            @Override
            protected Void doInBackground(Integer... integers) {

                ExpenseDatabase.getInstance(mContext).getTransactionDao().deleteTransactions(
                        budgetTransactionArrayList.get(integers[0]));

                return null;
            }
        }

    }
    class TransDiffCallback extends DiffUtil.Callback {

        private final List<BudgetTransaction> oldPosts, newPosts;

        public TransDiffCallback(List<BudgetTransaction> oldPosts, List<BudgetTransaction> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).getCategoryName() == newPosts.get(newItemPosition).getCategoryName();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }
}
