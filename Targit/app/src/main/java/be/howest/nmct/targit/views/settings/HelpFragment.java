package be.howest.nmct.targit.views.settings;


import be.howest.nmct.targit.adapters.ScreenSlidePagerAdapter;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.R;

public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        //aanmaken van de fragments voor elke help stap
        List<Fragment> mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(HelpStapFragment.newInstance("Ga naar instellingen", R.drawable.help_stap_1));
        mFragmentList.add(HelpStapFragment.newInstance("Zet Bluetooth aan", R.drawable.help_stap_2));
        mFragmentList.add(HelpStapFragment.newInstance("Koppel de Bluetooth apparaten\n" +
                "met de naam TARGIT", R.drawable.help_stap_3));
        mFragmentList.add(HelpStapFragment.newInstance("Geef de pincode 1234 in", R.drawable.help_stap_4));

        // Instantiate a ViewPager and a PagerAdapter.
        /*
          The pager widget, which handles animation and allows swiping horizontally to access previous
          and next wizard steps.
         */
        ViewPager mPager = (ViewPager) view.findViewById(R.id.pager);
        /*
         The pager adapter, which provides the pages to the view pager widget.
         */
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), mFragmentList);
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

        //hide the toolbar
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        return view;
    }


}
