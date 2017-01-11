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


public class IncomeCategoriesFragment extends Fragment {
    private List<Category> income;


    public IncomeCategoriesFragment() {
        // Required empty public constructor
    }

    public static IncomeCategoriesFragment newInstance(List<Category> income) {
        IncomeCategoriesFragment fragment = new IncomeCategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories_income, container, false);
        income = (List<Category>) getArguments().getSerializable("income");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.income_categories_layout);

        CategoriesFragmentHelper.createReportsTextViews(linearLayoutMain, income, getContext(), view);

        return view;
    }
}
