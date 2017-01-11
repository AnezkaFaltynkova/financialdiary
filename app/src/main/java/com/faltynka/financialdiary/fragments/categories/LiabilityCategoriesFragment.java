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

public class LiabilityCategoriesFragment extends Fragment {
    private List<Category> liability;

    public LiabilityCategoriesFragment() {
        // Required empty public constructor
    }


    public static LiabilityCategoriesFragment newInstance(List<Category> liability) {
        LiabilityCategoriesFragment fragment = new LiabilityCategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories_liability, container, false);

        liability = (List<Category>) getArguments().getSerializable("liability");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.liability_categories_layout);

        CategoriesFragmentHelper.createReportsTextViews(linearLayoutMain, liability, getContext(), view);
        return view;
    }
}
