package com.jhp.banseok.schedule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jhp.banseok.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ScheduleActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(String.format(getString(R.string.schetitile)));

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        viewPager = (ViewPager) findViewById(R.id.mViewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        setCurrentItem();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter mAdapter = new Adapter(getSupportFragmentManager());

        mAdapter.addFragment("3월", ScheduleFragment.getInstance(3));
        mAdapter.addFragment("4월", ScheduleFragment.getInstance(4));
        mAdapter.addFragment("5월", ScheduleFragment.getInstance(5));
        mAdapter.addFragment("6월", ScheduleFragment.getInstance(6));
        mAdapter.addFragment("7월", ScheduleFragment.getInstance(7));
        mAdapter.addFragment("8월", ScheduleFragment.getInstance(8));
        mAdapter.addFragment("9월", ScheduleFragment.getInstance(9));
        mAdapter.addFragment("10월", ScheduleFragment.getInstance(10));
        mAdapter.addFragment("11월", ScheduleFragment.getInstance(11));
        mAdapter.addFragment("12월", ScheduleFragment.getInstance(12));
        mAdapter.addFragment("2018년 1월", ScheduleFragment.getInstance(1));
        mAdapter.addFragment("2018년 2월", ScheduleFragment.getInstance(2));

        viewPager.setAdapter(mAdapter);
    }

    private void setCurrentItem() {
        int month = Calendar.getInstance().get(Calendar.MONTH);

        if (month >= 2) month -= 2;
        else month += 9;

        viewPager.setCurrentItem(month);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(String mTitle, Fragment mFragment) {
            mFragments.add(mFragment);
            mFragmentTitles.add(mTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

         if (id == android.R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
