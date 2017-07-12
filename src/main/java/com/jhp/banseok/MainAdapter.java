package com.jhp.banseok;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by whdghks913 on 2015-11-30.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    //    private int mBackground;
    private ArrayList<MainInfo> mValues = new ArrayList<>();

    public MainAdapter(Context mContext) {
//        TypedValue mTypedValue = new TypedValue();
//        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
    }

    public void addItem(int imageId, String mTitle, String mText) {
        MainInfo addInfo = new MainInfo();

        addInfo.imageId = imageId;
        addInfo.mTitle = mTitle;
        addInfo.mText = mText;
        addInfo.isSimple = false;
        addInfo.isSimpleButDetailedLayout = false;

        mValues.add(addInfo);
    }

    public void addItem(int imageId, String mTitle, String mText, boolean isSimple) {
        MainInfo addInfo = new MainInfo();

        addInfo.imageId = imageId;
        addInfo.mTitle = mTitle;
        addInfo.mText = mText;
        addInfo.isSimple = true;
        addInfo.isSimpleButDetailedLayout = isSimple;

        mValues.add(addInfo);
    }

    public void addItem(int imageId, String mTitle, String mText, String mSimpleTitle, String mSimpleText) {
        MainInfo addInfo = new MainInfo();

        addInfo.imageId = imageId;
        addInfo.mTitle = mTitle;
        addInfo.mText = mText;
        addInfo.mSimpleTitle = mSimpleTitle;
        addInfo.mSimpleText = mSimpleText;
        addInfo.isSimple = true;
        addInfo.isSimpleButDetailedLayout = false;

        mValues.add(addInfo);
    }

    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_fragment, parent, false);
//        mView.setBackgroundResource(mBackground);

        return new MainViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        MainInfo mInfo = getItemData(position);

        holder.mTitle.setText(mInfo.mTitle);
        holder.mText.setText(mInfo.mText);

        if (mInfo.isSimple && !(mInfo.isSimpleButDetailedLayout)) {
            holder.mSimpleLayout.setVisibility(View.VISIBLE);
            holder.mSimpleTitle.setText(mInfo.mSimpleTitle);
            holder.mSimpleText.setText(mInfo.mSimpleText);
        } else {
            holder.mSimpleLayout.setVisibility(View.GONE);
        }

        Glide.with(holder.mCircleImageView.getContext())
                .load(mInfo.imageId)
                .fitCenter()
                .into(holder.mCircleImageView);

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public MainInfo getItemData(int position) {
        return mValues.get(position);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        //        public final View mView;
        public final CircleImageView mCircleImageView;
        public final LinearLayout mSimpleLayout;
        public final TextView mTitle, mText, mSimpleTitle, mSimpleText ;

        public MainViewHolder(View mView) {
            super(mView);
//            this.mView = mView;

            mCircleImageView = (CircleImageView) mView.findViewById(R.id.mCircleImageView);
            mTitle = (TextView) mView.findViewById(R.id.mTitle);
            mText = (TextView) mView.findViewById(R.id.mText);
            mSimpleLayout = (LinearLayout) mView.findViewById(R.id.mSimpleLayout);
            mSimpleTitle = (TextView) mView.findViewById(R.id.mSimpleTitle);
            mSimpleText = (TextView) mView.findViewById(R.id.mSimpleText);
        }
    }

    public class MainInfo {
        public boolean isSimple;
        public boolean isSimpleButDetailedLayout = false;
        public int imageId;
        public String mTitle;
        public String mText;
        public String mSimpleTitle;
        public String mSimpleText;
    }
}
