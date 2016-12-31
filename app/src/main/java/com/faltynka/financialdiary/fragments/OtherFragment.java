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


public class OtherFragment extends Fragment {
    private List<SumInCategory> other;

    public OtherFragment() {
        // Required empty public constructor
    }

    public static OtherFragment newInstance(List<SumInCategory> other) {
        OtherFragment fragment = new OtherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("other", (Serializable) other);
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
        View view = inflater.inflate(R.layout.fragment_other, container, false);

        other = (List<SumInCategory>) getArguments().getSerializable("other");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.other_layout);

        FragmentHelper.createReportsTextViews(linearLayoutMain, other, getContext());
        return view;
    }
}
