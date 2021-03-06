package be.howest.nmct.targit.views.infogamemode;


import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.targit.Constants;
import be.howest.nmct.targit.R;
import be.howest.nmct.targit.adapters.ScreenSlidePagerAdapter;
import be.howest.nmct.targit.views.settings.HelpStapFragment;

import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_MEMORIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_SMASHIT;
import static be.howest.nmct.targit.Constants.EXTRA_GAMEMODE_ZENIT;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoGameHelpFragment extends DialogFragment {

    //the gamemode that the dialog box is in
    private String mGameMode;

    //create a new instance of InfoGameHelpFragment
    public static InfoGameHelpFragment newInstance(String gameMode) {
        InfoGameHelpFragment fragment = new InfoGameHelpFragment();

        fragment.mGameMode = gameMode;

        return fragment;
    }

    public InfoGameHelpFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_game_help, container, false);

        setStyle(STYLE_NO_FRAME,getTheme());

        //change background colors based on gamemode
        switch (mGameMode) {
            case EXTRA_GAMEMODE_SMASHIT:
                view.findViewById(R.id.info_game_help_parent_layout).setBackgroundColor(getResources().getColor(R.color.colorSmashit,getActivity().getTheme()));
                break;
            case EXTRA_GAMEMODE_ZENIT:
                view.findViewById(R.id.info_game_help_parent_layout).setBackgroundColor(getResources().getColor(R.color.colorZenit,getActivity().getTheme()));
                break;
            case EXTRA_GAMEMODE_MEMORIT:
                view.findViewById(R.id.info_game_help_parent_layout).setBackgroundColor(getResources().getColor(R.color.colorMemorit,getActivity().getTheme()));
                break;
        }


        //pressing done button closes dialog
        view.findViewById(R.id.info_game_help_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });


        List<Fragment> mFragmentList = new ArrayList<Fragment>();


        //aanmaken van de fragments voor elke help stap based on gamemode
        switch (mGameMode) {
            case EXTRA_GAMEMODE_SMASHIT:
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getResources().getString(R.string.smashit_info_text_4), R.drawable.smashit_info_anim_1));
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getString(R.string.smashit_info_anim_text_2), R.drawable.smashit_info_anim_2));
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getString(R.string.smashit_info_anim_text_3), R.drawable.smashit_info_anim_3));
                break;
            case EXTRA_GAMEMODE_ZENIT:
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getString(R.string.zenit_info_anim_text_1), R.drawable.zenit_info_anim_1));
                break;
            case EXTRA_GAMEMODE_MEMORIT:
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getString(R.string.memorit_info_anim_text_1), R.drawable.memorit_info_anim_1));
                mFragmentList.add(InfoGameHelpAnimationFragment.newInstance(getString(R.string.memorit_info_anim_text_2), R.drawable.memorit_info_anim_2));
                break;
        }

        // Instantiate a ViewPager and a PagerAdapter.
        /*
          The pager widget, which handles animation and allows swiping horizontally to access previous
          and next wizard steps.
         */
        ViewPager mPager = (ViewPager) view.findViewById(R.id.info_game_help_pager);
        /*
         The pager adapter, which provides the pages to the view pager widget.
         */
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), mFragmentList);
        mPager.setAdapter(mPagerAdapter);

        if(mFragmentList.size() > 1) {
            // linken  van tabs met viewpager
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.info_game_help_dots);
            tabLayout.setupWithViewPager(mPager, true);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //make dialog have full width
        if(getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.info_game_help_height));
    }
}
