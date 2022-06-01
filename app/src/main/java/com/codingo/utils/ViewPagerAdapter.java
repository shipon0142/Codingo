package com.codingo.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codingo.HomeFragment;
import com.codingo.MeFragment;

import com.codingo.SubscriptionFragment;
import com.codingo.bd.R;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;

    public ViewPagerAdapter(Context context , FragmentManager fragmentManager , int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
    }
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab2" };
    private int[] imageResId = { R.drawable.home_button, R.drawable.star_button,R.drawable.profile_button };

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);

        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        return v;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SubscriptionFragment();
            case 2:
                return new MeFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}