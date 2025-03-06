package net.m4.onlineshop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import net.m4.onlineshop.Domain.ItemDomain;
import net.m4.onlineshop.Models.Order;
import net.m4.onlineshop.databinding.ViewholderOrderBinding;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final ArrayList<Order> items;
    private final Context context;

    public OrderAdapter(ArrayList<Order> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderOrderBinding binding = ViewholderOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order currentItem = items.get(position);

        holder.binding.orderIdTxt.setText(currentItem.getOrderId());

        String status = currentItem.getStatus().name();
        holder.binding.statusTxt.setText(status);

        if (status.equals("PENDING") || status.equals("SHIPPING")) {
            holder.binding.statusTxt.setTextColor(Color.YELLOW);
        } else if (status.equals("COMPLETED")) {
            holder.binding.statusTxt.setTextColor(Color.GREEN);
        } else {
            holder.binding.statusTxt.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderOrderBinding binding;

        public ViewHolder(ViewholderOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}