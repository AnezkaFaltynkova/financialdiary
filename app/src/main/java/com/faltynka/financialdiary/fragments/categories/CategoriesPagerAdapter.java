package com.faltynka.financialdiary.fragments.categories;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.faltynka.financialdiary.SumInCategory;
import com.faltynka.financialdiary.fragments.AssetFragment;
import com.faltynka.financialdiary.fragments.ExpenseFragment;
import com.faltynka.financialdiary.fragments.IncomeFragment;
import com.faltynka.financialdiary.fragments.LiabilityFragment;
import com.faltynka.financialdiary.fragments.OtherFragment;
import com.faltynka.financialdiary.sqlite.model.Category;

import java.util.List;

public class CategoriesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<Category> income;
    List<Category> expense;
    List<Category> asset;
    List<Category> liability;
    List<Category> other;

    public CategoriesPagerAdapter(FragmentManager fm, int NumOfTabs, List<Category> income, List<Category> expense, List<Category> asset, List<Category> liability, List<Category> other) {
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
                AssetCategoriesFragment assetCategoriesFragment = AssetCategoriesFragment.newInstance(asset);
                return assetCategoriesFragment;
            case 1:
                ExpenseCategoriesFragment expenseCategoriesFragment = ExpenseCategoriesFragment.newInstance(expense);
                return expenseCategoriesFragment;
            case 2:
                IncomeCategoriesFragment incomeCategoriesFragment = IncomeCategoriesFragment.newInstance(income);
                return incomeCategoriesFragment;
            case 3:
                LiabilityCategoriesFragment liabilityCategoriesFragment = LiabilityCategoriesFragment.newInstance(liability);
                return liabilityCategoriesFragment;
            case 4:
                OtherCategoriesFragment otherCategoriesFragment = OtherCategoriesFragment.newInstance(other);
                return otherCategoriesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
