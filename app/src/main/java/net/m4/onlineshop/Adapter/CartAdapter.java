package net.m4.onlineshop.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import net.m4.onlineshop.Models.ItemCart;
import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<ItemCart> cartList;
    private Context context;
    private DatabaseReference databaseReference;
    private CartUpdateListener cartUpdateListener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(ArrayList<ItemCart> cartList, CartUpdateListener cartUpdateListener) {
        this.cartList = cartList;
        this.cartUpdateListener = cartUpdateListener;
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ItemCart currentItem = cartList.get(position);

        databaseReference.child(String.valueOf(currentItem.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String productName = snapshot.child("Title").getValue(String.class);
                    String productImage = snapshot.child("ImagePath").getValue(String.class);
                    double productPrice = snapshot.child("Price").getValue(Double.class);

                    holder.binding.titleItemTxt.setText(productName);

                    int quantityKg = (int) Math.round(currentItem.getQuantityKg());
                    holder.binding.numberItemTxt.setText(quantityKg + " Kg");
                    holder.binding.totalEachTxt.setText(String.format("%.2f €", (quantityKg * productPrice)));
                    holder.binding.feeEachTxt.setText(productPrice + " €");

                    Glide.with(context)
                            .load(productImage)
                            .into(holder.binding.picCart);

                    holder.binding.plusBtn.setOnClickListener(v -> {
                        currentItem.setQuantityKg(currentItem.getQuantityKg() + 1.0);
                        updateCart(holder, currentItem, productPrice);
                    });

                    holder.binding.minusBtn.setOnClickListener(v -> {
                        if (currentItem.getQuantityKg() > 1) {
                            currentItem.setQuantityKg(currentItem.getQuantityKg() - 1.0);
                            updateCart(holder, currentItem, productPrice);
                        } else {
                            removeItemFromCart(holder, currentItem);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateCart(CartViewHolder holder, ItemCart currentItem, double productPrice) {
        int quantityKg = (int) Math.round(currentItem.getQuantityKg());
        holder.binding.numberItemTxt.setText(quantityKg + " Kg");
        holder.binding.totalEachTxt.setText(String.format("%.2f €", (quantityKg * productPrice)));

        saveCartToSharedPreferences();

        if (cartUpdateListener != null) {
            cartUpdateListener.onCartUpdated();
        }

        notifyDataSetChanged();
    }

    private void removeItemFromCart(CartViewHolder holder, ItemCart currentItem) {
        cartList.remove(currentItem);
        saveCartToSharedPreferences();
        if (cartUpdateListener != null) {
            cartUpdateListener.onCartUpdated();
        }
        notifyDataSetChanged();
    }

    public void clearCart() {
        cartList.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_items", "[]");

        editor.apply();
        notifyDataSetChanged();
    }

    private void saveCartToSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String updatedJson = gson.toJson(cartList);
        editor.putString("cart_items", updatedJson);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public CartViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}