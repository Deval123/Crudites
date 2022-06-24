package com.crudite.apps.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.crudite.apps.R;
import com.crudite.apps.adapter.PlaylistAdapter;
import com.crudite.apps.adapter.UniverCruditeAdapter;
import com.crudite.apps.controller.RecyclerItemClickListener;
import com.crudite.apps.entite.Playlist;
import com.crudite.apps.entite.UniversCruditeItem;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

import java.util.ArrayList;


public class UniversCruditeFragment extends Fragment {
    private ListView recyclerView;
    View rootView;
    ProgressBar progressBar;
    UniverCruditeAdapter adapter;
    ArrayList<UniversCruditeItem> list = new ArrayList<>();
    public UniversCruditeFragment() {
        // Required empty public constructor
    }

    public static UniversCruditeFragment newInstance() {
        UniversCruditeFragment fragment = new UniversCruditeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_page_listview, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.univers_crudite);
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
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = (ListView) rootView.findViewById(R.id.recycler_view);
        adapter = new UniverCruditeAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        (new UniversCruditeItem()).load(null, true)
                .done(new DoneCallback<ArrayList<?>>() {
                    @Override
                    public void onDone(ArrayList<?> result) {
                        progressBar.setVisibility(View.GONE);
                        init((ArrayList<UniversCruditeItem>)result);
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

        return rootView;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
    }
    public void init(ArrayList<UniversCruditeItem> cates){
        list.removeAll(list);
        list.addAll(cates);
        adapter.notifyDataSetChanged();
    }
}
