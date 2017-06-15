package be.howest.nmct.targit.views.settings;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import be.howest.nmct.targit.R;

import static be.howest.nmct.targit.Constants.EXTRA_HELP_IMAGE;
import static be.howest.nmct.targit.Constants.EXTRA_HELP_TEXT;


public class HelpStapFragment extends Fragment {

    //nieuw instantie aanmaken van fragment
    //parameters: helpTekst, helpImageId
    public static HelpStapFragment newInstance(String helpTekst, int helpImageId) {
        HelpStapFragment myFragment = new HelpStapFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_HELP_TEXT, helpTekst);
        args.putInt(EXTRA_HELP_IMAGE, helpImageId);
        myFragment.setArguments(args);

        return myFragment;
    }

    public HelpStapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_stap, container, false);

        //ingeselde waarden ophalen en in view tonen
        if (getArguments() != null) {
            Bundle args = getArguments();

            TextView tekst = (TextView) v.findViewById(R.id.help_stap_tekst);
            tekst.setText(args.getString(EXTRA_HELP_TEXT));

            ImageView img = (ImageView) v.findViewById(R.id.help_stap_image);
            img.setImageResource(args.getInt(EXTRA_HELP_IMAGE));
        }


        return v;
    }

}
