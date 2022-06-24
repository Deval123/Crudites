package com.crudite.apps.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crudite.apps.MainActivity;
import com.crudite.apps.R;
import com.crudite.apps.StartChooseActivity;
import com.crudite.apps.adapter.ListMenuAdapter;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.controller.RecyclerItemClickListener;
import com.crudite.apps.models.MenuItem;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by root on 27/07/18.
 */

public class ProfileFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ListMenuAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        rootView = inflater.inflate(R.layout.fragment_profil, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.mon_profil);

        TextView email = (TextView) rootView.findViewById(R.id.email);
        TextView nom = (TextView) rootView.findViewById(R.id.nom_client);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), ProfilActivity.class));
            }
        });
        CircleImageView img = (CircleImageView) rootView.findViewById(R.id.circleViewProfile);

        nom.setText(AppController.getInstance().getPrefManager().pref.getString("nom", "")+" "+AppController.getInstance().getPrefManager().pref.getString("prenom", ""));
        email.setText(AppController.getInstance().getPrefManager().pref.getString("email", ""));
        if(AppController.getInstance().getPrefManager().pref.getString("photo_user", "").length()==0)
            img.setImageResource(R.drawable.profile);
        else
            ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/utilisateur/"+AppController.getInstance().getPrefManager().pref.getString("photo_user", ""), img);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ListMenuAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadData();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                switch (list.get(position).stringResId){
                    case R.string.fa_power_off:
                        SharedPreferences.Editor e = AppController.getInstance().getPrefManager().pref.edit();
                        e.clear();
                        e.commit();
                        e.putBoolean("first_use", true);
                        e.commit();
                        startActivity(new Intent(getActivity(), StartChooseActivity.class));
                        getActivity().finish();
                        break;
                    case R.string.fa_share_alt:
                        try {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "Crudités");
                            String sAux = "\nEspace de dégustation\n";
                            sAux = sAux + "https://play.google.com/store/apps/details?id=com.crudit.apps";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "Choisir"));
                        } catch(Exception eo) {
                            //e.toString();
                        }
                        break;
                }
            }
        }));

        return rootView;
    }
    public void loadData(){
        list.removeAll(list);
        MenuItem menuItem = new MenuItem();
        menuItem.intitule = "Mes commandes";
        menuItem.stringResId = R.string.fa_cutlery;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Mon abonnement";
        menuItem.stringResId = R.string.fa_star;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Notifications";
        menuItem.stringResId = R.string.fa_bell;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Paramètres";
        menuItem.stringResId = R.string.fa_cog;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Aide";
        menuItem.stringResId = R.string.fa_info;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Recommander à un ami";
        menuItem.stringResId = R.string.fa_share_alt;
        list.add(menuItem);
        menuItem = new MenuItem();
        menuItem.intitule = "Déconnexion";
        menuItem.stringResId = R.string.fa_power_off;
        list.add(menuItem);

        adapter.notifyDataSetChanged();
    }
}
