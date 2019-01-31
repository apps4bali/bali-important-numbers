package com.apps4bali.nomorpenting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apps4bali.nomorpenting.models.Section;

import java.util.List;

/**
 * Created by eka on 7/3/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private List<Section> sections;

    PagerAdapter(FragmentManager fm, List<Section> sections) {
        super(fm);

        this.sections = sections;
    }

    @Override
    public Fragment getItem(int i) {
        Section section = sections.get(i);
        Fragment fragment = new SectionFragment();
        fragment.setArguments(section.toBundle());
        return fragment;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Section section = sections.get(position);
        return section.getTitleId();
    }
}
