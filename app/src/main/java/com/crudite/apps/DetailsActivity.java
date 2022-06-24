package com.crudite.apps;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.crudite.apps.utilitaires.DecodeBitmapTask;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;


public class DetailsActivity extends AppCompatActivity implements DecodeBitmapTask.Listener {

    public static final String BUNDLE_IMAGE_ID = "BUNDLE_IMAGE_ID";

    private ImageView imageView;
    private DecodeBitmapTask decodeBitmapTask;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String smallResId = getIntent().getStringExtra(BUNDLE_IMAGE_ID);
        if (smallResId == null) {
            finish();
            return;
        }

        imageView = (ImageView)findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/produit/"+smallResId, imageView);
        //imageView.setImageResource(smallResId);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.super.onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //loadFullSizeBitmap(smallResId);
        } else {
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {

                private boolean isClosing = false;

                @Override public void onTransitionPause(Transition transition) {}
                @Override public void onTransitionResume(Transition transition) {}
                @Override public void onTransitionCancel(Transition transition) {}

                @Override public void onTransitionStart(Transition transition) {
                    if (isClosing) {
                        addCardCorners();
                    }
                }

                @Override public void onTransitionEnd(Transition transition) {
                    if (!isClosing) {
                        isClosing = true;

                        removeCardCorners();
                        //loadFullSizeBitmap(smallResId);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && decodeBitmapTask != null) {
            decodeBitmapTask.cancel(true);
        }
    }

    private void addCardCorners() {
        final CardView cardView = (CardView) findViewById(R.id.card);
        cardView.setRadius(25f);
    }

    private void removeCardCorners() {
        final CardView cardView = (CardView)findViewById(R.id.card);
        ObjectAnimator.ofFloat(cardView, "radius", 0f).setDuration(50).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadFullSizeBitmap(int smallResId) {
        int bigResId;
        switch (smallResId) {
            case R.drawable.p1: bigResId = R.drawable.p1; break;
            case R.drawable.p2: bigResId = R.drawable.p2; break;
            case R.drawable.p3: bigResId = R.drawable.p3; break;
            case R.drawable.p4: bigResId = R.drawable.p4; break;
            case R.drawable.p5: bigResId = R.drawable.p5; break;
            default: bigResId = R.drawable.p1;
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        final int w = metrics.widthPixels;
        final int h = metrics.heightPixels;

        decodeBitmapTask = new DecodeBitmapTask(getResources(), bigResId, w, h, this);
        decodeBitmapTask.execute();
    }

    @Override
    public void onPostExecuted(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
