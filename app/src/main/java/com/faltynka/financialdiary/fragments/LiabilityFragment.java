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

public class LiabilityFragment extends Fragment {
    private List<SumInCategory> liability;

    public LiabilityFragment() {
        // Required empty public constructor
    }


    public static LiabilityFragment newInstance(List<SumInCategory> liability) {
        LiabilityFragment fragment = new LiabilityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("liability", (Serializable) liability);
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
        View view = inflater.inflate(R.layout.fragment_liability, container, false);

        liability = (List<SumInCategory>) getArguments().getSerializable("liability");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.liability_layout);

        FragmentHelper.createReportsTextViews(linearLayoutMain, liability, getContext());
        return view;
    }
}
