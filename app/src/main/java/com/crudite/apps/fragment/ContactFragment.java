package com.crudite.apps.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.crudite.apps.adapter.ListMenuAdapter;
import com.crudite.apps.models.MenuItem;
import com.crudite.apps.utilitaires.Standard;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.ArrayList;


/**
 * Created by root on 27/07/18.
 */

public class ContactFragment extends Fragment {
    View rootView;

    private TextView telval, emailval, facebookval, twitterval, instagramval;
    private TextView adresseval;
    private EditText objet;
    private EditText message;
    private TextView linkedinval;
    private TextView youtubeval;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
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
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(getString(R.string.nous_joindre));
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


        telval = (TextView) rootView.findViewById(R.id.telval);
        adresseval = (TextView) rootView.findViewById(R.id.adresseval);
        emailval = (TextView) rootView.findViewById(R.id.emailval);
        facebookval = (TextView) rootView.findViewById(R.id.facebookval);
        twitterval = (TextView) rootView.findViewById(R.id.twitterval);
        linkedinval = (TextView) rootView.findViewById(R.id.linkedinval);
        youtubeval = (TextView) rootView.findViewById(R.id.youtubeval);
        instagramval = (TextView) rootView.findViewById(R.id.instagramval);
        objet = (EditText) rootView.findViewById(R.id.objet);
        message = (EditText) rootView.findViewById(R.id.message);

        TextView icon_face = (TextView) rootView.findViewById(R.id.facebook);
        TextView icon_twitter = (TextView) rootView.findViewById(R.id.twitter);
        TextView icon_instagram = (TextView) rootView.findViewById(R.id.instagram);
        TextView icon_telephone = (TextView) rootView.findViewById(R.id.icon_phone);
        TextView icon_email = (TextView) rootView.findViewById(R.id.icon_email);
        TextView icon_adresse = (TextView) rootView.findViewById(R.id.icon_adresse);
        TextView linkedin = (TextView) rootView.findViewById(R.id.linkedin);
        TextView youtube = (TextView) rootView.findViewById(R.id.youtube);

        rootView.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        Typeface font = Standard.getIconFont(getContext());
        icon_face.setTypeface(font);
        icon_twitter.setTypeface(font);
        icon_instagram.setTypeface(font);
        icon_telephone.setTypeface(font);
        icon_email.setTypeface(font);
        icon_adresse.setTypeface(font);
        linkedin.setTypeface(font);
        youtube.setTypeface(font);

        icon_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Crudites/?__mref=message_bubble"));
                startActivity(browserIntent);
            }
        });

        icon_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/crudites"));
                startActivity(browserIntent);
            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://linkedin.com/crudites"));
                startActivity(browserIntent);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/crudites"));
                startActivity(browserIntent);

            }
        });

        icon_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/crudites/"));
                startActivity(browserIntent);
            }
        });
        setValues();
        return rootView;
    }
    public void setValues(){
        telval.setText("696440416");
        adresseval.setText("Bonapriso Douala");
        emailval.setText("contact@crudites.com");
        facebookval.setText("crudites");
        twitterval.setText("@crudites");
        linkedinval.setText("Crudites");
        instagramval.setText("@crudites");
        youtubeval.setText("Crudites");
    }
    public void send(){
        if(objet.getText().toString().length()==0){
            objet.setError("Champs réquis");
        }
        else if(message.getText().toString().length()==0){
            message.setError("Champs réquis");
        }
        else {
            String[] TO = {"contact@crudites.com"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, objet.getText().toString());
            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message.getText().toString()));

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "Vous n'avez pas de client email installé", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
