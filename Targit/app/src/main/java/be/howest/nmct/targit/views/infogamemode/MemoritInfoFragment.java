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

public class MemoritInfoFragment extends Fragment {

    private OnMemoritInfoListener mListener;

    public MemoritInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_info, container, false);

        view.findViewById(R.id.memorit_info_button_play_easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(GameActivity.EXTRA_LIVES_MANY);
            }
        });
        view.findViewById(R.id.memorit_info_button_play_medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(GameActivity.EXTRA_LIVES_MEDIUM);
            }
        });
        view.findViewById(R.id.memorit_info_button_play_hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.playMemorit(GameActivity.EXTRA_LIVES_FEW);
            }
        });

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        Button btnSnel = (Button)view.findViewById(R.id.memorit_info_button_play_hard);
        btnSnel.setTypeface(font);
        Button btnMatig = (Button)view.findViewById(R.id.memorit_info_button_play_medium);
        btnMatig.setTypeface(font);
        Button btnTraag = (Button)view.findViewById(R.id.memorit_info_button_play_easy);
        btnTraag.setTypeface(font);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMemoritInfoListener) {
            mListener = (OnMemoritInfoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMemoritInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMemoritInfoListener {
        void playMemorit(int cmdLives);
    }
}
