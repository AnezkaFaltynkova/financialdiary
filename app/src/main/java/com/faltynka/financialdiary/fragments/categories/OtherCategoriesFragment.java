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


public class OtherCategoriesFragment extends Fragment {
    private List<Category> other;

    public OtherCategoriesFragment() {
        // Required empty public constructor
    }

    public static OtherCategoriesFragment newInstance(List<Category> other) {
        OtherCategoriesFragment fragment = new OtherCategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories_other, container, false);

        other = (List<Category>) getArguments().getSerializable("other");

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.other_categories_layout);

        CategoriesFragmentHelper.createReportsTextViews(linearLayoutMain, other, getContext(), view);
        return view;
    }
}
