package com.faltynka.financialdiary;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faltynka.financialdiary.fragments.IncomeFragment;
import com.faltynka.financialdiary.fragments.PagerAdapter;
import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportsActivity extends AppCompatActivity {
    private int year;
    private int month;
    private String type;
    private Button btnLeft;
    private Button btnRight;
    private DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mydb = new DatabaseHelper(this);

        year = getIntent().getIntExtra("year", 1987);
        month = getIntent().getIntExtra("month", 9);
        type = getIntent().getStringExtra("type");

        List<SumInCategory> income = (List<SumInCategory>) getIntent().getSerializableExtra("income");
        List<SumInCategory> expense = (List<SumInCategory>) getIntent().getSerializableExtra("expense");
        List<SumInCategory> asset = (List<SumInCategory>) getIntent().getSerializableExtra("asset");
        List<SumInCategory> liability = (List<SumInCategory>) getIntent().getSerializableExtra("liability");
        List<SumInCategory> other = (List<SumInCategory>) getIntent().getSerializableExtra("other");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Asset"));
        tabLayout.addTab(tabLayout.newTab().setText("Expense"));
        tabLayout.addTab(tabLayout.newTab().setText("Income"));
        tabLayout.addTab(tabLayout.newTab().setText("Liability"));
        tabLayout.addTab(tabLayout.newTab().setText("Other"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), income, expense, asset, liability, other);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (!"ever".equals(type)){
            TextView header = (TextView) findViewById(R.id.dateReportsTextView);
            header.setVisibility(View.VISIBLE);
            header.setText(createHeader(year, month, type));

            btnLeft = (Button) findViewById(R.id.left_arrow_reports_button);
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ReportsActivity.this, ReportsActivity.class);
                    DateTime fromDate = new DateTime(year, month , 1, 0, 0);
                    DateTime toDate = new DateTime(year, month, 1, 0, 0);
                    if ("year".equals(type)){
                        fromDate = fromDate.minusYears(1);
                    } else {
                        fromDate = fromDate.minusMonths(1);
                    }
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
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
            });

            btnRight = (Button) findViewById(R.id.right_arrow_reports_button);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(ReportsActivity.this, ReportsActivity.class);
                    DateTime fromDate = new DateTime(year, month , 1, 0, 0);
                    DateTime toDate = new DateTime(year, month, 1, 0, 0);
                    if ("year".equals(type)){
                        fromDate = fromDate.plusYears(1);
                        toDate = toDate.plusYears(2);
                    } else {
                        fromDate = fromDate.plusMonths(1);
                        toDate = toDate.plusMonths(2);
                    }
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
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    private String createHeader(int year, int month, String type){
        String header = "" + year;
        Date date = new Date(year, month-1, 1);
        String monthString =new SimpleDateFormat("MMMM", Locale.US).format(date);
        if (type.equals("month")) {
            header = header + ", " + monthString;
        }
        return header;
    }
}
