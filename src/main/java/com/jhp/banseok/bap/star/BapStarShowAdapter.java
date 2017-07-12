package com.jhp.banseok.bap.star;

/**
 * Created by home on 2016-01-17.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by whdghks913 on 2015-12-09.
 */
class BapStarShowViewHolder {
    public TextView mMemo;
}

class BapStarShowData {
    public String mText;
}

public class BapStarShowAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<BapStarShowData> mListData = new ArrayList<BapStarShowData>();

    public BapStarShowAdapter(Context mContext) {
        super();

        this.mContext = mContext;
    }

    public void addItem(String mText) {
        BapStarShowData addItemInfo = new BapStarShowData();
        addItemInfo.mText = mText;

        mListData.add(addItemInfo);
    }

    public void clearData() {
        mListData.clear();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public BapStarShowData getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BapStarShowViewHolder mHolder;

        if (convertView == null) {
            mHolder = new BapStarShowViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);

            mHolder.mMemo = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(mHolder);
        } else {
            mHolder = (BapStarShowViewHolder) convertView.getTag();
        }

        BapStarShowData mData = mListData.get(position);
        String mText = mData.mText;

        mHolder.mMemo.setText(mText);

        return convertView;
    }
}
