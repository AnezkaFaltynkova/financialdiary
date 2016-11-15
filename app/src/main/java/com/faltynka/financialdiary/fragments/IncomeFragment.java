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
        for(SumInCategory sumInCategory : income){
            LinearLayout layoutWithTwoTextViews = new LinearLayout(getContext());
            layoutWithTwoTextViews.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutWithTwoTextViews.setLayoutParams(LLParams);
            layoutWithTwoTextViews.setPadding(50, 0, 50, 0);
            linearLayoutMain.addView(layoutWithTwoTextViews);

            TextView textViewCategory = new TextView(getContext());
            textViewCategory.setText(sumInCategory.getCategory());
            LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textViewCategory.setLayoutParams(txtViewParams);
            textViewCategory.setTextColor(Color.BLACK);
            textViewCategory.setTextSize(18);
            layoutWithTwoTextViews.addView(textViewCategory);

            TextView textViewSum = new TextView(getContext());
            textViewSum.setText("" + sumInCategory.getSum());
            LinearLayout.LayoutParams txtViewParamsSum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            textViewSum.setLayoutParams(txtViewParamsSum);
            textViewSum.setGravity(Gravity.RIGHT);
            textViewSum.setTextColor(Color.BLACK);
            textViewSum.setTextSize(18);
            layoutWithTwoTextViews.addView(textViewSum);
        }

        LinearLayout layoutWithTogether = new LinearLayout(getContext());
        layoutWithTogether.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        layoutWithTogether.setLayoutParams(LLParams);
        layoutWithTogether.setPadding(50, 0, 50, 0);
        layoutWithTogether.setGravity(Gravity.BOTTOM);
        linearLayoutMain.addView(layoutWithTogether);

        TextView textViewTogetherLeft = new TextView(getContext());
        textViewTogetherLeft.setText("Together");
        LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textViewTogetherLeft.setLayoutParams(txtViewParams);
        textViewTogetherLeft.setTextColor(Color.BLACK);
        textViewTogetherLeft.setTextSize(18);
        layoutWithTogether.addView(textViewTogetherLeft);

        int together = 0;
        for(SumInCategory sumInCategory : income) {
            together = together + sumInCategory.getSum();
        }

        TextView textViewSumTogether = new TextView(getContext());
        textViewSumTogether.setText("" + together);
        LinearLayout.LayoutParams txtViewParamsSum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        textViewSumTogether.setLayoutParams(txtViewParamsSum);
        textViewSumTogether.setGravity(Gravity.RIGHT);
        textViewSumTogether.setTextColor(Color.BLACK);
        textViewSumTogether.setTextSize(18);
        layoutWithTogether.addView(textViewSumTogether);
        return view;
    }
}
