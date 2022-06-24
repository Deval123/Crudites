package com.crudite.apps;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.crudite.apps.utilitaires.KenBurnsView;
import com.crudite.apps.utilitaires.LoopViewPager;

public class StartChooseActivity extends AppCompatActivity {
	private AutoCompleteTextView atvPlaces;
	private static final String LOG_TAG = "Crudites";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_choose);
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StartChooseActivity.this, VerificationActivity.class));
			}
		});
		findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StartChooseActivity.this, RegisterActivity.class));
			}
		});
		initializeKenBurnsView();
		
	}
	
	private void initializeKenBurnsView() {
        // KenBurnsView
        final KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.ken_burns_view);
        kenBurnsView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        kenBurnsView.setSwapMs(10000);
        kenBurnsView.setFadeInOutMs(2000);

        // ResourceIDs
        List<Integer> resourceIDs = Arrays.asList(new Integer[]{
									                R.drawable.bg,
									                R.drawable.bg_2,
									                R.drawable.bg_3,
									                R.drawable.sample1
										        });
        kenBurnsView.loadResourceIDs(resourceIDs);

        // LoopViewListener
        LoopViewPager.LoopViewPagerListener listener = new LoopViewPager.LoopViewPagerListener() {
            @Override
            public View OnInstantiateItem(int page) {
                TextView counterText = new TextView(getApplicationContext());
                counterText.setText("");
                return counterText;
            }

            @Override
            public void onPageScroll(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                kenBurnsView.forceSelected(position);
            }

            @Override
            public void onPageScrollChanged(int page) {
            }
        };

        // LoopView
        LoopViewPager loopViewPager = new LoopViewPager(this, resourceIDs.size(), listener);

        FrameLayout viewPagerFrame = (FrameLayout) findViewById(R.id.view_pager_frame);
        viewPagerFrame.addView(loopViewPager);

        kenBurnsView.setPager(loopViewPager);
    }
}
