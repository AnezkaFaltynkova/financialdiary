package com.faltynka.financialdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;

import java.util.List;

public class SelectionOverview extends AppCompatActivity {

    private DatabaseHelper mydb;
    private Spinner yearSpinner;
    private Integer year;

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
    }

    public class ItemSelectedYearListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            year = Integer.parseInt(String.valueOf(yearSpinner.getSelectedItem()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }
}
