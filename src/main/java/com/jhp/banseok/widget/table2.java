package com.jhp.banseok.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.tool.TimeTableTool;
import com.jhp.banseok.time.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Implementation of App Widget functionality.
 */
public class table2 extends AppWidgetProvider {

    static void updateAppWidget(Context mContext, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews mViews = new RemoteViews(mContext.getPackageName(), R.layout.widgettime2);

        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA);

        mViews.setTextViewText(R.id.mCalender, mFormat.format(mCalendar.getTime()));

        TimeTableTool.todayTimeTableData mData = TimeTableTool.getTodayTimeTable(mContext);
        mViews.setTextViewText(R.id.mTimeTable, mData.info);

        PendingIntent layoutPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, activity.class), 0);
        mViews.setOnClickPendingIntent(R.id.mWidgetLayout, layoutPendingIntent);

        Intent mIntent = new Intent(mContext, WidgetBroadCast.class);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(mContext, 0, mIntent, 0);
        mViews.setOnClickPendingIntent(R.id.mUpdateLayout, updatePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widgettime2);
        Intent intent = new Intent(context, table2.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.updater, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        super.onReceive(mContext, mIntent);

        String mAction = mIntent.getAction();
        if (mAction.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            updateAllTimeTableWidget(mContext);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAllTimeTableWidget(Context mContext) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, TimeTableWidget.class));

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(mContext, appWidgetManager, appWidgetId);
        }
    }
}