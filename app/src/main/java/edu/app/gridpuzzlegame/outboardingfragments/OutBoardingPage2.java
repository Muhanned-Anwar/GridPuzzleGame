package edu.app.gridpuzzlegame.outboardingfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.app.gridpuzzlegame.R;

public class OutBoardingPage2 extends Fragment {

    public OutBoardingPage2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_out_boarding_page2, container, false);
    }
}