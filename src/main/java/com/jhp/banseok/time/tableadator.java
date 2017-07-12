package com.jhp.banseok.time;

/**
 * Created by home on 2016-02-20.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhp.banseok.R;

import java.util.ArrayList;


/**
 * Created by whdghks913 on 2015-12-10.
 */
public class tableadator extends RecyclerView.Adapter<tableadator.TimeTableViewHolder> {
    //    private int mBackground;
    private ArrayList<TimeTableInfo> mValues = new ArrayList<>();

    public tableadator(Context mContext) {
//        TypedValue mTypedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
    }

    public void addItem(int time, String name) {
        TimeTableInfo addInfo = new TimeTableInfo();

        addInfo.time = time;
        addInfo.name = name;

        mValues.add(addInfo);
    }

    @Override
    public tableadator.TimeTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timetable_item, parent, false);
//        mView.setBackgroundResource(mBackground);

        return new TimeTableViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TimeTableViewHolder holder, int position) {
        TimeTableInfo mInfo = getItemData(position);

        holder.mTimeText.setText(String.valueOf(mInfo.time));
        holder.mTimeName.setText(mInfo.name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public TimeTableInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class TimeTableViewHolder extends RecyclerView.ViewHolder {
        //        public final View mView;
        public final TextView mTimeText, mTimeName;

        public TimeTableViewHolder(View mView) {
            super(mView);
//            this.mView = mView;

            mTimeText = (TextView) mView.findViewById(R.id.mTimeText);
            mTimeName = (TextView) mView.findViewById(R.id.mTimeName);
        }
    }

    public class TimeTableInfo {
        public int time;
        public String name;
    }
}
