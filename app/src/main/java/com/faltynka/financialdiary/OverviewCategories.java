package com.faltynka.financialdiary;

import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.faltynka.financialdiary.fragments.PagerAdapter;
import com.faltynka.financialdiary.fragments.categories.CategoriesPagerAdapter;
import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Category;

import java.util.List;

public class OverviewCategories extends AppCompatActivity {
    static OverviewCategories overviewCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overviewCategories = this;
        setContentView(R.layout.activity_overview_categories);

        List<Category> income = (List<Category>) getIntent().getSerializableExtra("income");
        List<Category> expense = (List<Category>) getIntent().getSerializableExtra("expense");
        List<Category> asset = (List<Category>) getIntent().getSerializableExtra("asset");
        List<Category> liability = (List<Category>) getIntent().getSerializableExtra("liability");
        List<Category> other = (List<Category>) getIntent().getSerializableExtra("other");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_categories);
        tabLayout.addTab(tabLayout.newTab().setText("Asset"));
        tabLayout.addTab(tabLayout.newTab().setText("Expense"));
        tabLayout.addTab(tabLayout.newTab().setText("Income"));
        tabLayout.addTab(tabLayout.newTab().setText("Liability"));
        tabLayout.addTab(tabLayout.newTab().setText("Other"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_categories);
        final CategoriesPagerAdapter adapter = new CategoriesPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), income, expense, asset, liability, other);
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
    }

    public static OverviewCategories getInstance() {
        return overviewCategories;
    }
}
