package com.faltynka.financialdiary.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.faltynka.financialdiary.SumInCategory;

import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<SumInCategory> income;
    List<SumInCategory> expense;
    List<SumInCategory> asset;
    List<SumInCategory> liability;
    List<SumInCategory> other;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, List<SumInCategory> income, List<SumInCategory> expense, List<SumInCategory> asset, List<SumInCategory> liability, List<SumInCategory> other) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.income = income;
        this.expense = expense;
        this.asset = asset;
        this.liability = liability;
        this.other = other;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AssetFragment assetFragment = AssetFragment.newInstance(asset);
                return assetFragment;
            case 1:
                ExpenseFragment expenseFragment = ExpenseFragment.newInstance(expense);
                return expenseFragment;
            case 2:
                IncomeFragment incomeFragment = IncomeFragment.newInstance(income);
                return incomeFragment;
            case 3:
                LiabilityFragment liabilityFragment = LiabilityFragment.newInstance(liability);
                return liabilityFragment;
            case 4:
                OtherFragment otherFragment = OtherFragment.newInstance(other);
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
