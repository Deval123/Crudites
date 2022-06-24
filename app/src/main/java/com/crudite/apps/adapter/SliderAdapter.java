package com.crudite.apps.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crudite.apps.R;
import com.crudite.apps.entite.Produit;

import java.util.ArrayList;


public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private final int count;
    private int[] content;
    private ArrayList<Produit> contentString;
    private final View.OnClickListener listener;

    public SliderAdapter(int[] content, int count, View.OnClickListener listener) {
        this.content = content;
        this.count = count;
        this.listener = listener;
    }
    public SliderAdapter(ArrayList<Produit> content, int count, View.OnClickListener listener) {
        this.contentString = content;
        this.count = count;
        this.listener = listener;
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_repas_card, parent, false);

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
        //holder.setContent(content[position % content.length]);
        holder.setContentUrl(contentString.get(position % contentString.size()).image);
    }

    @Override
    public void onViewRecycled(SliderCard holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
