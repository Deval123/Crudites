package com.crudite.apps.fragment;

import com.crudite.apps.R;
import com.crudite.apps.entite.Playlist;
import com.crudite.apps.utilitaires.Standard;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class VideoDetailFragment extends DialogFragment implements
		YouTubePlayer.OnInitializedListener{
	
	private PopupWindow popWindow;
	private Context context;
	YouTubePlayer player;
	public VideoDetailFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	Playlist playlist;
	public static View inflatedView;
	private YouTubePlayerSupportFragment youTubeView;
	public VideoDetailFragment(Context context, Playlist playlist){
		this.context = context;
		this.playlist = playlist;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (inflatedView != null) {
			ViewGroup parent = (ViewGroup) inflatedView.getParent();
			if (parent != null) {
				parent.removeView(inflatedView);
			}
		}
		inflatedView = inflater.inflate(R.layout.detail_video, null,false);

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inflatedView.findViewById(R.id.popup_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	dismiss();
            }
        });
        ImageView img_sondage = (ImageView) inflatedView.findViewById(R.id.image);

		inflatedView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		TextView titre = (TextView) inflatedView.findViewById(R.id.title);
		titre.setText(playlist.titre);
		youTubeView = YouTubePlayerSupportFragment.newInstance();
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.youtube_view, youTubeView).commit();

		//youTubeView = (YouTubePlayerSupportFragment)((AppCompatActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.youtube_view);

		// Initializing video player with developer key
		youTubeView.initialize(Standard.DEVELOPER_KEY, this);
		return inflatedView;
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		try {
			player.pause();
			getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.youtube_view));
			getActivity().getFragmentManager().beginTransaction().commit();
		}catch(Exception e){
			
		}
	}
	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
										YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(getActivity(), 1).show();
		} else {
			Toast.makeText(getContext(), errorReason.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
										YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {

			// loadVideo() will auto play video
			// Use cueVideo() method, if you don't want to play it automatically
			this.player = player;
			player.loadVideo(playlist.key);

			// Hiding player controls
			player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
		}
	}
	private YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) inflatedView.findViewById(R.id.youtube_view);
	}
}
