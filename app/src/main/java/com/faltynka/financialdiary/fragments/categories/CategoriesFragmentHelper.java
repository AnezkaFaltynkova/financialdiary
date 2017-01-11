package com.faltynka.financialdiary.fragments.categories;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.faltynka.financialdiary.EditCategoryActivity;
import com.faltynka.financialdiary.EditRecordActivity;
import com.faltynka.financialdiary.Overview;
import com.faltynka.financialdiary.OverviewCategories;
import com.faltynka.financialdiary.SumInCategory;
import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;
import com.faltynka.financialdiary.sqlite.model.Category;
import com.faltynka.financialdiary.sqlite.model.Record;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class CategoriesFragmentHelper {
    private static DatabaseHelper mydb;

    public static void createReportsTextViews(RelativeLayout linearLayoutMain, List<Category> categoriesList, final Context context, View view) {
        mydb = DatabaseHelper.getInstance(context);

        ScrollView scrollView = new ScrollView(context);
        RelativeLayout.LayoutParams scrollViewParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scrollViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        scrollView.setLayoutParams(scrollViewParam);
        scrollView.setPadding(40, 0, 40, 20);
        linearLayoutMain.addView(scrollView);

        LinearLayout layoutInScrollView = new LinearLayout(context);
        layoutInScrollView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutInScrollParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutInScrollView.setLayoutParams(layoutInScrollParams);
        scrollView.addView(layoutInScrollView);

        for(final Category category : categoriesList){
            Button btn = new Button(context);
            btn.setId(View.generateViewId());
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setText(category.getName());
            btn.setGravity(Gravity.LEFT);
            btn.setPadding(0, 20, 0, 20);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(btnParams);
            btn.setMinimumHeight(0);
            btn.setMinHeight(0);
            layoutInScrollView.addView(btn);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditCategoryActivity.class);
                    intent.putExtra("category", category);
                    context.startActivity(intent);
                }
            };
            btn.setOnClickListener(onClickListener);
            View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this category?\nAll records related to this category will be ALSO deleted.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mydb.deleteAllRecordsForCategoryId(category.getId());
                                    mydb.deleteCategory(category.getId());
                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    OverviewCategories.getInstance().finish();
                                    Intent intent = new Intent(context, OverviewCategories.class);
                                    List<Category> asset = mydb.selectAllCategoriesNotDeletedWithType("Asset");
                                    List<Category> income = mydb.selectAllCategoriesNotDeletedWithType("Income");
                                    List<Category> expense = mydb.selectAllCategoriesNotDeletedWithType("Expense");
                                    List<Category> liability = mydb.selectAllCategoriesNotDeletedWithType("Liability");
                                    List<Category> other = mydb.selectAllCategoriesNotDeletedWithType("Other");
                                    intent.putExtra("asset", (Serializable) asset);
                                    intent.putExtra("income", (Serializable) income);
                                    intent.putExtra("expense", (Serializable) expense);
                                    intent.putExtra("liability", (Serializable) liability);
                                    intent.putExtra("other", (Serializable) other);
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Delete category");
                    d.show();
                    return true;
                }
            };
            btn.setOnLongClickListener(onLongClickListener);
        }
    }
}
