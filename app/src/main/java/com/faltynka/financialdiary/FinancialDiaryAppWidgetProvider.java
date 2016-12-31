package com.faltynka.financialdiary;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.faltynka.financialdiary.NewRecord;
import com.faltynka.financialdiary.R;
import com.faltynka.financialdiary.sqlite.helper.DatabaseHelper;

import org.joda.time.DateTime;

import java.util.List;

public class FinancialDiaryAppWidgetProvider extends AppWidgetProvider {
    private DatabaseHelper mydb;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        mydb = DatabaseHelper.getInstance(context);

        DateTime fromDate = new DateTime();
        fromDate = fromDate.withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime toDate = new DateTime(fromDate.getYear(), fromDate.getMonthOfYear(), fromDate.getDayOfMonth(), 0, 0);
        toDate = toDate.plusMonths(1);
        List<SumInCategory> income = mydb.countSumInIncomeCategoriesFromDateRange(fromDate, toDate);
        List<SumInCategory> expense = mydb.countSumInExpenseCategoriesFromDateRange(fromDate, toDate);
        List<SumInCategory> asset = mydb.countSumInCategoriesOfTypeFromDateRange("Asset", fromDate, toDate);
        Integer togetherIncome = countSumForSumInCategory(income);
        Integer togetherExpense = countSumForSumInCategory(expense);
        Integer togetherAsset = countSumForSumInCategory(asset);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch NewRecord activity
            Intent intent = new Intent(context, NewRecord.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.financial_diary_appwidget);
            views.setOnClickPendingIntent(R.id.btn_new_record_widget, pendingIntent);
            views.setTextViewText(R.id.asset_amount_widget, togetherAsset.toString());
            views.setTextViewText(R.id.expense_amount_widget, togetherExpense.toString());
            views.setTextViewText(R.id.income_amount_widget, togetherIncome.toString());

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private Integer countSumForSumInCategory(List<SumInCategory> sumInCategories) {
        Integer together = 0;
        for (SumInCategory sumInCategory : sumInCategories) {
            together += sumInCategory.getSum();
        }
        return together;
    }
}
