package com.jhp.banseok.calender;

import java.util.ArrayList;

import com.jhp.banseok.R;
import com.jhp.banseok.calender.adapter.AndroidListAdapter;
import com.jhp.banseok.calender.util.CalendarCollection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListViewActivity extends Activity implements OnClickListener {

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private Button btn_calender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-02","개학식"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-09","전국연합(1,2,3)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-15","재난대피훈련\n학부모총회(3)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-16","학부모총회(2)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-17","학부모총회(1)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-22","수학여행(2)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-23","수학여행(2)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-03-24","수학여행(2)"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-04-12","전국연합(3)"));
        getWidget();
    }



    public void getWidget(){
        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);

        lv_android = (ListView) findViewById(R.id.lv_android);
        list_adapter=new AndroidListAdapter(ListViewActivity.this,R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);

    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                startActivity(new Intent(ListViewActivity.this,CalenderActivity.class));

                break;

            default:
                break;
        }

    }

}
