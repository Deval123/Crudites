package com.crudite.apps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.crudite.apps.entite.Contenu;
import com.crudite.apps.fragment.AmbassadorFragment;
import com.crudite.apps.fragment.CommandeFragment;
import com.crudite.apps.fragment.CommanderLayout;
import com.crudite.apps.fragment.PagerHomeFragment;
import com.crudite.apps.fragment.CruditeFragment;
import com.crudite.apps.fragment.PlaylistFragment;
import com.crudite.apps.fragment.ProfileFragment;
import com.crudite.apps.utilitaires.AddPictureLayout;
import com.crudite.apps.utilitaires.NonSwipeableViewPager;
import com.crudite.apps.utilitaires.ViewPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.theartofdev.edmodo.cropper.CropImage;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private NonSwipeableViewPager viewpager;
    private GoogleApiClient mGoogleApiClient;
    public static int PLACE_PICKER_REQUEST = 1;
    public SlidingUpPanelLayout mLayout;
    private static CommanderLayout mCommanderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.crudites, R.mipmap.ic_launcher, R.color.colorOr);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.playlist, R.mipmap.ic_playlist, R.color.color_tab3);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.commander, R.mipmap.ic_commander, R.color.color_tab1);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.ambassadeur, R.mipmap.ic_ambassadeur, R.color.color_tab5);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.profil, R.mipmap.ic_profile, R.color.colorPrimary);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#455C65"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setCurrentItem(2);
        bottomNavigation.setColoredModeColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                viewpager.setCurrentItem(position);
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
        viewpager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PagerHomeFragment.newInstance(CruditeFragment.newInstance()), "Acceuil");
        adapter.addFragment(PagerHomeFragment.newInstance(PlaylistFragment.newInstance()), "Acceuil");
        adapter.addFragment(PagerHomeFragment.newInstance(CommandeFragment.newInstance()), "Acceuil");
        adapter.addFragment(PagerHomeFragment.newInstance(AmbassadorFragment.newInstance()), "Acceuil");
        adapter.addFragment(PagerHomeFragment.newInstance(ProfileFragment.newInstance()), "Acceuil");
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(2);
    }
    public static void commander(Contenu contenu, MainActivity mainActivity){
        mCommanderFragment = new CommanderLayout(mainActivity);
        mCommanderFragment.setContenu(contenu);
        mCommanderFragment.setLayoutReceiver(mainActivity.mLayout);
        mainActivity.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        mCommanderFragment.load();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            StringBuilder stBuilder = new StringBuilder();
            String placename = String.format("%s", place.getName());
            String latitude = String.valueOf(place.getLatLng().latitude);
            String longitude = String.valueOf(place.getLatLng().longitude);
            String address = String.format("%s", place.getAddress());
            //stBuilder.append("Name: ");
            stBuilder.append(placename);
            /*stBuilder.append("\n");
            stBuilder.append("Latitude: ");
            stBuilder.append(latitude);
            stBuilder.append("\n");
            stBuilder.append("Logitude: ");
            stBuilder.append(longitude);
            stBuilder.append("\n");
            stBuilder.append("Address: ");
            stBuilder.append(address);*/
            Toast.makeText(getApplicationContext(), "Select"+placename, Toast.LENGTH_SHORT).show();
            CommanderLayout.Lieu2Fragment.lieu.setText(stBuilder.toString());
        }
    }
}
