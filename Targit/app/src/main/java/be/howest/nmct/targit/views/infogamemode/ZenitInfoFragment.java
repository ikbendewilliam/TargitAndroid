package be.howest.nmct.targit.views.infogamemode;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.ingame.GameActivity;

public class ZenitInfoFragment extends Fragment {

    private OnZenitInfoListener mListener;

    public ZenitInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zenit_info, container, false);

        view.findViewById(R.id.zenit_info_button_play_short).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(GameActivity.EXTRA_DURATION_SHORT);
            }
        });
        view.findViewById(R.id.zenit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(GameActivity.EXTRA_DURATION_MEDIUM);
            }
        });
        view.findViewById(R.id.zenit_info_button_play_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playZenit(GameActivity.EXTRA_DURATION_LONG);
            }
        });

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        Button btnSnel = (Button)view.findViewById(R.id.zenit_info_button_play_long);
        btnSnel.setTypeface(font);
        Button btnMatig = (Button)view.findViewById(R.id.zenit_info_button_play_medium);
        btnMatig.setTypeface(font);
        Button btnTraag = (Button)view.findViewById(R.id.zenit_info_button_play_short);
        btnTraag.setTypeface(font);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnZenitInfoListener) {
            mListener = (OnZenitInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnZenitInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnZenitInfoListener {
        void playZenit(int cmdDuration);
    }
}
