package com.crudite.apps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crudite.apps.R;

/**
 * Created by root on 25/07/18.
 */

public class PagerHomeFragment extends Fragment {
    public PagerHomeFragment() {
        // Required empty public constructor
    }
    public PagerHomeFragment(Fragment fr) {
        // Required empty public constructor
        this.fragment = fr;
    }
    Fragment fragment;
    public static PagerHomeFragment newInstance(Fragment fr) {
        PagerHomeFragment fragment = new PagerHomeFragment(fr);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frame_layout, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null);
        ft.commit();
        return rootView;
    }

}
