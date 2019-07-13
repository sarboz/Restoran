package com.restoran.adapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.restoran.R;
import com.restoran.Models.Products;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.Holder> {
    List<Products.Product> list;
    SharedPreferences pref;

    public ProductsRecyclerAdapter(List<Products.Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        pref = PreferenceManager.getDefaultSharedPreferences(viewGroup.getContext());

        int h = Integer.parseInt(pref.getString("product_height", "500"));
        item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));

        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Products.Product p = list.get(i);
        holder.name.setTextSize(Integer.parseInt(pref.getString("product_shrift", "20")));
        holder.name.setText(p.getName());
        holder.id.setText("");
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
