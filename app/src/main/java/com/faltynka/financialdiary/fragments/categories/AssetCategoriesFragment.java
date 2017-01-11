package com.faltynka.financialdiary.fragments.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.faltynka.financialdiary.EditCategoryActivity;
import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.SumInCategory;
import com.faltynka.financialdiary.fragments.FragmentHelper;
import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Category;

import java.io.Serializable;
import java.util.List;

public class AssetCategoriesFragment extends Fragment {
    private List<Category> asset;
    private DatabaseHelper mydb;

    public AssetCategoriesFragment() {
        // Required empty public constructor
    }

    public static AssetCategoriesFragment newInstance(List<Category> asset) {
        AssetCategoriesFragment fragment = new AssetCategoriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_categories_asset, container, false);

        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.asset_categories_layout);

        asset = (List<Category>) getArguments().getSerializable("asset");

        CategoriesFragmentHelper.createReportsTextViews(linearLayoutMain, asset, getContext(), view);
//
//        Context context = getContext();
//        mydb = DatabaseHelper.getInstance(context);
//
//        ScrollView scrollView = new ScrollView(context);
//        RelativeLayout.LayoutParams scrollViewParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        scrollViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        scrollView.setLayoutParams(scrollViewParam);
//        scrollView.setPadding(0, 0, 0, 100);
//        linearLayoutMain.addView(scrollView);
//
//        LinearLayout layoutInScrollView = new LinearLayout(context);
//        layoutInScrollView.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams layoutInScrollParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        layoutInScrollView.setLayoutParams(layoutInScrollParams);
//        scrollView.addView(layoutInScrollView);
//
//        for(final Category category : asset) {
//            Button btn = new Button(context);
//            btn.setId(View.generateViewId());
//            final int id_ = btn.getId();
//            btn.setBackgroundColor(Color.TRANSPARENT);
//            btn.setText(category.getName());
//            btn.setGravity(Gravity.LEFT);
//            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            btn.setLayoutParams(btnParams);
//            Button btn1 = ((Button) view.findViewById(id_));
//            View.OnClickListener onClickListener = new View.OnClickListener() {
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), EditCategoryActivity.class);
//                    intent.putExtra("category", category);
//                    getContext().startActivity(intent);
//                }
//            };
//            btn1.setOnClickListener(onClickListener);
//        }
        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        RelativeLayout linearLayoutMain = (RelativeLayout) view.findViewById(R.id.asset_categories_layout);
//
//        Button btn = new Button(getActivity());
//        btn.setId(View.generateViewId());
//        final int id_ = btn.getId();
//        btn.setBackgroundColor(Color.TRANSPARENT);
////        btn.setText(category.getName());
//        btn.setGravity(Gravity.LEFT);
//        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        btn.setLayoutParams(btnParams);
//        Button btn1 = ((Button) view.findViewById(id_));
//
//    }
}