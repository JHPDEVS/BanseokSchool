package com.jhp.banseok.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.tool.TimeTableTool;


/**
 * Created by whdghks913 on 2015-12-02.
 */
public class tablefragment extends Fragment {

    public static Fragment getInstance(int mGrade, int mClass, int dayOfWeek) {
        tablefragment mFragment = new tablefragment();

        Bundle args = new Bundle();
        args.putInt("mGrade", mGrade);
        args.putInt("mClass", mClass);
        args.putInt("dayOfWeek", dayOfWeek);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final tableadator mAdapter = new tableadator(getActivity());
        recyclerView.setAdapter(mAdapter);

        Bundle args = getArguments();
        int mGrade = args.getInt("mGrade");
        int mClass = args.getInt("mClass");
        int dayOfWeek = args.getInt("dayOfWeek");

        TimeTableTool.timeTableData mData = TimeTableTool.getTimeTableData(mGrade, mClass, dayOfWeek);

        for (int position = 0; position < 7; position++) {
            mAdapter.addItem(position + 1, mData.subject[position]);
        }

        return recyclerView;
    }
}
