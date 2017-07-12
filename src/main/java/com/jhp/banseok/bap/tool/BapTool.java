package com.jhp.banseok.bap.tool;

import android.content.Context;

import com.jhp.banseok.R;
import com.jhp.banseok.toast.MealLibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BapTool {
    public static final String BAP_PREFERENCE_NAME = "BapData";
    public static final int TYPE_LUNCH = 1;
    public static final int TYPE_DINNER = 2;

    public static final String ACTION_UPDATE = "ACTION_BAP_UPDATE";

    public static String getBapStringFormat(int year, int month, int day, int type) {
        /**
         * Format : year-month-day-TYPE
         */
        // Calendar의 month는 1이 부족하므로 1을 더해줌
        month += 1;
        return year + "-" + month + "-" + day + "-" + type;
    }

    public static boolean canPostStar(Context mContext, int type) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        Preference mPref = new Preference(mContext, "RateStarInfo");
        String lunchKey = "LunchStar_" + year + month + day;
        String dinnerKey = "DinnerStar_" + year + month + day;

        return ((type == 0) && mPref.getBoolean(lunchKey, true)) || ((type == 1) && mPref.getBoolean(dinnerKey, true));
    }

    public static void todayPostStar(Context mContext, int type) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        Preference mPref = new Preference(mContext, "RateStarInfo");
        String lunchKey = "LunchStar_" + year + month + day;
        String dinnerKey = "DinnerStar_" + year + month + day;

        if (type == 0) {
            mPref.putBoolean(lunchKey, false);
        } else {
            mPref.putBoolean(dinnerKey, false);
        }
    }

    /**
     * Pref Name Format : 2015-02-17-TYPE_index
     * ex) 2015-02-17-1_3
     */
    public static void saveBapData(Context mContext, String[] Calender, String[] Lunch, String[] Dinner) {
        Preference mPref = new Preference(mContext, BAP_PREFERENCE_NAME);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd(E)",
                Locale.KOREA);

        for (int index = 0; index < Calender.length; index++) {
            try {
                Calendar mDate = Calendar.getInstance();
                mDate.setTime(mFormat.parse(Calender[index]));

                int year = mDate.get(Calendar.YEAR);
                int month = mDate.get(Calendar.MONTH);
                int day = mDate.get(Calendar.DAY_OF_MONTH);

                String mPrefLunchName = getBapStringFormat(year, month, day, TYPE_LUNCH);
                String mPrefDinnerName = getBapStringFormat(year, month, day, TYPE_DINNER);

                String mLunch = Lunch[index];
                String mDinner = Dinner[index];

                if (!MealLibrary.isMealCheck(mLunch)) mLunch = "";
                if (!MealLibrary.isMealCheck(mDinner)) mDinner = "";

                mPref.putString(mPrefLunchName, mLunch);
                mPref.putString(mPrefDinnerName, mDinner);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Format : 2015-2-11-2
     */
    public static restoreBapDateClass restoreBapData(Context mContext, int year, int month, int day) {
        Preference mPref = new Preference(mContext, BAP_PREFERENCE_NAME);
        SimpleDateFormat mCalenderFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
        SimpleDateFormat mDayOfWeekFormat = new SimpleDateFormat("E요일", Locale.KOREA);
        Calendar mDate = Calendar.getInstance();
        mDate.set(year, month, day);

        /*Log.e("YEAR", "" + mDate.get(Calendar.YEAR));
        Log.e("MONTH", "" + mDate.get(Calendar.MONTH));
        Log.e("DAY_OF_MONTH", "" + mDate.get(Calendar.DAY_OF_MONTH));*/

        restoreBapDateClass mData = new restoreBapDateClass();

        String mPrefLunchName = getBapStringFormat(year, month, day, TYPE_LUNCH);
        String mPrefDinnerName = getBapStringFormat(year, month, day, TYPE_DINNER);

        /*Log.e("mPrefLunchName", "" + mPrefLunchName);
        Log.e("mPrefDinnerName", "" + mPrefDinnerName);*/

        mData.Calender = mCalenderFormat.format(mDate.getTime());
        mData.DayOfTheWeek = mDayOfWeekFormat.format(mDate.getTime());
        mData.Lunch = mPref.getString(mPrefLunchName, null);
        mData.Dinner = mPref.getString(mPrefDinnerName, null);

        /*Log.e("mData.Calender", "" + mData.Calender);
        Log.e("mData.DayOfTheWeek", "" + mData.DayOfTheWeek);
        Log.e("mData.Lunch", "" + mData.Lunch);
        Log.e("mData.Dinner", "" + mData.Dinner);*/

        if (mData.Lunch == null && mData.Dinner == null) {
            mData.isBlankDay = true;
        }

        return mData;
    }

    public static class restoreBapDateClass {
        public String Calender;
        public String DayOfTheWeek;
        public String Lunch;
        public String Dinner;
        public boolean isBlankDay = false;
    }

    public static boolean mStringCheck(String mString) {
        if (mString == null || "".equals(mString) || " ".equals(mString))
            return true;
        return false;
    }

    public static todayBapData getTodayBap(Context mContext) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        restoreBapDateClass mData = BapTool.restoreBapData(mContext, year, month, day);
        todayBapData mReturnData = new todayBapData();

        if (!mData.isBlankDay) {
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

            /**
             * hour : 0~23
             *
             * 0~13 : Lunch
             * 14~23 : Dinner
             */
            if (hour <= 13) {
                mReturnData.title = mContext.getString(R.string.today_lunch);
                mReturnData.info = (!MealLibrary.isMealCheck(mData.Lunch) ? mContext.getString(R.string.no_data_lunch) : mData.Lunch);
            } else {
                mReturnData.title = mContext.getString(R.string.today_dinner);
                mReturnData.info = (!MealLibrary.isMealCheck(mData.Dinner) ? mContext.getString(R.string.no_data_dinner) : mData.Dinner);
            }
        } else {
            mReturnData.title = mContext.getString(R.string.no_data_title);
            mReturnData.info = mContext.getString(R.string.no_data_message);
        }

        return mReturnData;
    }

    public static class todayBapData {
        public String title;
        public String info;
    }

    public static String replaceString(String mString) {
        String[] mTrash = {"①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫", "⑬"};
        for (String e : mTrash) {
            mString = mString.replace(e, "");
        }

        mString = mString.replace("\n", "  ");

        return mString;
    }
}