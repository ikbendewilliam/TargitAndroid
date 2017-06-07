package be.howest.nmct.targit.views.ingame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.targit.R;
import be.howest.nmct.targit.views.infogamemode.MemoritInfoFragment;

public class MemoritGameFragment extends Fragment {

    private OnMemoritGameListener mListener;

    public MemoritGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memorit_game, container, false);

        view.findViewById(R.id.ingame_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.stopGame(GameActivity.EXTRA_GAME_MEMORIT, 0);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMemoritGameListener) {
            mListener = (OnMemoritGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMemoritGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMemoritGameListener {
        void stopGame(String gameMode, int score);
    }
}
