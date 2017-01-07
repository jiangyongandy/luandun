package com.jy.xinlangweibo.ui.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by JIANG on 2017/1/4.
 */

public class BaseViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private List<String> titles;

    public BaseViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles)
    {

        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position)
    {

        return fragments.get(position);
    }

    @Override
    public int getCount()
    {

        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {

        return titles.get(position);
    }

}
