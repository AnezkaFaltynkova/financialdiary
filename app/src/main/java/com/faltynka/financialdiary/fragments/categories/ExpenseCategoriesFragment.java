package com.faltynka.financialdiary.fragments.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.SumInCategory;
import com.faltynka.financialdiary.fragments.FragmentHelper;
import com.faltynka.financialdiary.sqlite.model.Category;

import java.io.Serializable;
import java.util.List;


public class ExpenseCategoriesFragment extends Fragment {
    private List<Category> expense;

    public ExpenseCategoriesFragment() {
        // Required empty public constructor
    }

    public static ExpenseCategoriesFragment newInstance(List<Category> expense) {
        ExpenseCategoriesFragment fragment = new ExpenseCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("expense", (Serializable) expense);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories_expense, container, false);

        expense = (List<Category>) getArguments().getSerializable("expense");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.expense_categories_layout);

        CategoriesFragmentHelper.createReportsTextViews(linearLayoutMain, expense, getContext(), view);

        return view;
    }

}
