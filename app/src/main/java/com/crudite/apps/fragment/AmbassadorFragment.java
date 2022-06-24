package com.crudite.apps.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codesgood.views.JustifiedTextView;
import com.crudite.apps.R;
import com.crudite.apps.StartChooseActivity;
import com.crudite.apps.adapter.ListMenuAdapter;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.controller.RecyclerItemClickListener;
import com.crudite.apps.models.MenuItem;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by root on 27/07/18.
 */

public class AmbassadorFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    ArrayList<MenuItem> list = new ArrayList<MenuItem>();
    ListMenuAdapter adapter;

    public AmbassadorFragment() {
        // Required empty public constructor
    }

    public static AmbassadorFragment newInstance() {
        AmbassadorFragment fragment = new AmbassadorFragment();
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
        rootView = inflater.inflate(R.layout.fragment_ambassadeur, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(R.string.the_ambassador);

        JustifiedTextView ambassadeurText = (JustifiedTextView) rootView.findViewById(R.id.ambassador_text);
        ambassadeurText.setText(Html.fromHtml(getString(R.string.ambassadeur_text)));
        return rootView;
    }
}
