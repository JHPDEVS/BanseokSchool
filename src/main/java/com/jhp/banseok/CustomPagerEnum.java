package com.jhp.banseok;

/**
 * Created by home on 2016-02-21.
 */
public enum CustomPagerEnum {

    RED(R.string.school_office, R.layout.schoolinfo),
    BLUE(R.string.not_go_to_school_title, R.layout.activity_open);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}