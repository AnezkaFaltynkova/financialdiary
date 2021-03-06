package com.faltynka.financialdiary.fragments;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.faltynka.financialdiary.SumInCategory;

import java.util.List;

public class FragmentHelper {

    public static void createReportsTextViews(RelativeLayout linearLayoutMain, List<SumInCategory> sumInCategoryList, Context context) {
        ScrollView scrollView = new ScrollView(context);
        RelativeLayout.LayoutParams scrollViewParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scrollViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        scrollView.setLayoutParams(scrollViewParam);
        scrollView.setPadding(0, 0, 0, 100);
        linearLayoutMain.addView(scrollView);

        LinearLayout layoutInScrollView = new LinearLayout(context);
        layoutInScrollView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutInScrollParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutInScrollView.setLayoutParams(layoutInScrollParams);
        scrollView.addView(layoutInScrollView);

        for(SumInCategory sumInCategory : sumInCategoryList){
            if (sumInCategory.getSum() == 0) {
                continue;
            }
            LinearLayout layoutWithTwoTextViews = new LinearLayout(context);
            layoutWithTwoTextViews.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutWithTwoTextViews.setLayoutParams(LLParams);
            layoutWithTwoTextViews.setPadding(50, 0, 50, 0);
            layoutInScrollView.addView(layoutWithTwoTextViews);

            TextView textViewCategory = new TextView(context);
            textViewCategory.setText(sumInCategory.getCategory());
            LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textViewCategory.setLayoutParams(txtViewParams);
            textViewCategory.setTextColor(Color.BLACK);
            textViewCategory.setTextSize(18);
            layoutWithTwoTextViews.addView(textViewCategory);

            TextView textViewSum = new TextView(context);
            textViewSum.setText("" + sumInCategory.getSum());
            LinearLayout.LayoutParams txtViewParamsSum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            textViewSum.setLayoutParams(txtViewParamsSum);
            textViewSum.setGravity(Gravity.RIGHT);
            textViewSum.setTextColor(Color.BLACK);
            textViewSum.setTextSize(18);
            layoutWithTwoTextViews.addView(textViewSum);
        }

        LinearLayout layoutWithTogether = new LinearLayout(context);
        layoutWithTogether.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams LLParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutWithTogether.setLayoutParams(LLParams);
        layoutWithTogether.setPadding(50, 0, 50, 0);
        linearLayoutMain.addView(layoutWithTogether);

        TextView textViewTogetherLeft = new TextView(context);
        textViewTogetherLeft.setText("Together");
        LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textViewTogetherLeft.setLayoutParams(txtViewParams);
        textViewTogetherLeft.setTextColor(Color.BLACK);
        textViewTogetherLeft.setTextSize(18);
        layoutWithTogether.addView(textViewTogetherLeft);

        int together = 0;
        for(SumInCategory sumInCategory : sumInCategoryList) {
            together = together + sumInCategory.getSum();
        }

        TextView textViewSumTogether = new TextView(context);
        textViewSumTogether.setText("" + together);
        LinearLayout.LayoutParams txtViewParamsSum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        textViewSumTogether.setLayoutParams(txtViewParamsSum);
        textViewSumTogether.setGravity(Gravity.RIGHT);
        textViewSumTogether.setTextColor(Color.BLACK);
        textViewSumTogether.setTextSize(18);
        layoutWithTogether.addView(textViewSumTogether);
    }
}
