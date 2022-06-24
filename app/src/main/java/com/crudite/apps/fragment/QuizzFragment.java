package com.crudite.apps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.crudite.apps.R;
import com.crudite.apps.adapter.ListMenuAdapter;
import com.crudite.apps.models.MenuItem;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.ArrayList;


/**
 * Created by root on 27/07/18.
 */

public class QuizzFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ListMenuAdapter adapter;

    public QuizzFragment() {
        // Required empty public constructor
    }

    public static QuizzFragment newInstance() {
        QuizzFragment fragment = new QuizzFragment();
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
        rootView = inflater.inflate(R.layout.fragment_bon_plan, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.quizz_thematique);

        ImageView back = (ImageView) rootView.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
            }
        });
        return rootView;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
    }
}
