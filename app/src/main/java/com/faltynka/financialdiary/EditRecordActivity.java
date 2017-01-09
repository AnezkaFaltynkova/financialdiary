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

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditRecordActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseHelper mydb;
    private Spinner typeFromSpinner;
    private Spinner typeToSpinner;
    private Spinner categoryFromSpinner;
    private Spinner categoryToSpinner;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText noteEditText;
    private Button btnEditRecord;
    private String typeFromString;
    private String typeToString;
    private String categoryFromString;
    private String categoryToString;
    private DatePickerDialog datePickerDialog;
    private Record record;
    private int year;
    private int month;
    private int day;
    private String type;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        record = (Record) getIntent().getSerializableExtra("record");
        year = getIntent().getIntExtra("year", 1987);
        month = getIntent().getIntExtra("month", 9);
        day = getIntent().getIntExtra("day", 25);
        type = getIntent().getStringExtra("type");

        mydb = DatabaseHelper.getInstance(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        typeFromSpinner = (Spinner) findViewById(R.id.type_from_edit_spinner);
        typeFromSpinner.setOnItemSelectedListener(new EditRecordActivity.ItemSelectedTypeFromListener());
        List<String> fromTypeEntries = mydb.getTypesByPossibilityFrom(1);
        ArrayAdapter<String> adapterFromType = new ArrayAdapter<String>(EditRecordActivity.this,
                android.R.layout.simple_spinner_dropdown_item, fromTypeEntries);
        typeFromSpinner.setAdapter(adapterFromType);
        typeFromSpinner.setSelection(fromTypeEntries.indexOf(mydb.findTypeNameForCategoryWithId(record.getFromId())));

        typeToSpinner = (Spinner) findViewById(R.id.type_to_edit_spinner);
        typeToSpinner.setOnItemSelectedListener(new EditRecordActivity.ItemSelectedTypeToListener());
        List<String> toTypeEntries = mydb.getTypesByPossibilityTo(1);
        ArrayAdapter<String> adapterToType = new ArrayAdapter<String>(EditRecordActivity.this,
                android.R.layout.simple_spinner_dropdown_item, toTypeEntries);
        typeToSpinner.setAdapter(adapterToType);
        typeToSpinner.setSelection(toTypeEntries.indexOf(mydb.findTypeNameForCategoryWithId(record.getToId())));

        categoryFromSpinner = (Spinner) findViewById(R.id.category_from_edit_spinner);
        categoryFromSpinner.setOnItemSelectedListener(new EditRecordActivity.ItemSelectedCategoryFromListener());

        categoryToSpinner = (Spinner) findViewById(R.id.category_to_edit_spinner);
        categoryToSpinner.setOnItemSelectedListener(new EditRecordActivity.ItemSelectedCategoryToListener());

        dateEditText = (EditText) findViewById(R.id.dateEditEditText);
        dateEditText.setInputType(InputType.TYPE_NULL);
        Date date = new Date(record.getDate());
        dateEditText.setText(dateFormatter.format(date.getTime()));

        setDateTimeField();

        amountEditText = (EditText) findViewById(R.id.amountEditEditText);
        amountEditText.setText("" + record.getAmount());

        noteEditText = (EditText) findViewById(R.id.noteEditEditText);
        noteEditText.setText(record.getNote());

        btnEditRecord = (Button) findViewById(R.id.edit_record_button);

        btnEditRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amountEditText.getText().toString();
                String dateString = dateEditText.getText().toString();
                if (categoryFromString.isEmpty()){
                    Toast.makeText(EditRecordActivity.this, "Choose From Category", Toast.LENGTH_LONG).show();
                } else if (categoryToString.isEmpty()){
                    Toast.makeText(EditRecordActivity.this, "Choose To Category", Toast.LENGTH_LONG).show();
                } else if (amountString.isEmpty()) {
                    Toast.makeText(EditRecordActivity.this, "Fill the amount", Toast.LENGTH_LONG).show();
                } else if (dateString.isEmpty()) {
                    Toast.makeText(EditRecordActivity.this, "Pick a date", Toast.LENGTH_LONG).show();
                } else {
                    Record editedRecord = new Record();
                    editedRecord.setId(record.getId());
                    editedRecord.setFromId(mydb.findCategoryIdByNameAndTypeName(categoryFromString, typeFromString));
                    editedRecord.setToId(mydb.findCategoryIdByNameAndTypeName(categoryToString, typeToString));
                    editedRecord.setAmount(Integer.parseInt(amountString));
                    editedRecord.setDate(getTimestampFromStringDate(dateString));
                    editedRecord.setDeleted(0);
                    Date date = new Date();
                    editedRecord.setEdited(date.getTime());
                    editedRecord.setNote(noteEditText.getText().toString());
                    mydb.editRecord(editedRecord);
                    Toast.makeText(EditRecordActivity.this, "Record was edited", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditRecordActivity.this, Overview.class);
                    DateTime fromDate = new DateTime(year, month , day, 0, 0);
                    DateTime toDate = new DateTime(year, month, day, 0, 0);
                    if (type.equals("year")){
                        toDate = toDate.plusYears(1);
                    } else if (type.equals("month")){
                        toDate = toDate.plusMonths(1);
                    } else {
                        toDate = toDate.plusDays(1);
                    }
                    List<Record> records = mydb.getRecordsBetweenDates(fromDate, toDate);
                    intent.putExtra("records", (Serializable) records);
                    intent.putExtra("year", fromDate.getYear());
                    intent.putExtra("month", fromDate.getMonthOfYear());
                    intent.putExtra("day", fromDate.getDayOfMonth());
                    intent.putExtra("type", type);
                    startActivity(intent);
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
            ArrayAdapter<String> adapterFromCategory = new ArrayAdapter<String>(EditRecordActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, fromCategoryEntries);
            categoryFromSpinner.setAdapter(adapterFromCategory);
            categoryFromSpinner.setSelection(fromCategoryEntries.indexOf(mydb.findCategoryNameById(record.getFromId())));
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
            ArrayAdapter<String> adapterToCategory = new ArrayAdapter<String>(EditRecordActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, toCategoryEntries);
            categoryToSpinner.setAdapter(adapterToCategory);
            categoryToSpinner.setSelection(toCategoryEntries.indexOf(mydb.findCategoryNameById(record.getToId())));
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
