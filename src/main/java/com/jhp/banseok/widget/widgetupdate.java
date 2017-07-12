package com.jhp.banseok.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class widgetupdate extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        String ACTION = mIntent.getAction();

        BapWidget.updateAllBapWidget(mContext);

        if (Intent.ACTION_BOOT_COMPLETED.equals(ACTION)) {
            // 24시간마다 앱 위젯 업데이트하기
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH) + 1, 1, 0);
        }
    }
}

