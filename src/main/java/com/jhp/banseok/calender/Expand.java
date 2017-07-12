package com.jhp.banseok.calender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.jhp.banseok.R;

public class Expand extends AppCompatActivity {
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "2017년 학사일정"));

        ExpandableListAdapter.Item f = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "3월");
        f.invisibleChildren = new ArrayList<>();
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-02 (목) 개학식"));
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-09 (목) 전국연합(1,2,3)"));
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-15 (수) 학부모총회(3)"));
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-16 (목) 학부모총회(2)"));
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-17 (금) 학부모총회(1)"));
        f.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-03-22 (수) ~ 2017-03-24 (금) 수학여행(2)"));
        data.add(f);

        ExpandableListAdapter.Item f2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "4월");
        f2.invisibleChildren = new ArrayList<>();
        f2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-04-12 (수) 전국연합(3)"));
        f2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-04-18 (화) 영어듣기평가(1)"));
        f2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-04-19 (수) 영어듣기평가(2)"));
        f2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-04-20 (목) 영어듣기평가(3)"));
        data.add(f2);

        ExpandableListAdapter.Item f3 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "5월");
        f3.invisibleChildren = new ArrayList<>();
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-08 (월) 개교기념일"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-03 (수) 석가탄신일"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-04 (목) 재량휴업일"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-05 (금) 어린이날"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-08 (월) 개교기념일"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-09 (화) ~ 2017-05-12 (금) 중간고사"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-15 (월) 스승의날 , 재난대비훈련"));
        f3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "2017-05-19 (금) 스포츠데이"));
        data.add(f3);
        recyclerview.setAdapter(new ExpandableListAdapter(data));
    }
}
