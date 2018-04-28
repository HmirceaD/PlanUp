package com.example.mircea.moneymanager.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.Listeners.HideKeyboardListener;
import com.example.mircea.moneymanager.R;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePlanActivity extends AppCompatActivity {

    //Logic
    private Date startDate;
    private Date endDate;

    //Spinner
    private List<String> currencyList;

    //Ui
    private Button goToExpensesButton;
    private NiceSpinner currencySpinner;
    private TextView startPlanDate;
    private TextView endPlanDate;
    private ConstraintLayout createPlanLayout;
    private EditText budgetEditText;

    //Other
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        setupUi();

    }

    private void setupUi() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        createPlanLayout = findViewById(R.id.create_plan_layout);
        new HideKeyboardListener(createPlanLayout, getApplicationContext());

        myCalendar = Calendar.getInstance();

        budgetEditText = findViewById(R.id.budgetEditText);

        goToExpensesButton = findViewById(R.id.goToExpenses);
        goToExpensesButton.setOnClickListener((View v) -> goToExpenses());

        startDate = new Date();
        endDate = new Date();

        initDateText();

        currencySpinner = findViewById(R.id.currencySpinner);
        setupSpinner();
    }

    private void initDateText() {
        //Initializes the date textviews /
        startPlanDate = findViewById(R.id.startPlanDate);
        startPlanDate.setText("");

        startPlanDate.setOnClickListener((View v) -> showDateDialog(startPlanDate));

        endPlanDate = findViewById(R.id.endPlanDate);
        endPlanDate.setText("");

        endPlanDate.setOnClickListener((View v) -> showDateDialog(endPlanDate));
    }

    private void showDateDialog(TextView textView) {
        //display the calendar dialog to select the dates


        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerListener(textView),
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        if(textView.getId() == R.id.endPlanDate){

            if(startDate != null){

                Date auxStartDate = startDate;
                Calendar auxCalendar = Calendar.getInstance();
                auxCalendar.setTime(auxStartDate);
                auxCalendar.add(Calendar.DATE, 1);
                auxStartDate = auxCalendar.getTime();

                datePickerDialog.getDatePicker().setMinDate(auxStartDate.getTime());

            }
        }

        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        datePickerDialog.show();

    }

    private void setupSpinner() {
        //setup currency spinner
        currencyList = Arrays.asList(getResources().getStringArray(R.array.currency_array));
        currencySpinner.attachDataSource(currencyList);

    }

    public void goToExpenses(){
        /**Go to expenses activity**/

        if(startPlanDate.getText().toString().equals("") ||
                endPlanDate.getText().toString().equals("") ||
                budgetEditText.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(), "Please select the dates and budget", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(getApplicationContext(), CreatePlanExpenses.class));

        }
    }

    private void updateTextView(Date date, TextView dateTextView) {
        //convert the calendar so it can be displayed in the date textviews
        String myFormat = "E/dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        dateTextView.setText(sdf.format(date));
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener{

        private TextView dateTextView;

        public DatePickerListener(TextView textView){
            this.dateTextView = textView;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);

            if(dateTextView.getId() == R.id.startPlanDate){

                startDate = myCalendar.getTime();

                if(startDate.getTime() > endDate.getTime() && endPlanDate.isEnabled()){
                    /**If the selected startdate is greater then the end date reset the end date**/

                    myCalendar.add(Calendar.DATE, 1);
                    endDate = myCalendar.getTime();
                    updateTextView(myCalendar.getTime(), endPlanDate);
                }

                endPlanDate.setEnabled(true);

            }else{

                endDate = myCalendar.getTime();
            }

            updateTextView(myCalendar.getTime(), dateTextView);
        }


    }


}
