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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Date startDate, endDate;

    //Spinner
    private List<String> currencyList;

    //Ui
    private Button goToExpensesButton;
    private NiceSpinner currencySpinner;
    private TextView startPlanDate, endPlanDate;
    private ConstraintLayout createPlanLayout;
    private EditText budgetEditText;
    private RadioGroup updateBudgetRadioGroup;
    private RadioButton monthlyRadioButton, weeklyRadioButton, otherDateRadioButton;

    //Other
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        setupUi();

    }

    //TODO ADD COMMENTS
    private void setupUi() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupLayout();

        myCalendar = Calendar.getInstance();

        budgetEditText = findViewById(R.id.budgetEditText);

        setupRadioButtons();

        setupExpenseButton();

        setupDateText();

        setupSpinner();
    }

    private void setupExpenseButton() {
        goToExpensesButton = findViewById(R.id.goToExpenses);
        goToExpensesButton.setOnClickListener((View v) -> goToExpenses());
    }

    private void setupRadioButtons() {
        updateBudgetRadioGroup = findViewById(R.id.updateBudgetRadioGroup);
        updateBudgetRadioGroup.setOnCheckedChangeListener(new UpdateCheckChange());

        monthlyRadioButton = findViewById(R.id.monthlyRadioButton);
        weeklyRadioButton = findViewById(R.id.weeklyRadioButton);
        otherDateRadioButton = findViewById(R.id.otherDateRadioButton);

    }

    private void setupLayout() {

        createPlanLayout = findViewById(R.id.create_plan_layout);
        new HideKeyboardListener(createPlanLayout, getApplicationContext());
    }

    private void setupDateText() {

        startDate = new Date();
        endDate = new Date();

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
        currencySpinner = findViewById(R.id.currencySpinner);
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

                if(updateBudgetRadioGroup.getCheckedRadioButtonId() != -1){
                    /**If this is the first time that the edittext is select then do nothing**/
                    changeEndDateByRadioAmount(updateBudgetRadioGroup.getCheckedRadioButtonId());

                }

                /**Activate the radio buttons to select the end date**/
                monthlyRadioButton.setEnabled(true);
                weeklyRadioButton.setEnabled(true);
                otherDateRadioButton.setEnabled(true);

            }else{

                otherDateRadioButton.setChecked(true);
                endDate = myCalendar.getTime();
            }

            updateTextView(myCalendar.getTime(), dateTextView);
        }
    }

    private class UpdateCheckChange implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {

            changeEndDateByRadioAmount(id);
        }
    }

    private void changeEndDateByRadioAmount(int id) {

        Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.setTime(startDate);

        switch (id){

            case R.id.monthlyRadioButton:

                auxCalendar.add(Calendar.MONTH, 1);
                endDate = auxCalendar.getTime();
                updateTextView(auxCalendar.getTime(), endPlanDate);

                endPlanDate.setEnabled(true);

                break;
            case R.id.weeklyRadioButton:

                auxCalendar.add(Calendar.WEEK_OF_MONTH, 1);
                endDate = auxCalendar.getTime();
                updateTextView(auxCalendar.getTime(), endPlanDate);

                endPlanDate.setEnabled(true);

                break;

            case R.id.otherDateRadioButton:

                auxCalendar.add(Calendar.DAY_OF_MONTH, 1);
                endDate = auxCalendar.getTime();
                updateTextView(auxCalendar.getTime(), endPlanDate);

                endPlanDate.setEnabled(true);

                break;
        }
    }


}
