package com.jhp.banseok.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.BapActivity;
import com.jhp.banseok.bap.tool.BapTool;
import com.jhp.banseok.bap.tool.Preference;
import com.jhp.banseok.bap.tool.ProcessTask;
import com.jhp.banseok.bap.tool.Tools;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class BapWidget extends AppWidgetProvider {

      static void updateLargeAppWidget(Context mContext, AppWidgetManager appWidgetManager, int appWidgetId, boolean ifNotUpdate) {
        RemoteViews mViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_bap_two2);

        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        BapTool.restoreBapDateClass mData = BapTool.restoreBapData(mContext, year, month, day);

        mViews.setTextViewText(R.id.mCalender, mData.Calender);
        mViews.setTextViewText(R.id.mDayOfTheWeek, mData.DayOfTheWeek);

        if (mData.isBlankDay) {
            // 데이터 없음
            if (Tools.isOnline(mContext)) {
                // Only Wifi && Not Wifi
                if (new Preference(mContext).getBoolean("updateWiFi", true) && !Tools.isWifi(mContext)) {
                    mViews.setTextViewText(R.id.mLunch, mContext.getString(R.string.widget_no_data));
                    mViews.setTextViewText(R.id.mDinner, mContext.getString(R.string.widget_no_data));
                }
                // 급식 데이터 받아옴
                else if (ifNotUpdate) {
                    BapDownloadTask mProcessTask = new BapDownloadTask(mContext);
                    mProcessTask.execute(year, month, day);
                }
            } else {
                mViews.setTextViewText(R.id.mLunch, mContext.getString(R.string.widget_no_data));
                mViews.setTextViewText(R.id.mDinner, mContext.getString(R.string.widget_no_data));
            }
        } else {
            // 데이터 있음
            String mLunch = mData.Lunch;
            String mDinner = mData.Dinner;
            if (BapTool.mStringCheck(mLunch)) {
                mLunch = mContext.getString(R.string.no_data_lunch);
            } else {
                mLunch = BapTool.replaceString(mLunch);
            }

            if (BapTool.mStringCheck(mDinner)) {
                mDinner = mContext.getString(R.string.no_data_dinner);
            } else {
                mDinner = BapTool.replaceString(mDinner);
            }

            mViews.setTextViewText(R.id.mLunch, mLunch);
            mViews.setTextViewText(R.id.mDinner, mDinner);
        }
          Intent mIntent2 = new Intent(mContext, widgetupdate.class);
          PendingIntent updatePendingIntent2 = PendingIntent.getBroadcast(mContext, 0, mIntent2, 0);

        PendingIntent layoutPendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, BapActivity.class), 0);
        mViews.setOnClickPendingIntent(R.id.mWidgetLayout, layoutPendingIntent);

        Intent mIntent = new Intent(mContext, WidgetBroadCast.class);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(mContext, 0, mIntent, 0);
        mViews.setOnClickPendingIntent(R.id.mUpdateLayout, updatePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mViews);
    }

    @Override
    public void onUpdate(Context mContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_bap_two2);
            int minResizeWidth = appWidgetManager.getAppWidgetInfo(appWidgetId).minResizeWidth;
            int minResizeHeight = appWidgetManager.getAppWidgetInfo(appWidgetId).minResizeHeight;
            Intent intent = new Intent(mContext, BapWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.updater, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            if (minResizeWidth >= 343 && minResizeHeight >= 343) {
                updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
            } else {
                updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
            }
        }
    }

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        super.onReceive(mContext, mIntent);

        String mAction = mIntent.getAction();
        if (mAction.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            updateAllBapWidget(mContext);
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

    public static class BapDownloadTask extends ProcessTask {
        Context mContext;

        public BapDownloadTask(Context mContext) {
            super(mContext);
            this.mContext = mContext;
        }

        @Override
        public void onPreDownload() {
        }

        @Override
        public void onUpdate(int progress) {
        }

        @Override
        public void onFinish(long result) {
            Intent mIntent = new Intent(mContext, WidgetBroadCast.class);
            mContext.sendBroadcast(mIntent);
        }
    }

    public static void updateAllBapWidget(Context mContext) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BapWidget.class));

        for (int appWidgetId : appWidgetIds) {

            Bundle mBundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int maxWidth = mBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int maxHeight = mBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

            if (maxWidth >= 343 && maxHeight >= 343) {
                updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
            } else {
                updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context mContext, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(mContext, appWidgetManager, appWidgetId, newOptions);

//        int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
//        int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if (maxWidth >= 343 && maxHeight >= 343) {
            updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
        } else {
            updateLargeAppWidget(mContext, appWidgetManager, appWidgetId, false);
        }
    }
}

