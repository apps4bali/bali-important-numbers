package com.balicodes.balinumbers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.balicodes.balinumbers.sections.Embassies;
import com.balicodes.balinumbers.sections.Emergency;
import com.balicodes.balinumbers.sections.FireFighter;
import com.balicodes.balinumbers.sections.Hospitals;
import com.balicodes.balinumbers.sections.Police;
import com.balicodes.balinumbers.sections.Taxi;

/**
 * Created by eka on 7/3/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Emergency();
            case 1:
                return new Police();
            case 2:
                return new Hospitals();
            case 3:
                return new FireFighter();
            case 4:
                return new Taxi();
            case 5:
                return new Embassies();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Emergency.TITLE;
            case 1:
                return Police.TITLE;
            case 2:
                return Hospitals.TITLE;
            case 3:
                return FireFighter.TITLE;
            case 4:
                return Taxi.TITLE;
            case 5:
                return Embassies.TITLE;
        }
        return super.getPageTitle(position);
    }
}
