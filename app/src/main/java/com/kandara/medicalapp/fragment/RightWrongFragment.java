package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kandara.medicalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RightWrongFragment extends Fragment {

    boolean isRight;

    public void setRight(boolean right) {
        isRight = right;
    }

    public RightWrongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_right_wrong, container, false);
        AppCompatImageView rightWrong=view.findViewById(R.id.rightWrong);
        if(isRight){
            rightWrong.setImageResource(R.drawable.ic_correct);
        }else{
            rightWrong.setImageResource(R.drawable.ic_wrong);
        }
        return view;
    }

}
