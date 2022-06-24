package com.crudite.apps.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.crudite.apps.R;
import com.crudite.apps.StartChooseActivity;
import com.crudite.apps.adapter.PlaylistAdapter;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.controller.RecyclerItemClickListener;
import com.crudite.apps.entite.Playlist;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment {
    private RecyclerView recyclerView;
    View rootView;
    ProgressBar progressBar;
    PlaylistAdapter adapter;
    ArrayList<Playlist> list = new ArrayList<>();
    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
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
        rootView = inflater.inflate(R.layout.fragment_page_recycler, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.playlist);

        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new PlaylistAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        (new Playlist()).load(null, true)
                .done(new DoneCallback<ArrayList<?>>() {
                    @Override
                    public void onDone(ArrayList<?> result) {
                        progressBar.setVisibility(View.GONE);
                        init((ArrayList<Playlist>)result);
                    }
                })
                .fail(new AndroidFailCallback<VolleyError>() {
                    @Override
                    public void onFail(VolleyError result) {
                        Toast.makeText(getActivity(), R.string.verifier_connexion, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public AndroidExecutionScope getExecutionScope() {
                        return null;
                    }
                });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VideoDetailFragment com = new VideoDetailFragment(getContext(), list.get(position));
                com.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
                com.show(getFragmentManager(), null);
            }
        }));
        return rootView;
    }
    public void init(ArrayList<Playlist> cates){
        list.removeAll(list);
        list.addAll(cates);
        adapter.notifyDataSetChanged();
    }
}
