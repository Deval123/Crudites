package com.crudite.apps.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.crudite.apps.R;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.controller.RequestManager;
import com.crudite.apps.models.MenuItem;
import com.crudite.apps.utilitaires.Standard;

import org.jdeferred.DoneCallback;


public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.MyViewHolder> {
	  private static final int ITEM_VIEW_TYPE_ITEM = 1;
	  ArrayList<MenuItem> listItem = new ArrayList<MenuItem>();
	  Context context;
	public boolean setDivision = true;
	 public ListMenuAdapter(ArrayList<MenuItem> listPublicites, Context context) {
		 this.listItem = listPublicites;
		 this.context = context;
	}
	  
	public class MyViewHolder extends RecyclerView.ViewHolder{
		private ImageView chevron;
		TextView icon, item;
		View view_inter;
		Switch newletter;
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			icon= (TextView) itemView.findViewById(R.id.icon_menu);
			view_inter= (View) itemView.findViewById(R.id.view_inter);
			item= (TextView) itemView.findViewById(R.id.textViewName_row);
			newletter= (Switch) itemView.findViewById(R.id.newletter);
			chevron= (ImageView) itemView.findViewById(R.id.chevron);
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
		holder.item.setText(listItem.get(position).intitule);
        holder.icon.setText(listItem.get(position).stringResId);
        holder.icon.setTypeface(Standard.getIconFont(context));
		if(setDivision)
			if(position==2 || position == 4 || position >=6){
				holder.view_inter.setVisibility(View.VISIBLE);
			}
			else {
				holder.view_inter.setVisibility(View.GONE);
			}
		else
			holder.view_inter.setVisibility(View.GONE);

		if(listItem.get(position).stringResId==R.string.fa_envelope){
			holder.chevron.setVisibility(View.GONE);
			holder.newletter.setVisibility(View.VISIBLE);
			holder.newletter.setChecked(AppController.getInstance().getPrefManager().pref.getBoolean("newletter", false));
			holder.newletter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
					HashMap<String, String> params = new HashMap<>();
					params.put("state", (isChecked ? 1 : 0)+"");
					RequestManager.post("Apk/newletter", params).done(new DoneCallback<String>() {
						@Override
						public void onDone(String result) {
							AppController.getInstance().getPrefManager().editor.putBoolean("newletter", isChecked);
							AppController.getInstance().getPrefManager().editor.commit();
						}
					});
				}
			});
		}
		else {
			holder.chevron.setVisibility(View.VISIBLE);
			holder.newletter.setVisibility(View.GONE);
		}
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu_option, parent, false);
		MyViewHolder vHolder = new MyViewHolder(view);
		return vHolder;
	}

}
