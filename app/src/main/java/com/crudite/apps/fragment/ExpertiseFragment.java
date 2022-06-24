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

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;


/**
 * Created by root on 27/07/18.
 */

public class ExpertiseFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ListMenuAdapter adapter;

    public ExpertiseFragment() {
        // Required empty public constructor
    }

    public static ExpertiseFragment newInstance() {
        ExpertiseFragment fragment = new ExpertiseFragment();
        return fragment;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_expertise, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.champs_expertise);
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
        HtmlTextView ambassadeurText = (HtmlTextView) rootView.findViewById(R.id.ambassador_text);
        ambassadeurText.setHtml(getString(R.string.expertise));
        return rootView;
    }
}
