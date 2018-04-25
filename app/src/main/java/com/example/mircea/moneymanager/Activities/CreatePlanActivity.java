package com.example.mircea.moneymanager.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mircea.moneymanager.R;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePlanActivity extends AppCompatActivity {

    //Spinner
    private List<String> currencyList;

    //Ui
    private Button goToExpensesButton;
    private NiceSpinner currencySpinner;
    private TextView startPlanDate;
    private TextView endPlanDate;

    //Other
    private Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        setupUi();

    }

    private void setupUi() {

        myCalendar = Calendar.getInstance();

        goToExpensesButton = findViewById(R.id.goToExpenses);
        goToExpensesButton.setOnClickListener((View v) -> goToExpenses());

        initDateText();

        currencySpinner = findViewById(R.id.currencySpinner);
        setupSpinner();
    }

    private void initDateText() {
        /**Initializes the date textviews with the start and end of the plan dates**/
        startPlanDate = findViewById(R.id.startPlanDate);
        updateTextView(myCalendar.getTime(), startPlanDate);
        startPlanDate.setOnClickListener((View v) -> showDateDialog(startPlanDate));

        endPlanDate = findViewById(R.id.endPlanDate);
        updateTextView(myCalendar.getTime(), endPlanDate);
        endPlanDate.setOnClickListener((View v) -> showDateDialog(endPlanDate));
    }

    private void showDateDialog(TextView textView) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerListener(textView),
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        datePickerDialog.show();

    }

    private void setupSpinner() {

        currencyList = Arrays.asList(getResources().getStringArray(R.array.currency_array));
        currencySpinner.attachDataSource(currencyList);

    }

    public void goToExpenses(){

        startActivity(new Intent(getApplicationContext(), CreatePlanExpenses.class));
    }

    private void updateTextView(Date date, TextView dateTextView) {
        //TODO make this a lil nicer
        String myFormat = "E/dd/MM/yy"; //In which you need put here
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
            updateTextView(myCalendar.getTime(), dateTextView);
        }


    }
}
