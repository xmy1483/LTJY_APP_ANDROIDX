package com.bac.bacplatform.old.module.insurance.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Wjz on 2017/5/17.
 */

public class InsuranceFragmentPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public InsuranceFragmentPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
