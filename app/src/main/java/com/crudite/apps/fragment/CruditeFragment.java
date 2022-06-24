package com.crudite.apps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.crudite.apps.R;
import com.crudite.apps.adapter.ListMenuAdapter;
import com.crudite.apps.controller.RecyclerItemClickListener;
import com.crudite.apps.models.MenuItem;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.ArrayList;


/**
 * Created by root on 27/07/18.
 */

public class CruditeFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ListMenuAdapter adapter;

    public CruditeFragment() {
        // Required empty public constructor
    }

    public static CruditeFragment newInstance() {
        CruditeFragment fragment = new CruditeFragment();
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
        rootView = inflater.inflate(R.layout.fragment_crudites, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.app_name));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ListMenuAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadData();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                switch (list.get(position).stringResId){
                    case R.string.fa_phone:
                        ft.replace(R.id.frame_layout,ContactFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.string.fa_certificate:
                        ft.replace(R.id.frame_layout,ExpertiseFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.string.fa_exclamation_circle:
                        ft.replace(R.id.frame_layout,UniversCruditeFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.string.fa_gift:
                        ft.replace(R.id.frame_layout,BonPlanFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.string.fa_gamepad:
                        ft.replace(R.id.frame_layout,QuizzFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case R.string.fa_hand_o_up:
                        ft.replace(R.id.frame_layout,SuggestionFragment.newInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                }
            }
        }));

        return rootView;
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
    }
    public void loadData(){
        list.removeAll(list);
        //list.add("Modifier mon profil");
        MenuItem menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.univers_crudite);
        menuItem.stringResId = R.string.fa_exclamation_circle;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.champs_expertise);
        menuItem.stringResId = R.string.fa_certificate;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.bon_plan);
        menuItem.stringResId = R.string.fa_gift;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.quizz_thematique);
        menuItem.stringResId = R.string.fa_gamepad;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.newsletter);
        menuItem.stringResId = R.string.fa_envelope;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.boite_suggestion);
        menuItem.stringResId = R.string.fa_hand_o_up;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = getString(R.string.nous_joindre);
        menuItem.stringResId = R.string.fa_phone;
        list.add(menuItem);

        adapter.notifyDataSetChanged();
    }
}
