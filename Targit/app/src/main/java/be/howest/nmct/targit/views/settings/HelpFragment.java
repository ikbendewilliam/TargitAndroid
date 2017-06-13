package be.howest.nmct.targit.views.settings;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;

public class HelpFragment extends Fragment {

    private List<Fragment> stappen;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        //aanmaken van de fragments voor elke help stap
        stappen = new ArrayList<Fragment>();
        stappen.add(HelpStapFragment.newInstance("Ga naar instellingen",R.drawable.help_stap_1));
        stappen.add(HelpStapFragment.newInstance("Zet Bluetooth aan",R.drawable.help_stap_2));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // linken  van tabs met viewpager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        //back toets instellen
        view.findViewById(R.id.help_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    /**
     * A simple pager adapter that represents Fragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
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


}
