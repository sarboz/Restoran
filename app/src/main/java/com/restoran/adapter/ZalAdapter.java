package com.restoran.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.restoran.Fragments.ZalFragment;
import com.restoran.Models.ZalStol;

import java.util.List;


public class ZalAdapter extends FragmentPagerAdapter {
    List<ZalStol.Zal> listOfZal;
    String id;

    public ZalAdapter(FragmentManager fm, List<ZalStol.Zal> list, String id) {
        super(fm);
        listOfZal = list;
        this.id=id;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        ZalStol.Zal z = listOfZal.get(position);
        z.getStol();
        return z.getName();
    }

    @Override
    public Fragment getItem(int position) {
        ZalStol.Zal z = listOfZal.get(position);
        return ZalFragment.newInstance(position + 1, z.getName(),z.getId(), z.getStol(), id);
    }

    @Override
    public int getCount() {

        return listOfZal.size();
    }
}
