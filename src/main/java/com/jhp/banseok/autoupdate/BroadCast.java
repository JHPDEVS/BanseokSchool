package com.jhp.banseok.autoupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jhp.banseok.bap.tool.BapTool;
import com.jhp.banseok.bap.tool.Preference;

import java.util.Calendar;

public class BroadCast extends BroadcastReceiver {
    Preference mPref;

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        String ACTION = mIntent.getAction();
        mPref = new Preference(mContext);

        boolean autoUpdate = mPref.getBoolean("autoBapUpdate", false);
        if (!autoUpdate)
            return;

        /**
         * 1 : 자동
         * 0 : 매주 토요일
         * -1 : 매주 일요일
         */
        int updateLife = Integer.parseInt(mPref.getString("updateLife", "0"));

        Calendar mCalendar = Calendar.getInstance();
        int DayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        /**
         * 부팅후 실행
         */
        if (Intent.ACTION_BOOT_COMPLETED.equals(ACTION)) {
            if ((DayOfWeek == Calendar.SUNDAY && updateLife == -1)
                    || (DayOfWeek == Calendar.SATURDAY && updateLife == 0)) {
                if (haveToUpdate(mContext, mCalendar)) {
                    mContext.startService(new Intent(mContext, updateService.class));
                }
            }

            updateAlarm updateAlarm = new updateAlarm(mContext);
            switch (updateLife) {
                case 1:
                    updateAlarm.autoUpdate();
                    break;
                case 0:
                    updateAlarm.SaturdayUpdate();
                    break;
                case -1:
                    updateAlarm.SundayUpdate();
                    break;
            }

        } else if (BapTool.ACTION_UPDATE.equals(ACTION)) {
            if (haveToUpdate(mContext, mCalendar))
                mContext.startService(new Intent(mContext, updateService.class));
        }
    }

    private boolean haveToUpdate(Context mContext, Calendar mCalendar) {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        BapTool.restoreBapDateClass mData = BapTool.restoreBapData(mContext, year, month, day);

        return mData.isBlankDay;
    }
}
