package com.restoran.adapter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.restoran.R;
import com.restoran.Models.Catecories;

import java.util.List;

public class PodCategoryRecyclerAdapter extends RecyclerView.Adapter<PodCategoryRecyclerAdapter.Holder> {
    List<Catecories.Cat> list;
    SharedPreferences pref;

    public PodCategoryRecyclerAdapter(List<Catecories.Cat> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pod_catalog, viewGroup, false);

        pref = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());

        int h = Integer.parseInt(pref.getString("pod_category_height", "500"));
        item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Catecories.Cat cat = list.get(i);
        holder.name.setTextSize(Integer.parseInt(pref.getString("pod_category_shrift", "20")));
        holder.name.setText(cat.getName());
        holder.id.setText(cat.getId());

        byte[] decodedString = Base64.decode(cat.getImg(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.img.setImageBitmap(decodedByte);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id, name;
        ImageView img;

        public Holder(@NonNull View v) {
            super(v);
            id = (TextView) v.findViewById(R.id.id);
            name = (TextView) v.findViewById(R.id.name);
            img=(ImageView)v.findViewById(R.id.img);
        }
    }
}
