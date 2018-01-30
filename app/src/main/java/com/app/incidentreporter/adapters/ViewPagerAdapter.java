package com.app.incidentreporter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.incidentreporter.fragments.IncidentHistoryFragment;
import com.app.incidentreporter.fragments.NewIncidentFragment;

/**
 * Created by Mishael on 1/30/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    private static int TAB_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NewIncidentFragment.newInstance();
            case 1:
                return IncidentHistoryFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return NewIncidentFragment.TITLE;
            case 1:
                return IncidentHistoryFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}
