package com.crudite.apps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crudite.apps.utilitaires.ShimmerFrameLayout;
import com.crudite.apps.utilitaires.Standard;
import com.nineoldandroids.animation.ObjectAnimator;

import in.codeshuffle.typewriterview.TypeWriterView;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences(Standard.MyPREFERENCES, Context.MODE_PRIVATE);
        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    Intent intent;

                    /*final boolean b = sharedpreferences.getBoolean("first_use", false);

                    if (b == false) {
                        startActivity(new Intent(SplashActivity.this, StartChooseActivity.class));
                        finish();
                    } else {*/
                        if (sharedpreferences.getInt("id_utilisateur", 0) == 0)
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        else {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        startActivity(intent);

                        finish();
                    //}


                }
            }
        };
        splash.start();
        ShimmerFrameLayout mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setDuration(5000);
        mShimmerViewContainer.setRepeatMode(ObjectAnimator.REVERSE);
        mShimmerViewContainer.startShimmerAnimation();
        /*TypeWriterView typeWriterView=(TypeWriterView)findViewById(R.id.typeWriterView);

        //Setting each character animation delay
        typeWriterView.setDelay(2000);

        //Setting music effect On/Off
        typeWriterView.setWithMusic(false);

        //Animating Text
        typeWriterView.animateText("BECAUSE A SOUL IS PRICELESS, WE ALWAYS OFFER YOU ‘THE BEST’ FOR A HEALTHY AND FULFILLING LIFE");*/

    }
}
