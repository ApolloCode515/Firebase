package com.spark.swarajyabiz.MyFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spark.swarajyabiz.R;

public class MembersFragment extends Fragment {


    public static MembersFragment newInstance(String commId) {
        MembersFragment fragment = new MembersFragment();
        Bundle args = new Bundle();
        args.putString("CommID", commId);
        fragment.setArguments(args);
        return new MembersFragment();
    }
    public MembersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_members, container, false);



        return view;
    }

    public void getData(){

    }
}