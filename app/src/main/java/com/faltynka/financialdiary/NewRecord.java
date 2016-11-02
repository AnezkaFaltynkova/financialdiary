package com.faltynka.financialdiary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Record;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewRecord extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper mydb;
    private Spinner typeFromSpinner;
    private Spinner typeToSpinner;
    private Spinner categoryFromSpinner;
    private Spinner categoryToSpinner;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText noteEditText;
    private Button btnNewRecord;
    private String typeFromString;
    private String typeToString;
    private String categoryFromString;
    private String categoryToString;
    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("New Record");


        setContentView(R.layout.activity_new_record);

        mydb = new DatabaseHelper(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        typeFromSpinner = (Spinner) findViewById(R.id.type_from_spinner);
        typeFromSpinner.setOnItemSelectedListener(new NewRecord.ItemSelectedTypeFromListener());
        List<String> fromTypeEntries = mydb.getTypesByPossibilityFrom(1);
        ArrayAdapter<String> adapterFromType = new ArrayAdapter<String>(NewRecord.this,
                android.R.layout.simple_spinner_item, fromTypeEntries);
        typeFromSpinner.setAdapter(adapterFromType);

        typeToSpinner = (Spinner) findViewById(R.id.type_to_spinner);
        typeToSpinner.setOnItemSelectedListener(new NewRecord.ItemSelectedTypeToListener());
        List<String> toTypeEntries = mydb.getTypesByPossibilityTo(1);
        ArrayAdapter<String> adapterToType = new ArrayAdapter<String>(NewRecord.this,
                android.R.layout.simple_spinner_item, toTypeEntries);
        typeToSpinner.setAdapter(adapterToType);

        categoryFromSpinner = (Spinner) findViewById(R.id.category_from_spinner);
        categoryFromSpinner.setOnItemSelectedListener(new NewRecord.ItemSelectedCategoryFromListener());

        categoryToSpinner = (Spinner) findViewById(R.id.category_to_spinner);
        categoryToSpinner.setOnItemSelectedListener(new NewRecord.ItemSelectedCategoryToListener());

        dateEditText = (EditText) findViewById(R.id.dateEditText);
        dateEditText.setInputType(InputType.TYPE_NULL);

        setDateTimeField();

        amountEditText = (EditText) findViewById(R.id.amountEditText);

        noteEditText = (EditText) findViewById(R.id.noteEditText);

        btnNewRecord = (Button) findViewById(R.id.create_new_record_button);

        btnNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amountEditText.getText().toString();
                String dateString = dateEditText.getText().toString();
                if (categoryFromString.isEmpty()){
                    Toast.makeText(NewRecord.this, "Choose From Category", Toast.LENGTH_LONG).show();
                } else if (categoryToString.isEmpty()){
                    Toast.makeText(NewRecord.this, "Choose To Category", Toast.LENGTH_LONG).show();
                } else if (amountString.isEmpty()) {
                    Toast.makeText(NewRecord.this, "Fill the amount", Toast.LENGTH_LONG).show();
                } else if (dateString.isEmpty()) {
                    Toast.makeText(NewRecord.this, "Pick a date", Toast.LENGTH_LONG).show();
                } else {
                    Record record = new Record();
                    record.setFromId(mydb.findCategoryIdByNameAndTypeName(categoryFromString, typeFromString));
                    record.setToId(mydb.findCategoryIdByNameAndTypeName(categoryToString, typeToString));
                    record.setAmount(Integer.parseInt(amountString));
                    record.setDate(getTimestampFromStringDate(dateString));
                    record.setDeleted(0);
                    Date date = new Date();
                    record.setEdited(date.getTime());
                    record.setNote(noteEditText.getText().toString());
                    mydb.createRecord(record);
                    Toast.makeText(NewRecord.this, "New record was created date:"+ record.getDate()+" edited: " + record.getEdited(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NewRecord.this, NewRecord.class));
                    finish();
                }
            }
        });
    }

    private void setDateTimeField() {
        dateEditText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == dateEditText) {
            datePickerDialog.show();
        }
    }

    public class ItemSelectedTypeFromListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            typeFromString = String.valueOf(typeFromSpinner.getSelectedItem());
            int typeFromId = mydb.findTypeIdByName(typeFromString);
            List<String> fromCategoryEntries = mydb.getCategoriesByTypeId(typeFromId);
            ArrayAdapter<String> adapterFromCategory = new ArrayAdapter<String>(NewRecord.this,
                    android.R.layout.simple_spinner_item, fromCategoryEntries);
            categoryFromSpinner.setAdapter(adapterFromCategory);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class ItemSelectedTypeToListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            typeToString = String.valueOf(typeToSpinner.getSelectedItem());
            int typeToId = mydb.findTypeIdByName(typeToString);
            List<String> toCategoryEntries = mydb.getCategoriesByTypeId(typeToId);
            ArrayAdapter<String> adapterToCategory = new ArrayAdapter<String>(NewRecord.this,
                    android.R.layout.simple_spinner_item, toCategoryEntries);
            categoryToSpinner.setAdapter(adapterToCategory);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class ItemSelectedCategoryFromListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            categoryFromString = String.valueOf(categoryFromSpinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public class ItemSelectedCategoryToListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            categoryToString = String.valueOf(categoryToSpinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    public long getTimestampFromStringDate(String dateString) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result.getTime();
    }

}
