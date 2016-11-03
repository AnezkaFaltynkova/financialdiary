package com.faltynka.financialdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectionOverview extends AppCompatActivity {

    private DatabaseHelper mydb;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Button btnShow;
    private Integer year;
    private Integer month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Overview");

        setContentView(R.layout.activity_selection_overview);

        mydb = new DatabaseHelper(this);

        yearSpinner = (Spinner) findViewById(R.id.year_overview_spinner);
        yearSpinner.setOnItemSelectedListener(new SelectionOverview.ItemSelectedYearListener());
        List<Integer> yearEntries = mydb.getAllDistinctYearsOfRecords();
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(SelectionOverview.this,
                android.R.layout.simple_spinner_item, yearEntries);
        yearSpinner.setAdapter(adapterYear);

        monthSpinner = (Spinner) findViewById(R.id.month_overview_spinner);
        monthSpinner.setOnItemSelectedListener(new SelectionOverview.ItemSelectedMonthListener());

        btnShow = (Button) findViewById(R.id.show_overview_button);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionOverview.this, Overview.class);
                DateTime fromDate = new DateTime(year, month , 1, 0, 0);
                DateTime toDate = new DateTime(year, month, 1, 0, 0);
                toDate.plusMonths(1);
                List<Record> records = mydb.getRecordsBetweenDates(fromDate, toDate);
                intent.putExtra("records", (Serializable) records);
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
