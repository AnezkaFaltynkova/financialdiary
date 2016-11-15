package com.faltynka.financialdiary.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TextView;

import com.faltynka.financialdiary.SumInCategory;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<SumInCategory> income;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, List<SumInCategory> income) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.income = income;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AssetFragment assetFragment = new AssetFragment();
                return assetFragment;
            case 1:
                ExpenseFragment expenseFragment = new ExpenseFragment();
                return expenseFragment;
            case 2:
                IncomeFragment incomeFragment = IncomeFragment.newInstance(income);
                return incomeFragment;
            case 3:
                LiabilityFragment liabilityFragment = new LiabilityFragment();
                return liabilityFragment;
            case 4:
                OtherFragment otherFragment = new OtherFragment();
                return otherFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
