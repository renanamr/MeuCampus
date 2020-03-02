package com.meucampus.arthur.testez.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.meucampus.arthur.testez.Fragments.CalcAnual;
import com.meucampus.arthur.testez.Fragments.CalcSemestre;

/**
 * Created by renan on 16/11/2016.
 */
public class PagerAdp extends FragmentPagerAdapter {

    private String[] mTableTitles;

    public PagerAdp(FragmentManager fm, String[] mTableTitles) {
        super(fm);
        this.mTableTitles=mTableTitles;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CalcAnual();
            case 1:
                return new CalcSemestre();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return this.mTableTitles[position];
    }

    @Override
    public int getCount() {

        return this.mTableTitles.length;
    }

}
