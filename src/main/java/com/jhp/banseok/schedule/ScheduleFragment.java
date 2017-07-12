package com.jhp.banseok.schedule;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.tool.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by whdghks913 on 2015-12-10.
 */
public class ScheduleFragment extends Fragment {

    public static Fragment getInstance(int month) {
        ScheduleFragment mFragment = new ScheduleFragment();

        Bundle args = new Bundle();
        args.putInt("month", month);
        mFragment.setArguments(args);

        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final ScheduleAdapter mAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View mView, int position) {
                try {
                    String date = mAdapter.getItemData(position).date;
                    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREA);

                    Calendar mCalendar = Calendar.getInstance();
                    long nowTime = mCalendar.getTimeInMillis();

                    mCalendar.setTime(mFormat.parse(date));
                    long touchTime = mCalendar.getTimeInMillis();

                    long diff = (touchTime - nowTime);

                    boolean isPast = false;
                    if (diff < 0) {
                        diff = -diff;
                        isPast = true;
                    }

                    int diffDays = (int) (diff /= 24 * 60 * 60 * 1000);
                    String mText = "";

                    if (diffDays == 0)
                        mText += "오늘 일정입니다.";
                    else if (isPast)
                        mText = "선택하신 날짜는 " + diffDays + "일전 날짜입니다";
                    else
                        mText = "선택하신 날짜까지 " + diffDays + "일 남았습니다";

                    Snackbar.make(mView, mText, Snackbar.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }));

        Bundle args = getArguments();
        int month = args.getInt("month");

        switch (month) {
            case 3:
                mAdapter.addItem("3.1절", "2017.03.01 (수)", true);
                mAdapter.addItem("입학식", "2017.03.02 (목)");
                mAdapter.addItem("전국 연합 학력 평가 (전학년)", "2017.03.09 (목)");
                mAdapter.addItem("학부모총회(3) \n 재난대피훈련", "2017.03.15 (수)");
                mAdapter.addItem("학부모총회(2)", "2017.03.16 (목)");
                mAdapter.addItem("학부모총회(1)", "2017.03.17 (금)");
                mAdapter.addItem("수학여행(2)", "2017.03.22 (수)");
                mAdapter.addItem("수학여행(2)", "2017.03.23 (목)");
                mAdapter.addItem("수학여행(2)", "2017.03.24 (금)");
                break;
            case 4:
                mAdapter.addItem("전국연합(3)", "2017.04.12 (수)");
                mAdapter.addItem("영어 듣기 평가(1)", "2017.04.18 (화)");
                mAdapter.addItem("영어 듣기 평가(2)", "2017.04.19 (수)");
                mAdapter.addItem("영어 듣기 평가(3)", "2017.04.20 (목)");
                break;
            case 5:
                mAdapter.addItem("석가탄신일", "2017.05.03 (수)",true);
                mAdapter.addItem("재량휴업일", "2017.05.04 (목)",true);
                mAdapter.addItem("어린이날", "2017.05.05 (금)",true);
                mAdapter.addItem("개교기념일", "2017.05.08 (월)",true);
                mAdapter.addItem("중간고사", "2017.05.09 (화) ~ 2017.05.12 (금)");
                mAdapter.addItem("스승의날 \n 재난대피훈련", "2017.05.15 (월)");
                mAdapter.addItem("스포츠데이", "2017.05.19 (금)");
                break;
            case 6:
                mAdapter.addItem("전국 연합 학력 평가 (1,2,3학년)", "2017.06.01 (목)");
                mAdapter.addItem("진로 체험의 날", "2017.06.02 (금)");
                mAdapter.addItem("현충일", "2017.06.06 (화)",true);
                mAdapter.addItem("화재대피훈련", "2017.06.19 (월)");
                mAdapter.addItem("학업성취도평가 (2)", "2017.06.20 (화)");
                break;
            case 7:
                mAdapter.addItem("1학기 기말고사", "2017.07.06 (목) ~ 2017.07.11(화)");
                mAdapter.addItem("전국 연합 학력 평가 (3학년)", "2017.07.12 (수)");
                mAdapter.addItem("제헌절", "2017.07.17 (월)");
                mAdapter.addItem("방학식", "2017.07.20 (목)",true);
                break;
            case 8:
                mAdapter.addItem("개학", "2017.08.09 (수)");
                mAdapter.addItem("광복절", "2017.08.15 (화)",true);
                mAdapter.addItem("2학기 중간고사(3)", "2017.08.23 (수)");
                mAdapter.addItem("2학기 중간고사(3) \n 민방공훈련", "2017.08.24 (목)");
                mAdapter.addItem("2학기 중간고사(3)", "2017.08.25 (금)");
                break;
            case 9:
                mAdapter.addItem("전국 연합 학력 평가 (1,2,3학년)", "2017.09.06 (목)");
                mAdapter.addItem("영어 듣기 평가(1)", "2017.09.19 (화)");
                mAdapter.addItem("영어 듣기 평가(2)", "2017.09.20 (수)");
                mAdapter.addItem("영어 듣기 평가(3)", "2017.09.21 (목)");
                mAdapter.addItem("2학기 중간고사", "2017.09.26 (화) ~ 2017.09.29 (금)");

                break;
            case 10:
                mAdapter.addItem("재량휴업일", "2017.10.02 (월)",true);
                mAdapter.addItem("개천절", "2017.10.03 (화)",true);
                mAdapter.addItem("추석", "2017.10.04 (수)",true);
                mAdapter.addItem("대체휴일", "2017.10.06 (금)",true);
                mAdapter.addItem("한글날", "2017.10.09 (월)",true);
                mAdapter.addItem("수학여행(1)", "2017.10.11 (수) ~ 2017.10.13",true);
                mAdapter.addItem("재난대피훈련", "2017.10.16 (월)");
                mAdapter.addItem("전국연합(3)", "2017.10.17 (화)");
                break;
            case 11:
                mAdapter.addItem("수능", "2017.11.16 (목)",true);
                mAdapter.addItem("전국 연합 학력 평가 (1,2학년)", "2017.11.22 (수)");
                break;
            case 12:
                mAdapter.addItem("2학기 기말고사 (1,2학년)", "2017.12.07 (목)");
                mAdapter.addItem("2학기 기말고사 (1,2학년)", "2017.12.08 (금)");
                mAdapter.addItem("2학기 기말고사 (1,2학년)", "2017.12.11 (월)");
                mAdapter.addItem("2학기 기말고사 (1,2학년)", "2017.12.12 (월)");
                mAdapter.addItem("축제", "2017.12.22 (금)",true);
                mAdapter.addItem("성탄절", "2017.12.25 (월)",true);
                mAdapter.addItem("방학식", "2017.12.28 (목)", true);
                break;
            case 1:
                mAdapter.addItem("신정", "2018.01.01 (일)", true);
                mAdapter.addItem("등교일", "2018.01.31 (수)");
                break;
            case 2:
                mAdapter.addItem("등교일", "2018.02.01 (목)");
                mAdapter.addItem("졸업식", "2018.02.02 (금)",true);
                mAdapter.addItem("설날", "2018.02.15 (목) ~ 2018.02.16 (토)",true);
                mAdapter.addItem("대체휴일", "2018.02.19 (월)",true);
                mAdapter.addItem("등교일(1,2)", "2018.02.20 (화)");
                mAdapter.addItem("종업식(1,2)", "2018.02.21 (수)",true);
                break;
        }

        return recyclerView;
    }
}
