package com.hualianzb.sec.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wty on 2017/8/23.
 * desc:主页面viewpager的适配器
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
