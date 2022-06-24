package com.crudite.apps.fragment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crudite.apps.R;
import com.crudite.apps.utilitaires.Standard;
import com.labo.kaji.fragmentanimations.MoveAnimation;


/**
 * Created by root on 27/07/18.
 */

public class SuggestionFragment extends Fragment {
    View rootView;
    private EditText objet;
    private EditText message;
    public SuggestionFragment() {
        // Required empty public constructor
    }

    public static SuggestionFragment newInstance() {
        SuggestionFragment fragment = new SuggestionFragment();
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
        rootView = inflater.inflate(R.layout.fragment_suggestion, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(getString(R.string.boite_suggestion));
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

        objet = (EditText) rootView.findViewById(R.id.objet);
        message = (EditText) rootView.findViewById(R.id.message);

        rootView.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        return rootView;
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
    }

    ProgressDialog progress;
    public void send(){
        if(objet.getText().toString().length()==0){
            objet.setError("Champs réquis");
        }
        else if(message.getText().toString().length()==0){
            message.setError("Champs réquis");
        }
        else {
            progress = new ProgressDialog(getActivity());
            progress.setCancelable(false);
            progress.setMessage("Veuillez patienter...");
            progress.show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Succès")
                            .setMessage("Merci pour votre suggestion")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                                        getFragmentManager().popBackStack();
                                        return;
                                    }
                                }
                            })
                            .show();
                }
            }, 2000);

        }
    }
}
