package com.jhp.banseok.bap.star;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.spreadsheets.GoogleSheetTask;
import com.jhp.banseok.bap.tool.BapTool;
import com.jhp.banseok.bap.tool.Database;
import com.jhp.banseok.bap.tool.Preference;
import com.jhp.banseok.bap.tool.TimeTableTool;
import com.jhp.banseok.bap.tool.Tools;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class BapStarActivity extends AppCompatActivity {
    public static final String StarRateDBName = "RiceStarRate.db";
    public static final String StarRateTableName = "RateInfo";

    Spinner mGiveStarType;
    RatingBar mPostRatingBar;
    EditText mBapReview;

    Spinner mDateSpinner;
    RatingBar mLunchRatingStar, mDinnerRatingStar;
    ListView mLunchListView, mDinnerListView;
    BapStarShowAdapter mLunchAdapter, mDinnerAdapter;
    int year, month, day;
    TextView lunchPeopleCount, dinnerPeopleCount;

    ArrayList<String> mTimeData = new ArrayList<>();

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bap_star);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);


        }

        LinearLayout giveStarLayout = (LinearLayout) findViewById(R.id.giveStarLayout);
        LinearLayout showStarLayout = (LinearLayout) findViewById(R.id.showStarLayout);

        Intent mIntent = getIntent();
        int starType = mIntent.getIntExtra("starType", 1);

        if (starType == 1) {
            giveStarLayout.setVisibility(View.VISIBLE);
            showStarLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("별점 주기");
            mGiveStarType = (Spinner) findViewById(R.id.mGiveStarType);
            mPostRatingBar = (RatingBar) findViewById(R.id.mPostRatingBar);
            mBapReview = (EditText) findViewById(R.id.mBapReview);

        } else if (starType == 2) {
            giveStarLayout.setVisibility(View.GONE);
            showStarLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("별점 확인");
            mDateSpinner = (Spinner) findViewById(R.id.mDateSpinner);
            mLunchRatingStar = (RatingBar) findViewById(R.id.mLunchRatingStar);
            mDinnerRatingStar = (RatingBar) findViewById(R.id.mDinnerRatingStar);
            mLunchListView = (ListView) findViewById(R.id.mLunchListView);
            mDinnerListView = (ListView) findViewById(R.id.mDinnerListView);
            lunchPeopleCount = (TextView) findViewById(R.id.lunchPeopleCount);
            dinnerPeopleCount = (TextView) findViewById(R.id.dinnerPeopleCount);
            mLunchAdapter = new BapStarShowAdapter(this);
            mDinnerAdapter = new BapStarShowAdapter(this);
            Calendar mCalendar = Calendar.getInstance();
            year = mCalendar.get(Calendar.YEAR);
            month = mCalendar.get(Calendar.MONTH);
            day = mCalendar.get(Calendar.DAY_OF_MONTH);

            mLunchListView.setAdapter(mLunchAdapter);
            mDinnerListView.setAdapter(mDinnerAdapter);

            showRiceStar();
        }
    }

    public void postStar(View v) {
        // 0 : Lunch, 1 : Dinner
        int position = mGiveStarType.getSelectedItemPosition();

        if (BapTool.canPostStar(getApplicationContext(), position)) {
            float rate = mPostRatingBar.getRating();
            (new HttpTask()).execute(String.valueOf(position), String.valueOf(rate), mBapReview.getText().toString());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.bap_star_once_title);
            builder.setMessage(R.string.bap_star_once_message);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private class HttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(BapStarActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.post_bap_star_posting));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                HttpPost postRequest = new HttpPost("https://script.google.com/macros/s/AKfycbxgKLVWo-E2Cemll37uYvaHxpWgW17eqoUfBb-n32D5Un0HtQ6b/exec");

                //전달할 값들
                Vector<NameValuePair> nameValue = new Vector<>();
                nameValue.add(new BasicNameValuePair("sheet_name", "RiceStar"));
                nameValue.add(new BasicNameValuePair("type", params[0]));
                nameValue.add(new BasicNameValuePair("rate", params[1]));
                nameValue.add(new BasicNameValuePair("memo", params[2]));
                nameValue.add(new BasicNameValuePair("deviceId", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));


                Preference mPref = new Preference(getApplicationContext());


                //웹 접속 - UTF-8으로
                HttpEntity Entity = new UrlEncodedFormEntity(nameValue, "UTF-8");
                postRequest.setEntity(Entity);

                HttpClient mClient = new DefaultHttpClient();
                mClient.execute(postRequest);

//                웹 서버에서 값받기
//                HttpResponse res = mClient.execute(postRequest);
//                HttpEntity entityResponse = res.getEntity();
//                InputStream im = entityResponse.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(im, "UTF-8"));
//
//                String total = "";
//                String tmp = "";
//                //버퍼에있는거 전부 더해주기
//                //readLine -> 파일내용을 줄 단위로 읽기
//                while ((tmp = reader.readLine()) != null) {
//                    if (tmp != null) {
//                        total += tmp;
//                    }
//                }
//                im.close();

                return Integer.parseInt(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return -1;
        }

        protected void onPostExecute(Integer value) {
            super.onPostExecute(value);

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (value == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BapStarActivity.this, R.style.AppCompatErrorAlertDialogStyle);
                builder.setTitle(R.string.post_bap_star_title);
                builder.setMessage(R.string.post_bap_star_failed);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();

            } else {
                mBapReview.setText("");

                AlertDialog.Builder builder = new AlertDialog.Builder(BapStarActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.post_bap_star_title);
                builder.setMessage(R.string.post_bap_star_success);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();

                BapTool.todayPostStar(getApplicationContext(), value);
            }
        }
    }

    private void showRiceStar() {
        if (Tools.isOnline(getApplicationContext())) {
            if (Tools.isWifi(getApplicationContext())) {
                getStarRateDownloadTask mTask = new getStarRateDownloadTask();
                mTask.execute("https://docs.google.com/spreadsheets/d/1CFwystoN7Pzl0-sRgbWENvzICWA9qvXY0uQ2tmwLfYc/pubhtml?gid=0&single=true");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.no_wifi_title);
                builder.setMessage(R.string.no_wifi_msg);
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        offlineData();
                    }
                });
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getStarRateDownloadTask mTask = new getStarRateDownloadTask();
                        mTask.execute("https://docs.google.com/spreadsheets/d/1CFwystoN7Pzl0-sRgbWENvzICWA9qvXY0uQ2tmwLfYc/pubhtml?gid=0&single=true");
                    }
                });
                builder.show();
            }
        } else {
            offlineData();
        }
    }


    private void offlineData() {
        if (new File(TimeTableTool.mFilePath + StarRateDBName).exists()) {
            showListViewDate();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(BapStarActivity.this, R.style.AppCompatErrorAlertDialogStyle);
            builder.setTitle(R.string.no_network_title);
            builder.setMessage(R.string.no_network_msg);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    class getStarRateDownloadTask extends GoogleSheetTask {
        private Database mDatabase;
        private String[] columnFirstRow;

        @Override
        public void onPreDownload() {
            mDialog = new ProgressDialog(BapStarActivity.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.loading_title));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            new File(TimeTableTool.mFilePath + StarRateDBName).delete();
            mDatabase = new Database();

            this.startRowNumber = 0;
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            if (mDatabase != null)
                mDatabase.release();

            showListViewDate();
        }

        @Override
        public void onRow(int startRowNumber, int position, String[] row) {
            if (startRowNumber == position) {
                columnFirstRow = row;

                StringBuilder Column = new StringBuilder();

                // remove deviceId
                for (int i = 0; i < row.length - 1; i++) {
                    Column.append(row[i]);
                    Column.append(" text, ");
                }

                mDatabase.openOrCreateDatabase(TimeTableTool.mFilePath, StarRateDBName, StarRateTableName, Column.substring(0, Column.length() - 2));
            } else {
                int length = row.length;
                for (int i = 0; i < length - 1; i++) {
                    mDatabase.addData(columnFirstRow[i], row[i]);
                }
                mDatabase.commit(StarRateTableName);
            }
        }
    }

    private void showListViewDate() {
        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, StarRateDBName);
        Cursor mCursor = mDatabase.getData(StarRateTableName, "*");

        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToNext();

            String date = mCursor.getString(1);
//            String type = mCursor.getString(2);
//            int rate = Integer.parseInt(mCursor.getString(3));
//            String memo = mCursor.getString(4);

            if (!mTimeData.contains(date)) {
                mTimeData.add(date);
            }
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTimeData);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDateSpinner.setAdapter(mAdapter);

        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDatabaseData(mTimeData.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDateSpinner.setSelection(mTimeData.size() - 1);
    }

    private void getDatabaseData(String dateName) {
        mLunchAdapter.clearData();
        mDinnerAdapter.clearData();

        Database mDatabase = new Database();
        mDatabase.openDatabase(TimeTableTool.mFilePath, StarRateDBName);
        Cursor mCursor = mDatabase.getData(StarRateTableName, "*");

        int sumLunch = 0, sumDinner = 0;
        int lunchCount = 0, dinnerCount = 0;

        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToNext();

            String date = mCursor.getString(1);
            if (!date.equals(dateName))
                continue;

            String memo = mCursor.getString(4);
            int type = Integer.parseInt(mCursor.getString(2));
            int rate = Integer.parseInt(mCursor.getString(3));

            if (type == 0) {
                if (!memo.isEmpty() || memo.length() != 0)
                    mLunchAdapter.addItem(memo);
                sumLunch += rate;
                lunchCount += 1;
            } else {
                if (!memo.isEmpty() || memo.length() != 0)
                    mDinnerAdapter.addItem(memo);
                sumDinner += rate;
                dinnerCount += 1;
            }
        }

        try {
            if ((sumLunch != 0) && (lunchCount != 0)) {
                float lunch = (new BigDecimal(sumLunch)).divide(new BigDecimal(lunchCount), 2, BigDecimal.ROUND_UP).floatValue();
                mLunchRatingStar.setRating(lunch);
            } else {
                mLunchRatingStar.setRating(0f);
            }

            if ((sumDinner != 0) && (dinnerCount != 0)) {
                float dinner = (new BigDecimal(sumDinner)).divide(new BigDecimal(dinnerCount), 2, BigDecimal.ROUND_UP).floatValue();
                mDinnerRatingStar.setRating(dinner);
            } else {
                mDinnerRatingStar.setRating(0f);
            }

            lunchPeopleCount.setText(String.format(getString(R.string.bap_star_people_count), lunchCount));
            dinnerPeopleCount.setText(String.format(getString(R.string.bap_star_people_count), dinnerCount));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mLunchAdapter.notifyDataSetChanged();
        mDinnerAdapter.notifyDataSetChanged();

        setDynamicHeight(mLunchListView);
        setDynamicHeight(mDinnerListView);
    }
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                // NavUtils.navigateUpFromSameTask(this);

                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);




    };
    private void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null)
            return;

        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

}
