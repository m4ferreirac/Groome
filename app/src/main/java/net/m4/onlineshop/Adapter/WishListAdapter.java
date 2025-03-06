package net.m4.onlineshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.m4.onlineshop.Activity.DetailActivity;
import net.m4.onlineshop.Domain.ItemDomain;
import net.m4.onlineshop.databinding.ViewholderWishlistBinding;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private final ArrayList<ItemDomain> items;
    private final Context context;

    public WishListAdapter(ArrayList<ItemDomain> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderWishlistBinding binding = ViewholderWishlistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain currentItem = items.get(position);

        holder.binding.titleItemTxt.setText(currentItem.getTitle());
        holder.binding.categoryItemTxt.setText("");

        Glide.with(context)
                .load(currentItem.getImagePath())
                .into(holder.binding.wishListImg);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", currentItem);
            context.startActivity(intent);
        });

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category").child(String.valueOf(currentItem.getCategoryId()));
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String categoryName = snapshot.child("Name").getValue(String.class);
                    holder.binding.categoryItemTxt.setText(categoryName);
                    holder.binding.totalEachTxt.setText(String.format("%.2f €", currentItem.getPrice()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.binding.removeBtn.setOnClickListener(view -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference wishlistRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userId)
                    .child("wishlist")
                    .child(String.valueOf(currentItem.getId()));

            wishlistRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, items.size());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderWishlistBinding binding;

        public ViewHolder(ViewholderWishlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}