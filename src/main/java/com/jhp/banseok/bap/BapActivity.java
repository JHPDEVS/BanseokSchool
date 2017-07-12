package com.jhp.banseok.bap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jhp.banseok.R;
import com.jhp.banseok.bap.star.BapStarActivity;
import com.jhp.banseok.bap.tool.BapTool;
import com.jhp.banseok.bap.tool.Preference;
import com.jhp.banseok.bap.tool.ProcessTask;
import com.jhp.banseok.bap.tool.Tools;

import java.util.Calendar;



public class BapActivity extends AppCompatActivity {

    ListView mListView;
    BapAdapter mAdapter;

    Calendar mCalendar;
    int YEAR, MONTH, DAY;
    int DAY_OF_WEEK;

    BapDownloadTask mProcessTask;
    ProgressDialog mDialog;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bap);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);


        }

        getCalendarInstance(true);
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new BapAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                BapListData mData = mAdapter.getItem(position);
                String mShareBapMsg = String.format(getString(R.string.shareBap_message_msg),
                        mData.mCalender, mData.mLunch, mData.mDinner);
                withShare(mShareBapMsg);

                return true;
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCalendarInstance(true);
                getBapList(true);

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalenderBap();
            }
        });

        getBapList(true);
    }

    private void getCalendarInstance(boolean getInstance) {
        if (getInstance || (mCalendar == null))
            mCalendar = Calendar.getInstance();
        YEAR = mCalendar.get(Calendar.YEAR);
        MONTH = mCalendar.get(Calendar.MONTH);
        DAY = mCalendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK = mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    private void getBapList(boolean isUpdate) {
        boolean isNetwork = Tools.isOnline(this);

        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();

        getCalendarInstance(false);

        final Calendar mToday = Calendar.getInstance();
        final int TodayYear = mToday.get(Calendar.YEAR);
        final int TodayMonth = mToday.get(Calendar.MONTH);
        final int TodayDay = mToday.get(Calendar.DAY_OF_MONTH);

        // 이번주 월요일 날짜를 가져온다
        mCalendar.add(Calendar.DATE, 2 - DAY_OF_WEEK);

        for (int i = 0; i < 6; i++) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            BapTool.restoreBapDateClass mData =
                    BapTool.restoreBapData(this, year, month, day);

            if (mData.isBlankDay) {
                if (isUpdate && isNetwork) {
                    mDialog = new ProgressDialog(this);
                    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mDialog.setMax(100);
                    mDialog.setTitle(R.string.loading_title);
                    mDialog.setCancelable(false);
                    mDialog.show();

                    mProcessTask = new BapDownloadTask(this);
                    mProcessTask.execute(year, month, day);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
                    builder.setTitle(R.string.no_network_title);
                    builder.setMessage(R.string.no_network_msg);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }

                return;
            }

            // if day equals today
            if ((year == TodayYear) && (month == TodayMonth) && (day == TodayDay)) {
                mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, true);
            } else {
                mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Lunch, mData.Dinner, false);
            }

            mCalendar.add(Calendar.DATE, 1);
        }

        mCalendar.set(YEAR, MONTH, DAY);
        mAdapter.notifyDataSetChanged();
        setCurrentItem();
    }

    private void setCurrentItem() {
        if (DAY_OF_WEEK > 1 && DAY_OF_WEEK < 7) {
            if (mAdapter.getCount() == 6)
                mListView.setSelection(DAY_OF_WEEK - 2);
        } else {
            mListView.setSelection(0);
        }
    }

    public void setCalenderBap() {
        getCalendarInstance(false);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                mCalendar.set(year, month, day);
                getCalendarInstance(false);

                getBapList(true);
            }
        }, year, month, day, false);
        datePickerDialog.setYearRange(2006, 2030);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), "Tag");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calender) {
            setCalenderBap();

            return true;

        } else if (id == R.id.action_refresh) {
            boolean isNetwork = Tools.isOnline(this);

            if (isNetwork) {
                getCalendarInstance(false);

                mCalendar.add(Calendar.DATE, 2 - DAY_OF_WEEK);

                String mPrefLunchName = BapTool.getBapStringFormat(YEAR, MONTH, DAY, BapTool.TYPE_LUNCH);
                String mPrefDinnerName = BapTool.getBapStringFormat(YEAR, MONTH, DAY, BapTool.TYPE_DINNER);

                Preference mPref = new Preference(getApplicationContext(), BapTool.BAP_PREFERENCE_NAME);
                mPref.remove(mPrefLunchName);
                mPref.remove(mPrefDinnerName);

                getBapList(true);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.no_network_title);
                builder.setMessage(R.string.no_network_msg);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }

            return true;
        } else if (id == R.id.action_today) {
            getCalendarInstance(true);
            getBapList(true);
            setCalenderBap();
            return true;

        } else if (id == R.id.action_show_star) {
            startActivity(new Intent(this, BapStarActivity.class).putExtra("starType", 2));
        }
        else if (id ==  android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class BapDownloadTask extends ProcessTask {
        public BapDownloadTask(Context mContext) {
            super(mContext);
        }

        @Override
        public void onPreDownload() {

        }

        @Override
        public void onUpdate(int progress) {
            mDialog.setProgress(progress);
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null)
                mDialog.dismiss();

            if (result == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BapActivity.this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.I_do_not_know_the_error_title);
                builder.setMessage(R.string.I_do_not_know_the_error_message);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();

                return;
            }

            getBapList(false);

            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void withShare(String mShareBapMsg) {
        Intent msg = new Intent(Intent.ACTION_SEND);
        msg.addCategory(Intent.CATEGORY_DEFAULT);
        msg.putExtra(Intent.EXTRA_TITLE, getString(R.string.shareBap_title));
        msg.putExtra(Intent.EXTRA_TEXT, mShareBapMsg);
        msg.setType("text/plain");
        startActivity(Intent.createChooser(msg, getString(R.string.shareBap_title)));
    }

}
