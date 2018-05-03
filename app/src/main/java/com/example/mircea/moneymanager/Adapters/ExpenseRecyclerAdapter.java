package com.example.mircea.moneymanager.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Activities.CreatePlanExpenses;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;

import java.util.ArrayList;

/**
 * Created by Mircea on 01.05.2018.
 */

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ExpenseHolder> {


    private Context mContext;
    private ArrayList<Expense> expenseArrayList;
    private int itemResource;
    private float budget;

    private static int[] iconsListId;
    private static int[] iconListDrawable;


    public ExpenseRecyclerAdapter(Context mContext, int itemResource, ArrayList<Expense> expenses, float budget) {

        this.expenseArrayList = expenses;
        this.mContext = mContext;
        this.itemResource = itemResource;
        this.budget = budget;

        iconsListId = new int[]{R.id.car_icon,R.id.cart_icon,R.id.dress_icon,R.id.food_icon,R.id.game_icon,R.id.gift_icon,R.id.house_icon,R.id.internet_icon,
                R.id.phone_icon,R.id.repair_icon,R.id.shirt_icon,R.id.shoes_icon,R.id.travel_icon,R.id.utilities_icon,R.id.wifi_icon};

        iconListDrawable = new int[]{R.drawable.car_icon,R.drawable.cart_icon,R.drawable.dress_icon,R.drawable.food_icon,R.drawable.game_icon,R.drawable.gift_icon,R.drawable.house_icon,R.drawable.internet_icon,
                R.drawable.phone_icon,R.drawable.repair_icon,R.drawable.shirt_icon,R.drawable.shoes_icon,R.drawable.travel_icon,R.drawable.utilities_icon,R.drawable.wifi_icon};
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_list_item, parent, false);

        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));

        ExpenseHolder expenseHodler = new ExpenseHolder(this.mContext, view);
        return expenseHodler;
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, int position) {

        Expense expense = this.expenseArrayList.get(position);

        holder.bindExpense(expense);
    }

    @Override
    public int getItemCount() {

        return this.expenseArrayList.size();
    }




    public class ExpenseHolder extends RecyclerView.ViewHolder{

        private final ConstraintLayout expenseLayout;
        private final LinearLayout expenseDummyLayout;
        private final ImageButton expenseIcon;
        private final EditText expenseName;
        private final EditText expenseBudget;
        private final ImageButton expenseDelete;

        private Context mContext;
        private Expense expense;

        public ExpenseHolder(Context mContext, View itemView){

            super(itemView);

            this.mContext = mContext;

            this.expenseLayout = itemView.findViewById(R.id.expenseItemLayout);
            this.expenseDummyLayout = itemView.findViewById(R.id.expenseDummyLayout);
            this.expenseIcon = itemView.findViewById(R.id.expenseIcon);
            this.expenseName = itemView.findViewById(R.id.expenseName);
            this.expenseBudget = itemView.findViewById(R.id.expenseBudget);
            this.expenseDelete = itemView.findViewById(R.id.expenseDelete);

        }

        public void bindExpense(Expense expense){

            this.expense = expense;

            this.expenseIcon.setImageDrawable(expense.getExpenseIcon());
            this.expenseName.setText(expense.getExpenseName());
            this.expenseBudget.setText(Float.toString(expense.getExpenseBudget()));
            this.expenseDelete.setImageResource(R.drawable.x_icon);

            int position = getAdapterPosition();

            this.expenseDelete.setOnClickListener((View v) -> deletePost(position, this));
            this.expenseDelete.setOnFocusChangeListener(new ClickOnFocus());

            this.expenseName.setOnFocusChangeListener((View v, boolean b) -> saveEditTextData(this, position, b));
            this.expenseName.setOnEditorActionListener(new DoneButtonPress(position, this));

            this.expenseBudget.setOnFocusChangeListener((View v, boolean b) -> saveEditTextData(this, position, b));
            this.expenseBudget.setOnEditorActionListener(new DoneButtonPress(position, this));

            this.expenseIcon.setOnClickListener((View v) -> changeIcon(this, position));
            this.expenseIcon.setOnFocusChangeListener(new ClickOnFocus());

            this.expenseLayout.setOnClickListener((View v) ->  hideKeyboardAndSave(position, this));


        }

        public void deletePost(int position, ExpenseHolder viewHolder){
            /**DELETES THE EXPENSE THAT WAS CLICKED**/

            hideKeyboardAndSave(position, viewHolder);

            expenseArrayList.remove(position);
            notifyDataSetChanged();

            updateBudgetLabel();

        }

        public void hideKeyboardAndSave(int pos, ExpenseHolder viewHolder){
            /**KILLS SOFT KEYBOARD**/
            viewHolder.expenseDummyLayout.requestFocus();

            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewHolder.expenseName.getWindowToken(), 0);

        }

        private void updateBudgetLabel() {
            /**UPDATES THE BUDGET IN THE ACTIVITY CONTROLLER AND KEEPS THIS BUDGET IN SYNC**/
            budget = ((CreatePlanExpenses)mContext).divideBudget();
            ((CreatePlanExpenses)mContext).changeBudgetTextView(budget);
        }

        private class ClickOnFocus implements View.OnFocusChangeListener {
            /**ELIMINATES THAT DOUBLE CLICK THING**/
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b){
                    view.performClick();
                }
            }
        }

        private void saveEditTextData(ExpenseHolder viewHolder, int position, boolean hasFocus) {

            if(!hasFocus){

                saveData(viewHolder, position);
            }
        }

        private void saveData(ExpenseHolder viewHolder, int position) {

            expenseArrayList.get(position).setExpenseName(viewHolder.expenseName.getText().toString());

            float budgetValue;

            if(viewHolder.expenseBudget.getText().toString().equals("")){
                budgetValue = 0f;
            }else{

                budgetValue = Float.parseFloat(viewHolder.expenseBudget.getText().toString());
            }

            if(budget - budgetValue < 0){
                budgetValue = budget;
                Toast.makeText(mContext, "You can't spend more than you already have", Toast.LENGTH_SHORT).show();

            }

            expenseArrayList.get(position).setExpenseBudget(budgetValue);

            updateBudgetLabel();

            //RUN on the ui thread cuz this updates ui boss
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    notifyDataSetChanged();
                }
            });



        }

        private class DoneButtonPress implements TextView.OnEditorActionListener {
            /**KILL KEYBOARD AND SAVE THE DATA WHEN ANYTHING IS PRESSED AND EDIT TEXT IS FOCUSED**/
            private int position;
            private ExpenseHolder viewHolder;

            public DoneButtonPress(int position, ExpenseHolder viewHolder){

                this.position = position;
                this.viewHolder = viewHolder;
            }

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || actionId == EditorInfo.IME_ACTION_DONE) {

                    hideKeyboardAndSave(position, viewHolder);
                    return true;
                }
                return false;
            }
        }

        private void changeIcon(ExpenseHolder viewHolder, int position) {

            hideKeyboardAndSave(position, viewHolder);

            final Dialog dialog = new Dialog(mContext);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.icon_alert_dialog);

            //Set Listeners
            for(int id: iconsListId){

                dialog.findViewById(id).setOnClickListener((View v) -> setDrawableResource(viewHolder, id, dialog, position));

            }

            dialog.show();
        }

        private void setDrawableResource(ExpenseHolder viewHolder, int id, Dialog dialog, int position) {

            for(int counter = 0; counter < iconsListId.length; counter++){
                if(id == iconsListId[counter]){
                    viewHolder.expenseIcon.setImageResource(iconListDrawable[counter]);
                    expenseArrayList.get(position).setExpenseIcon(ResourcesCompat.getDrawable(mContext.getResources(),iconListDrawable[counter], null));
                    notifyDataSetChanged();
                }
            }

            dialog.dismiss();

        }
    }
}
