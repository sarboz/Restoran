package com.restoran.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.restoran.R;
import com.restoran.Models.Orders;

import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.Holder> {
    List<Orders.Order> list;

    public OrderRecyclerAdapter(List<Orders.Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Orders.Order order = list.get(i);
        if (order.getKombo().size() > 0 && order.getStatus().isEmpty()) {
            String name = "";
            for (Orders.Order item : order.getKombo()) {
                name = name + " " + item.getName() + " x " + item.getKol() + ",";
            }

            order.setName(name);
            holder.name.setText(name);
            holder.kol.setText(String.valueOf(order.getKol()));
            holder.price.setText(order.getPrice() + " смн");
            return;
        }

        holder.name.setText(order.getName());
        holder.price.setText(order.getPrice() + " смн");
        holder.kol.setText(String.valueOf(order.getKol()));
        if (order.getStatus().equals("del"))
            holder.setVisible();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView price, name, kol;
        View v;

        public Holder(@NonNull View v) {
            super(v);
            this.v = v;
            price = (TextView) v.findViewById(R.id.price);
            name = (TextView) v.findViewById(R.id.name);
            kol = (TextView) v.findViewById(R.id.miqdor);
        }

        public void setVisible() {
            v.setVisibility(View.GONE);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            v.setClickable(false);
        }
    }
}
