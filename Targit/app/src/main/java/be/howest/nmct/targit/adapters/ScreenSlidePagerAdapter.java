package be.howest.nmct.targit.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * A simple pager adapter that represents Fragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> stappen;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> stappen) {
        super(fm);
        this.stappen = stappen;
    }

    @Override
    public Fragment getItem(int position) {
        return stappen.get(position);
    }

    @Override
    public int getCount() {
        return stappen.size();
    }
}
