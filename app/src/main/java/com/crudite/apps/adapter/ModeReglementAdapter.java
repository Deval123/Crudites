package com.crudite.apps.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crudite.apps.R;
import com.crudite.apps.entite.ModeReglement;
import com.crudite.apps.entite.Playlist;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ModeReglementAdapter extends RecyclerView.Adapter<ModeReglementAdapter.MyViewHolder> {
	private static final int ITEM_VIEW_TYPE_ITEM = 1;
	ArrayList<ModeReglement> listItem = new ArrayList<ModeReglement>();
	Context context;
	public int selection = -1;
	public boolean setDivision = true;
	public ModeReglementAdapter(ArrayList<ModeReglement> listPublicites, Context context) {
		this.listItem = listPublicites;
		this.context = context;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder{
		TextView titre;
		ImageView img_cover;
		View overlay;
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			titre= (TextView) itemView.findViewById(R.id.titre);
			img_cover= (ImageView) itemView.findViewById(R.id.img_cover);
			overlay= (View) itemView.findViewById(R.id.overlay);
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
		holder.titre.setText(listItem.get(position).mode);
		ImageLoader.getInstance().displayImage(Standard.adresse_serveur+"images/paiement/"+listItem.get(position).image, holder.img_cover);
		if(selection==position){
			holder.overlay.setBackgroundColor(context.getResources().getColor(R.color.primary_t));
		}
		else {
			holder.overlay.setBackgroundColor(context.getResources().getColor(R.color.noir_t));
		}
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selection = position;
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reglement_row, parent, false);
		MyViewHolder vHolder = new MyViewHolder(view);
		return vHolder;
	}

}