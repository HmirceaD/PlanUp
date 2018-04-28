package com.example.mircea.moneymanager.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Activities.CreatePlanExpenses;
import com.example.mircea.moneymanager.Listeners.HideKeyboardListener;
import com.example.mircea.moneymanager.R;
import com.example.mircea.moneymanager.Raw.Expense;

import java.util.ArrayList;
import java.util.jar.Attributes;


public class ExpenseListAdapter extends ArrayAdapter<Expense> {

    private static int[] iconsListId;
    private static int[] iconListDrawable;

    private static int[] colorListId;
    private static int[] colorListRes;

    private ArrayList<Expense> expenseArrayList;
    private Context mContext;

    private LayoutInflater lI;

    public ExpenseListAdapter(ArrayList<Expense> fA, Context c){
        super(c, R.layout.expense_list_item, fA);

        this.expenseArrayList = fA;
        this.mContext = c;
        this.lI = LayoutInflater.from(mContext);
        iconsListId = new int[]{R.id.car_icon,R.id.cart_icon,R.id.dress_icon,R.id.food_icon,R.id.game_icon,R.id.gift_icon,R.id.house_icon,R.id.internet_icon,
                R.id.phone_icon,R.id.repair_icon,R.id.shirt_icon,R.id.shoes_icon,R.id.travel_icon,R.id.utilities_icon,R.id.wifi_icon};

        iconListDrawable = new int[]{R.drawable.car_icon,R.drawable.cart_icon,R.drawable.dress_icon,R.drawable.food_icon,R.drawable.game_icon,R.drawable.gift_icon,R.drawable.house_icon,R.drawable.internet_icon,
                R.drawable.phone_icon,R.drawable.repair_icon,R.drawable.shirt_icon,R.drawable.shoes_icon,R.drawable.travel_icon,R.drawable.utilities_icon,R.drawable.wifi_icon};

        colorListId = new int[]{R.id.darkBlueButton,R.id.lightBlueButton,R.id.mauveButton,R.id.redButton,
                R.id.orangeButton,R.id.lavenderButton,R.id.purpleButton,R.id.aquaButton,
                R.id.greenButton,R.id.mustardButton,R.id.darkGrayButton,R.id.pinkButton,R.id.lightGrayButton};

        colorListRes = new int[]{R.color.darkBlue,R.color.lightBlue,R.color.mauve,R.color.red,
                R.color.orange,R.color.lavender,R.color.purple,R.color.aqua,
                R.color.green,R.color.mustard,R.color.darkGray,R.color.pink,R.color.lightGray};


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /**INflate or display images on the scree**/

        ViewHolder viewHolder;
        //globalPosition = position;

        if(convertView == null){
            convertView = lI.inflate(R.layout.expense_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.expenseLayout = convertView.findViewById(R.id.expenseItemLayout);

            viewHolder.expenseIcon = convertView.findViewById(R.id.expenseIcon);

            viewHolder.expenseName = convertView.findViewById(R.id.expenseName);

            viewHolder.expenseBudget = convertView.findViewById(R.id.expenseBudget);

            viewHolder.expenseColor = convertView.findViewById(R.id.expenseColor);
            viewHolder.expenseDelete = convertView.findViewById(R.id.expenseDelete);
            viewHolder.expenseDelete.setOnClickListener((View v) -> deletePost(position, viewHolder));

            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.expenseIcon.setOnClickListener(null);
            viewHolder.expenseColor.setOnClickListener(null);
            viewHolder.expenseDelete.setOnClickListener(null);

        }

        Expense expense = expenseArrayList.get(position);

        viewHolder.expenseIcon.setImageDrawable(expense.getExpenseIcon());

        viewHolder.expenseName.setText(expense.getExpenseName());

        viewHolder.expenseBudget.setText(Float.toString(expense.getExpenseBudget()));

        viewHolder.expenseColor.setBackgroundColor(expense.getExpenseColor());

        viewHolder.expenseDelete.setImageResource(R.drawable.x_icon);
        viewHolder.expenseDelete.setOnClickListener((View v) -> deletePost(position, viewHolder));

        viewHolder.expenseIcon.setOnClickListener((View v) -> changeIcon(viewHolder, position));
        viewHolder.expenseColor.setOnClickListener((View v) -> changeColor(viewHolder, position));

        viewHolder.expenseLayout.setOnClickListener((View v) ->  hideKeyboardAndSave(position, viewHolder.expenseName, viewHolder.expenseBudget));

        return convertView;
    }

    /**Create the color popup**/
    private void changeColor(ViewHolder viewHolder, int position) {

        hideKeyboardAndSave(position, viewHolder.expenseName, viewHolder.expenseBudget);

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.colors_alert_dialog);

        for(int id: colorListId){

            dialog.findViewById(id).setOnClickListener((View v) -> setColorResource(viewHolder, id, dialog, position));

        }

        dialog.show();
    }

    private void setColorResource(ViewHolder viewHolder, int id, Dialog dialog, int position) {

        for(int counter = 0; counter < colorListId.length; counter++){
            if(id == colorListId[counter]){
                viewHolder.expenseColor.setImageResource(colorListRes[counter]);
                expenseArrayList.get(position).setExpenseColor(iconListDrawable[counter]);
                notifyDataSetChanged();
            }
        }

        dialog.dismiss();
    }

    /**Create the icon popup**/
    private void changeIcon(ViewHolder viewHolder, int position) {

        hideKeyboardAndSave(position, viewHolder.expenseName, viewHolder.expenseBudget);

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

    private void setDrawableResource(ViewHolder viewHolder, int id, Dialog dialog, int position) {

        for(int counter = 0; counter < iconsListId.length; counter++){
            if(id == iconsListId[counter]){
                viewHolder.expenseIcon.setImageResource(iconListDrawable[counter]);
                expenseArrayList.get(position).setExpenseIcon(ResourcesCompat.getDrawable(mContext.getResources(),iconListDrawable[counter], null));
                notifyDataSetChanged();
            }
        }

        dialog.dismiss();

    }

    //TODO refactor this ugly shit


    public void hideKeyboardAndSave(int pos, EditText nameEditText, EditText budgetEditText){

        expenseArrayList.get(pos).setExpenseName(nameEditText.getText().toString());

        float budgetValue;

        if(budgetEditText.getText().toString().equals("")){
            budgetValue = 0f;
        }else{

            budgetValue = Float.parseFloat(budgetEditText.getText().toString());
        }

        expenseArrayList.get(pos).setExpenseBudget(budgetValue);

        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
    }

    public void deletePost(int position, ViewHolder viewHolder){
        /*Delete the post int the list*/

        hideKeyboardAndSave(position, viewHolder.expenseName, viewHolder.expenseBudget);

        expenseArrayList.remove(position);
        notifyDataSetChanged();
        notifyDataSetInvalidated();

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

        //Layout
        ConstraintLayout expenseLayout;
        ImageButton expenseIcon;
        EditText expenseName;
        EditText expenseBudget;
        ImageButton expenseColor;
        ImageButton expenseDelete;

    }
}
