package com.restoran.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.restoran.R;
import com.restoran.Models.ZalStol;

import java.util.List;

public class StolRecyclerAdapter extends RecyclerView.Adapter<StolRecyclerAdapter.Holder> {
    private List<ZalStol.Stol> list;
    private SharedPreferences pref = null;

    public StolRecyclerAdapter(List<ZalStol.Stol> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stol, viewGroup, false);
        if (pref == null)
            pref = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());

        int h = Integer.parseInt(pref.getString("stol_height", "180"));
        item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        return new Holder(item);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        ZalStol.Stol stol = list.get(i);
        CardView  card = (CardView) holder.name.getParent().getParent();

        if (stol.isStatus()) {
            card.setBackgroundResource(R.color.colorBlack);
            holder.afitsant.setText(stol.getFio());
            holder.afitsant.setVisibility(View.VISIBLE);
        } else {
            card.setBackgroundResource(R.color.colorWhite);
            holder.afitsant.setVisibility(View.GONE);
        }
        holder.name.setTextSize(Integer.parseInt(pref.getString("stol_shrift", "20")));
        holder.name.setText(stol.getName());

        if (stol.isVizov()) {
            card.setBackgroundResource(R.color.colorVizov);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView afitsant, name;

        public Holder(@NonNull View v) {
            super(v);
            afitsant = (TextView) v.findViewById(R.id.afitsant);
            name = (TextView) v.findViewById(R.id.name);
        }
    }
}
