package com.jhp.banseok;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by kundan on 10/16/2015.
 */
public class Pager extends FragmentStatePagerAdapter {
    public Pager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new Main2Activity();
                break;
            case 1:
                frag=new open();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Game";
                break;
            case 1:
                title="Movie";
                break;
        }

        return title;
    }
}
