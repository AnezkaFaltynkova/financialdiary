package com.faltynka.financialdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.SumInCategory;

import java.io.Serializable;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetFragment extends Fragment {
    private List<SumInCategory> asset;

    public AssetFragment() {
        // Required empty public constructor
    }

    public static AssetFragment newInstance(List<SumInCategory> asset) {
        AssetFragment fragment = new AssetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("asset", (Serializable) asset);
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
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        asset = (List<SumInCategory>) getArguments().getSerializable("asset");

        LinearLayout linearLayoutMain = (LinearLayout) view.findViewById(R.id.asset_layout);

        FragmentHelper.createReportsTextViews(linearLayoutMain, asset, getContext());
        return view;
    }

}
