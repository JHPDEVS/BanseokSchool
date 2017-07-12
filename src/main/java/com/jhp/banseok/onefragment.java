package com.jhp.banseok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class onefragment extends Fragment{

    public onefragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        view.findViewById(R.id.call_1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:042-824-3022"));
                try {
                    // the user can choose the email client
                    startActivity(Intent.createChooser(intent, "전화 앱 선택"));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "전화가능한 기기아님.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.call_2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:042-824-3028"));
                try {
                    // the user can choose the email client
                    startActivity(Intent.createChooser(intent, "전화 앱 선택"));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "전화가능한 기기아님.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.call_3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:042-824-3022"));
                try {
                    // the user can choose the email client
                    startActivity(Intent.createChooser(intent, "전화 앱 선택"));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "전화가능한 기기아님.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //구글 지도 연결 기능
        View maps_card = view.findViewById(R.id.maps_card);
        maps_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent src = new Intent(Intent.ACTION_VIEW);
                src.setData(Uri
                        .parse("https://www.google.co.kr/maps/place/%EB%8C%80%EC%A0%84%EB%B0%98%EC%84%9D%EA%B3%A0%EB%93%B1%ED%95%99%EA%B5%90/@36.3868828,127.3114146,17z/data=!3m1!4b1!4m2!3m1!1s0x35654b2363f3c789:0x57badd0e1db54637"));
                startActivity(src);
            }
        });
        return view;
    }}