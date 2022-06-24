package com.crudite.apps.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.error.VolleyError;
import com.crudite.apps.DetailsActivity;
import com.crudite.apps.MainActivity;
import com.crudite.apps.R;
import com.crudite.apps.adapter.SliderAdapter;
import com.crudite.apps.entite.Contenu;
import com.crudite.apps.entite.Produit;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.labo.kaji.fragmentanimations.PushPullAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by root on 27/07/18.
 */

public class CategorieFragment extends Fragment {
    private SliderAdapter sliderAdapter;

    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private TextSwitcher temperatureSwitcher;
    private HtmlTextView descriptionsSwitcher;

    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;

    View rootView;
    Contenu contenu;

    public CategorieFragment() {
        // Required empty public constructor
    }
    public CategorieFragment(Contenu contenu) {
        // Required empty public constructor
        this.contenu = contenu;
    }

    public static CategorieFragment newInstance(Contenu contenu) {
        CategorieFragment fragment = new CategorieFragment(contenu);
        return fragment;
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //if (enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        /*} else {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
        }*/
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_categorie, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(contenu.nom_categorie);
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        subtitle.setText(R.string.pack_crudite);
        ImageView back = (ImageView) rootView.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        temperatureSwitcher = (TextSwitcher) rootView.findViewById(R.id.ts_temperature);
        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        temperatureSwitcher.setCurrentText(getString(R.string.commander));

        temperatureSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.commander(contenu, (MainActivity)getActivity());
            }
        });
        //subtitle.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
            }
        });
        HashMap<String, String> params = new HashMap<>();
        params.put("idcontenu", contenu.id_categorie+"");
        (new Produit()).load(params, true)
                .done(new DoneCallback<ArrayList<?>>() {
                    @Override
                    public void onDone(ArrayList<?> result) {
                        init((ArrayList<Produit>)result);
                    }
                })
                .fail(new AndroidFailCallback<VolleyError>() {
                    @Override
                    public void onFail(VolleyError result) {
                        Toast.makeText(getActivity(), "Vérifiez votre connexion internet et réessayez", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public AndroidExecutionScope getExecutionScope() {
                        return null;
                    }
                });


        return rootView;
    }
    public ArrayList<Produit> produitArrayList;
    public void init(ArrayList<Produit> produitArrayList){
        if(produitArrayList.size()>0) {
            this.produitArrayList = produitArrayList;
            sliderAdapter = new SliderAdapter(produitArrayList, 20, new OnCardClickListener());

            initRecyclerView();
            initCountryText();
            initSwitchers();
        }
        else {
            descriptionsSwitcher = (HtmlTextView) rootView.findViewById(R.id.ts_description);
            descriptionsSwitcher.setHtml(contenu.description);

            rootView.findViewById(R.id.recycler_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.tv_country_1).setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.tv_country_2).setVisibility(View.INVISIBLE);
        }
    }
    private void initRecyclerView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }
    private void initSwitchers() {
        descriptionsSwitcher = (HtmlTextView) rootView.findViewById(R.id.ts_description);
        descriptionsSwitcher.setHtml(contenu.description);
    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView = (TextView) rootView.findViewById(R.id.tv_country_1);
        country2TextView = (TextView) rootView.findViewById(R.id.tv_country_2);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        country1TextView.setText(produitArrayList.get(0).nom_produit);
        country2TextView.setAlpha(0f);

        country1TextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans-extrabold.ttf"));
        country2TextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans-extrabold.ttf"));
    }

    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCountryText(produitArrayList.get(pos % produitArrayList.size()).nom_produit, left2right);

        /*temperatureSwitcher.setInAnimation(getContext(), animH[0]);
        temperatureSwitcher.setOutAnimation(getContext(), animH[1]);
        temperatureSwitcher.setText(temperatures[pos % temperatures.length]);

        descriptionsSwitcher.setText(getString(descriptions[pos % descriptions.length]));*/

        currentPosition = pos;
    }

    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(getContext());

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getContext(), styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, produitArrayList.get(activeCardPosition % produitArrayList.size()).image);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity(), sharedView, "shared");
                    startActivity(intent, options.toBundle());
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

}
