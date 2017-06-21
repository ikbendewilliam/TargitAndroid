package be.howest.nmct.targit.views.infogamemode;


import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import be.howest.nmct.targit.R;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class InfoGameHelpAnimationFragment extends Fragment {

    //the id of the drawable gif
    private int mGifId;

    //the gif object
    private GifDrawable mGifImage;

    //the text to show with the image
    private String mText;

    public InfoGameHelpAnimationFragment() {
        // Required empty public constructor
    }

    public static InfoGameHelpAnimationFragment newInstance(String text, int drawableId) {
        InfoGameHelpAnimationFragment myFragment = new InfoGameHelpAnimationFragment();

        myFragment.mGifId = drawableId;
        myFragment.mText = text;

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_game_help_animation, container, false);

        TextView textView = (TextView) view.findViewById(R.id.game_info_help_tekst);
        textView.setText(mText);

        //instellen fonts
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/BRLNSDB.TTF");
        textView.setTypeface(font);

        try {
            mGifImage = new GifDrawable( getResources(), mGifId );
        } catch (IOException e) {
            e.printStackTrace();
        }

        GifImageView gifImageView = (GifImageView) view.findViewById(R.id.gifImageView);
        gifImageView.setImageDrawable(mGifImage);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGifImage.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGifImage.stop();
        mGifImage.recycle();
    }
}
