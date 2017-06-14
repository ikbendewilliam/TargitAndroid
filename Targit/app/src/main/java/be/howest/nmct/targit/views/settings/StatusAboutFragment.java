package be.howest.nmct.targit.views.settings;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.ScreenSlidePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusAboutFragment extends Fragment {

    private List<Fragment> fragments;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public StatusAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status_about, container, false);

        //aanmaken fragments
        fragments = new ArrayList<>();
        fragments.add(new StatusFragment());
        fragments.add(new AboutFragment());

        //get tabs
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.settingsTabs);

        //setup tabs
        tabLayout.addTab(tabLayout.newTab().setText("Status buttons"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) v.findViewById(R.id.settingsPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        // linken  van tabs met viewpager
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return v;
    }

}
