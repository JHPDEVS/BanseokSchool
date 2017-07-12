package com.jhp.banseok.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class WidgetBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        String ACTION = mIntent.getAction();

        BapWidget.updateAllBapWidget(mContext);

        if (Intent.ACTION_BOOT_COMPLETED.equals(ACTION)) {
            // 24시간마다 앱 위젯 업데이트하기
            Calendar mCalendar = Calendar.getInstance();
            AlarmManager mAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            Intent mIntentDate = new Intent(mContext, WidgetBroadCast.class);
            PendingIntent mPending = PendingIntent.getBroadcast(mContext, 0, mIntentDate, 0);
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH) + 1, 1, 0);
            mAlarm.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 24 * 60 * 60 * 1000, mPending);
        }
    }
}

