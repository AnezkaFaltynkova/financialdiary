package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class SelectionReportsActivity extends AppCompatActivity {

    private DatabaseHelper mydb;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Button btnShow;
    private Button btnThisMonth;
    private Button btnThisYear;
    private Button btnEver;
    private Integer year;
    private Integer month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Reports");

        setContentView(R.layout.activity_selection_reports);

        mydb = new DatabaseHelper(this);

        yearSpinner = (Spinner) findViewById(R.id.year_reports_spinner);
        yearSpinner.setOnItemSelectedListener(new SelectionReportsActivity.ItemSelectedYearListener());
        List<Integer> yearEntries = mydb.getAllDistinctYearsOfRecords();
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(SelectionReportsActivity.this,
                android.R.layout.simple_spinner_item, yearEntries);
        yearSpinner.setAdapter(adapterYear);

        monthSpinner = (Spinner) findViewById(R.id.month_reports_spinner);
        monthSpinner.setOnItemSelectedListener(new SelectionReportsActivity.ItemSelectedMonthListener());

        btnShow = (Button) findViewById(R.id.show_reports_button);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionReportsActivity.this, ReportsActivity.class);
                DateTime fromDate = new DateTime(year, month , 1, 0, 0);
                DateTime toDate = new DateTime(year, month, 1, 0, 0);
                toDate = toDate.plusMonths(1);
                List<SumInCategory> income = mydb.countSumInIncomeCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> expense = mydb.countSumInExpenseCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> asset = mydb.countSumInCategoriesOfTypeFromDateRange("Asset", fromDate, toDate);
                List<SumInCategory> liability = mydb.countSumInCategoriesOfTypeFromDateRange("Liability", fromDate, toDate);
                List<SumInCategory> other = mydb.countSumInCategoriesOfTypeFromDateRange("Other", fromDate, toDate);
                intent.putExtra("income", (Serializable) income);
                intent.putExtra("expense", (Serializable) expense);
                intent.putExtra("asset", (Serializable) asset);
                intent.putExtra("liability", (Serializable) liability);
                intent.putExtra("other", (Serializable) other);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });

        btnThisMonth = (Button) findViewById(R.id.this_month_reports_button);

        btnThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionReportsActivity.this, ReportsActivity.class);
                DateTime fromDate = new DateTime();
                fromDate = fromDate.withDayOfMonth(1).withTimeAtStartOfDay();
                DateTime toDate = new DateTime(fromDate.getYear(), fromDate.getMonthOfYear(), fromDate.getDayOfMonth(), 0, 0);
                toDate = toDate.plusMonths(1);
                List<SumInCategory> income = mydb.countSumInIncomeCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> expense = mydb.countSumInExpenseCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> asset = mydb.countSumInCategoriesOfTypeFromDateRange("Asset", fromDate, toDate);
                List<SumInCategory> liability = mydb.countSumInCategoriesOfTypeFromDateRange("Liability", fromDate, toDate);
                List<SumInCategory> other = mydb.countSumInCategoriesOfTypeFromDateRange("Other", fromDate, toDate);
                intent.putExtra("income", (Serializable) income);
                intent.putExtra("expense", (Serializable) expense);
                intent.putExtra("asset", (Serializable) asset);
                intent.putExtra("liability", (Serializable) liability);
                intent.putExtra("other", (Serializable) other);
                intent.putExtra("year", fromDate.getYear());
                intent.putExtra("month", fromDate.getMonthOfYear());
                intent.putExtra("day", fromDate.getDayOfMonth());
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });

        btnThisYear = (Button) findViewById(R.id.this_year_reports_button);

        btnThisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionReportsActivity.this, ReportsActivity.class);
                DateTime fromDate = new DateTime();
                fromDate = fromDate.withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay();
                DateTime toDate = new DateTime(fromDate.getYear(), fromDate.getMonthOfYear(), fromDate.getDayOfMonth(), 0, 0);
                toDate = toDate.plusYears(1);
                List<SumInCategory> income = mydb.countSumInIncomeCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> expense = mydb.countSumInExpenseCategoriesFromDateRange(fromDate, toDate);
                List<SumInCategory> asset = mydb.countSumInCategoriesOfTypeFromDateRange("Asset", fromDate, toDate);
                List<SumInCategory> liability = mydb.countSumInCategoriesOfTypeFromDateRange("Liability", fromDate, toDate);
                List<SumInCategory> other = mydb.countSumInCategoriesOfTypeFromDateRange("Other", fromDate, toDate);
                intent.putExtra("income", (Serializable) income);
                intent.putExtra("expense", (Serializable) expense);
                intent.putExtra("asset", (Serializable) asset);
                intent.putExtra("liability", (Serializable) liability);
                intent.putExtra("other", (Serializable) other);
                intent.putExtra("year", fromDate.getYear());
                intent.putExtra("month", fromDate.getMonthOfYear());
                intent.putExtra("day", fromDate.getDayOfMonth());
                intent.putExtra("type", "year");
                startActivity(intent);
            }
        });

        btnEver = (Button) findViewById(R.id.ever_reports_button);

        btnEver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionReportsActivity.this, ReportsActivity.class);
                List<SumInCategory> income = mydb.countSumInIncomeCategoriesEver();
                List<SumInCategory> expense = mydb.countSumInExpenseCategoriesEver();
                List<SumInCategory> asset = mydb.countSumInCategoriesOfTypeEver("Asset");
                List<SumInCategory> liability = mydb.countSumInCategoriesOfTypeEver("Liability");
                List<SumInCategory> other = mydb.countSumInCategoriesOfTypeEver("Other");
                intent.putExtra("income", (Serializable) income);
                intent.putExtra("expense", (Serializable) expense);
                intent.putExtra("asset", (Serializable) asset);
                intent.putExtra("liability", (Serializable) liability);
                intent.putExtra("other", (Serializable) other);
                intent.putExtra("type", "ever");
                startActivity(intent);
            }
        });
    }

    public class ItemSelectedYearListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            year = Integer.parseInt(String.valueOf(yearSpinner.getSelectedItem()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class ItemSelectedMonthListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            month = pos+1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }
}
