package com.crudite.apps.adapter;

import java.util.ArrayList;

import com.crudite.apps.R;
import com.crudite.apps.entite.UniversCruditeItem;
import com.crudite.apps.utilitaires.Standard;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class UniverCruditeAdapter extends BaseAdapter {
	private ArrayList<UniversCruditeItem> list_categorie;
    LayoutInflater inflater;
    Context context;
    ImageLoader imageLoader = ImageLoader.getInstance();
    int selectedPosition = -1;
    public UniverCruditeAdapter(Context context, ArrayList<UniversCruditeItem> results) {
    	list_categorie = results;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_categorie.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list_categorie.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    NumeroViewHolder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.univers_crud_list_item, null);
            holder = new NumeroViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.nom_element);
            holder.comment = (HtmlTextView) convertView.findViewById(R.id.description);
            holder.img = (ImageView) convertView.findViewById(R.id.picture_categorie);
            holder.repas_chevron_plus = (TextView) convertView.findViewById(R.id.repas_chevron_plus);
            holder.top_line = (LinearLayout) convertView.findViewById(R.id.top_line);
            holder.down = (LinearLayout) convertView.findViewById(R.id.down);

            convertView.setTag(holder);

        } else {
            holder = (NumeroViewHolder) convertView.getTag();
        }

    	holder.top_line.setTag(position);
    	holder.top_line.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(holder.down.getVisibility()==View.VISIBLE){
					holder.down.setVisibility(View.GONE);
					String packageName = context.getPackageName();
		            holder.repas_chevron_plus.setRotation(0);
				}else {
					selectedPosition = (Integer)v.getTag();
	                notifyDataSetChanged();
				}
			}
		});
    	
    	
        holder.repas_chevron_plus.setTypeface(Standard.getIconFont(context));

		String packageName = context.getPackageName();
		int resId;
        if(position == selectedPosition){
        	holder.down.setVisibility(View.VISIBLE);

            holder.repas_chevron_plus.setRotation(-90);
        }
        else {
        	holder.down.setVisibility(View.GONE);
            holder.repas_chevron_plus.setRotation(0);
        }
        
        
        String titre = list_categorie.get(position).titre_rubrique;
        holder.titre.setText(titre);
        holder.comment.setHtml(list_categorie.get(position).detail);
        return convertView;
    }
    
    class NumeroViewHolder {
		public TextView repas_chevron_plus;
		TextView titre;
        HtmlTextView comment;
        public ImageView img;
        
        LinearLayout top_line, down;
    	public boolean isRelativeResize = false;
    }
}

