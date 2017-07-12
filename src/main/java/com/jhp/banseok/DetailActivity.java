package com.jhp.banseok;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.jhp.banseok.parser.RSSFeed;


public class DetailActivity extends FragmentActivity {

    RSSFeed feed;
    int pos;
    private DescAdapter adapter;
    private ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        // Get the feed object and the position from the Intent
        feed = (RSSFeed) getIntent().getExtras().get("feed");
        pos = getIntent().getExtras().getInt("pos");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Initialize the views
        adapter = new DescAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        // Set Adapter to pager:
        pager.setAdapter(adapter);
        pager.setCurrentItem(pos);
    }

    public class DescAdapter extends FragmentStatePagerAdapter {
        public DescAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return feed.getItemCount();
        }

        @Override
        public Fragment getItem(int position) {

            DetailFragment frag = new DetailFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("feed", feed);
            bundle.putInt("pos", position);
            frag.setArguments(bundle);

            return frag;

        }

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

}
