package com.faltynka.financialdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.SumInCategory;

import java.io.Serializable;
import java.util.List;


public class ExpenseFragment extends Fragment {
    private List<SumInCategory> expense;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    public static ExpenseFragment newInstance(List<SumInCategory> expense) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        expense = (List<SumInCategory>) getArguments().getSerializable("expense");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.expense_layout);

        FragmentHelper.createReportsTextViews(linearLayoutMain, expense, getContext());

        return view;
    }

}
