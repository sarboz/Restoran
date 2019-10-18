package com.restoran.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        if (order.getKombo().size() > 0) {
            holder.name.setText(order.getName());
            holder.kol.setText(String.valueOf(order.getKol()));
            holder.price.setText(order.getPrice());
            holder.narh.setText("");

            if (order.getPrint()==1){
                LinearLayout l = (LinearLayout) holder.name.getParent();
                l.setBackgroundResource(R.color.print);
            }else if (order.getPrint()==0){
                LinearLayout l = (LinearLayout) holder.name.getParent();
                l.setBackgroundResource(R.color.colorWhite);
            }
            return;
        }

        holder.name.setText(order.getName());
        holder.narh.setText(order.getNarh());
        holder.price.setText(order.getPrice());
        holder.kol.setText(String.valueOf(order.getKol()));
        if (order.getStatus().equals("del"))
            holder.setVisible();
        if (order.getVariant().equals("Да")){
            LinearLayout l = (LinearLayout) holder.name.getParent();
            l.setBackgroundResource(R.color.variant);
        }else if (order.getVariant().equals("Нет")){
            LinearLayout l = (LinearLayout) holder.name.getParent();
            l.setBackgroundResource(R.color.colorWhite);
        }

        if (order.getPrint()==1){
            LinearLayout l = (LinearLayout) holder.name.getParent();
            l.setBackgroundResource(R.color.print);
        }else if (order.getPrint()==0){
            LinearLayout l = (LinearLayout) holder.name.getParent();
            l.setBackgroundResource(R.color.colorWhite);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView price, name, kol, narh;
        View v;

        public Holder(@NonNull View v) {
            super(v);
            this.v = v;
            price = (TextView) v.findViewById(R.id.price);
            name = (TextView) v.findViewById(R.id.name);
            kol = (TextView) v.findViewById(R.id.miqdor);
            narh = (TextView) v.findViewById(R.id.narh);
        }

        public void setVisible() {
            v.setVisibility(View.GONE);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            v.setClickable(false);
        }
    }
}
