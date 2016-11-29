package com.faltynka.financialdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Overview extends AppCompatActivity {

    private DatabaseHelper mydb;
    private int year;
    private int month;
    private int day;
    private String type;
    private Button btnLeft;
    private Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Overview");

        setContentView(R.layout.activity_overview);

        mydb = new DatabaseHelper(this);

        year = getIntent().getIntExtra("year", 1987);
        month = getIntent().getIntExtra("month", 9);
        day = getIntent().getIntExtra("day", 25);
        type = getIntent().getStringExtra("type");
        TextView header = (TextView) findViewById(R.id.dateOverviewTextView);
        header.setText(createHeader(year, month, day, type));

        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_overview);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams scrollViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(scrollViewParam);
        layout.addView(scrollView);

        LinearLayout layoutInScrollView = new LinearLayout(this);
        layoutInScrollView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutInScrollParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutInScrollView.setLayoutParams(layoutInScrollParams);
        scrollView.addView(layoutInScrollView);

        List<Record> records = (List<Record>) getIntent().getSerializableExtra("records");
        for (int i = 1; i <= records.size(); i++) {
            LinearLayout layoutWithTwoButtons = new LinearLayout(this);
            layoutWithTwoButtons.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutWithTwoButtons.setLayoutParams(LLParams);
            layoutWithTwoButtons.setPadding(50, 0, 50, 0);
            layoutInScrollView.addView(layoutWithTwoButtons);

            final Record record = records.get(i-1);
            Button btn = new Button(this);
            btn.setId(View.generateViewId());
            final int id_ = btn.getId();
            btn.setBackgroundColor(Color.TRANSPARENT);
            String fromString = mydb.findCategoryNameById(record.getFromId());
            String toString = mydb.findCategoryNameById(record.getToId());
            btn.setText("From: " + fromString.toUpperCase() + "\nTo: " + toString.toUpperCase());
            btn.setGravity(Gravity.LEFT);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            btn.setLayoutParams(btnParams);
            layoutWithTwoButtons.addView(btn);
            Button btn1 = ((Button) findViewById(id_));
            View.OnClickListener onClickListener = new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Overview.this, EditRecordActivity.class);
                    intent.putExtra("record", record);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
            };
            btn1.setOnClickListener(onClickListener);
            View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Overview.this);
                    builder.setMessage("Are you sure you want to delete this record?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mydb.deleteRecord(record.getId());
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Overview.this, Overview.class);
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
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Delete record");
                    d.show();
                    return true;
                }
            };
            btn1.setOnLongClickListener(onLongClickListener);

            Button btnAmount = new Button(this);
            btnAmount.setId(View.generateViewId());
            final int id_amount = btnAmount.getId();
            btnAmount.setBackgroundColor(Color.TRANSPARENT);
            btnAmount.setText("" + record.getAmount());
            btnAmount.setGravity(Gravity.RIGHT);
            LinearLayout.LayoutParams btnAmountParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
            btn.setLayoutParams(btnAmountParams);
            layoutWithTwoButtons.addView(btnAmount);
            Button btn1Amount = ((Button) findViewById(id_amount));
            btn1Amount.setOnClickListener(onClickListener);
            btn1Amount.setOnLongClickListener(onLongClickListener);
        }

        btnLeft = (Button) findViewById(R.id.left_arrow_overview_button);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Overview.this, Overview.class);
                DateTime fromDate = new DateTime(year, month , day, 0, 0);
                DateTime toDate = new DateTime(year, month, day, 0, 0);
                if (type.equals("year")){
                    fromDate = fromDate.minusYears(1);
                } else if (type.equals("month")){
                    fromDate = fromDate.minusMonths(1);
                } else {
                    fromDate = fromDate.minusDays(1);
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
        });

        btnRight = (Button) findViewById(R.id.right_arrow_overview_button);
        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Overview.this, Overview.class);
                DateTime fromDate = new DateTime(year, month , day, 0, 0);
                DateTime toDate = new DateTime(year, month, day, 0, 0);
                if (type.equals("year")){
                    fromDate = fromDate.plusYears(1);
                    toDate = toDate.plusYears(2);
                } else if (type.equals("month")){
                    fromDate = fromDate.plusMonths(1);
                    toDate = toDate.plusMonths(2);
                } else {
                    fromDate = fromDate.plusDays(1);
                    toDate = toDate.plusDays(2);
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
        });
    }

    private String createHeader(int year, int month, int day, String type){
        String header = "" + year;
        Date date = new Date(year, month-1, day);
        String monthString =new SimpleDateFormat("MMMM", Locale.US).format(date);
        if (type.equals("month") || type.equals("day")) {
            header = header + ", " + monthString;
        }
        if (type.equals("day")) {
            header = header + ", " + day;
        }
        return header;
    }
}
