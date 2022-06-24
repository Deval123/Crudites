package com.crudite.apps.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.crudite.apps.R;
import com.crudite.apps.entite.Playlist;
import com.crudite.apps.models.MenuItem;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;


public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
	private static final int ITEM_VIEW_TYPE_ITEM = 1;
	ArrayList<Playlist> listItem = new ArrayList<Playlist>();
	Context context;
	public boolean setDivision = true;
	public PlaylistAdapter(ArrayList<Playlist> listPublicites, Context context) {
		this.listItem = listPublicites;
		this.context = context;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder{
		TextView titre;
		ImageView img_cover;
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			titre= (TextView) itemView.findViewById(R.id.titre);
			img_cover= (ImageView) itemView.findViewById(R.id.img_cover);
		}
	}
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return ITEM_VIEW_TYPE_ITEM;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		// TODO Auto-generated method stub
		holder.titre.setText(listItem.get(position).titre);
		ImageLoader.getInstance().displayImage("http://img.youtube.com/vi/"+listItem.get(position).key+"/0.jpg", holder.img_cover);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_row, parent, false);
		MyViewHolder vHolder = new MyViewHolder(view);
		return vHolder;
	}

}