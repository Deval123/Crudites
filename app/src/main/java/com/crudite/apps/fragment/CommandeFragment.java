package com.crudite.apps.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.crudite.apps.R;
import com.crudite.apps.entite.Contenu;
import com.crudite.apps.utilitaires.KKViewPager;
import com.crudite.apps.utilitaires.PagerContainer;
import com.crudite.apps.utilitaires.Standard;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;

import java.util.ArrayList;


public class CommandeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PagerContainer mContainer;
    View rootView;
    DotsIndicator dotsIndicator;
    ProgressBar progressBar;
    public CommandeFragment() {
        // Required empty public constructor
    }

    public static CommandeFragment newInstance() {
        CommandeFragment fragment = new CommandeFragment();
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
        rootView = inflater.inflate(R.layout.fragment_commander_home, container, false);
        dotsIndicator = rootView.findViewById(R.id.dots_indicator);
        progressBar = rootView.findViewById(R.id.progress);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.pack_crudite);
        //mContainer = (PagerContainer) rootView.findViewById(R.id.pager_container);

        /*ViewPager pager = mContainer.getViewPager();
        DotIndicatorPagerAdapter adapter = new DotIndicatorPagerAdapter();
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages
        pager.setPageMargin(100);

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);*/
        progressBar.setVisibility(View.VISIBLE);
        (new Contenu()).load(null, true)
                .done(new DoneCallback<ArrayList<?>>() {
                    @Override
                    public void onDone(ArrayList<?> result) {
                        progressBar.setVisibility(View.GONE);
                        init((ArrayList<Contenu>)result);
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
        return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
    }
    public void init(ArrayList<Contenu> cates){
        CategorieAdapter adapter = new CategorieAdapter(cates);
        adapter.remote = true;
        KKViewPager viewPager = (KKViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setAnimationEnabled(true);
        viewPager.setFadeEnabled(true);
        viewPager.setFadeFactor(0.6f);
        dotsIndicator.setViewPager(viewPager);
    }

    public class CategorieAdapter extends PagerAdapter {
        private final ArrayList<Contenu> items;
        public boolean remote = false;
        public CategorieAdapter(ArrayList<Contenu> items){
            this.items = items;
        }
        @NonNull
        @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View item = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.categorie_page, container, false);
            ImageView image = (ImageView) item.findViewById(R.id.image);
            Contenu contenu = items.get(position);
            if(!remote)
                image.setImageResource(contenu.imageRessource);
            else
                ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/contenu/"+contenu.image, image);
            TextView text = (TextView) item.findViewById(R.id.text);
            TextView subtext = (TextView) item.findViewById(R.id.subtext);
            text.setText(contenu.nom_categorie);
            subtext.setText(contenu.subtext);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_layout,CategorieFragment.newInstance(contenu));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            container.addView(item);
            return item;
        }

        @Override public int getCount() {
            return items.size();
        }

        @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        private class Item {
            private final int color;

            private Item(int color) {
                this.color = color;
            }
        }
    }
}
