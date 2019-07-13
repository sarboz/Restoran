package com.restoran.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.restoran.R;
import com.restoran.Models.ZalStol;

import java.util.List;

public class StolRecyclerAdapter extends RecyclerView.Adapter<StolRecyclerAdapter.Holder> {
    List<ZalStol.Stol> list;
    SharedPreferences pref;

    public StolRecyclerAdapter(List<ZalStol.Stol> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stol, viewGroup, false);
        pref = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());


        int h = Integer.parseInt(pref.getString("stol_height", "500"));
        item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        return new Holder(item);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        ZalStol.Stol stol = list.get(i);
        if(!stol.isStatus()) {
            CardView l = (CardView) holder.name.getParent().getParent();
            l.setBackgroundResource(R.color.colorBlack);
        }else{
            CardView l = (CardView) holder.name.getParent().getParent();
            l.setBackgroundResource(R.color.colorWhite);
        }
        holder.name.setTextSize(Integer.parseInt(pref.getString("stol_shrift", "20")));
        holder.name.setText(stol.getName());

    }

    public void UpdateList(String id, String text) {
        for (ZalStol.Stol s : list) {
            if (s.getId().equals(id))
                s.setName(text);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id, name;

        public Holder(@NonNull View v) {
            super(v);
            id = (TextView) v.findViewById(R.id.id);
            name = (TextView) v.findViewById(R.id.name);
        }
    }
}
