package com.faltynka.financialdiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.SumInCategory;

import java.io.Serializable;
import java.util.List;


public class IncomeFragment extends Fragment {
    private List<SumInCategory> income;


    public IncomeFragment() {
        // Required empty public constructor
    }

    public static IncomeFragment newInstance(List<SumInCategory> income) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("income", (Serializable) income);
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
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        income = (List<SumInCategory>) getArguments().getSerializable("income");

        LinearLayout linearLayoutMain = (LinearLayout) view.findViewById(R.id.income_layout);

        FragmentHelper.createReportsTextViews(linearLayoutMain, income, getContext());

        return view;
    }
}
