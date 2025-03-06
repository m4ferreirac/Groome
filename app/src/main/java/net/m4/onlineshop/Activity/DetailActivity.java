package net.m4.onlineshop.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.m4.onlineshop.Adapter.SimilarAdapter;
import net.m4.onlineshop.Domain.ItemDomain;
import net.m4.onlineshop.Models.ItemCart;
import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.ActivityDetailBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private int weight = 1;
    private DatabaseReference usersRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserId = getCurrentUserId();
        FirebaseApp.initializeApp(this);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        getBundles();
        setVariable();
        initSimilarList();
        checkIfItemInWishlist();
    }


    private void checkIfItemInWishlist() {
        usersRef.child(currentUserId).child("wishlist").child(String.valueOf(object.getId()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.favBtn.setImageResource(R.drawable.heart_check_24px);
                        } else {
                            binding.favBtn.setImageResource(R.drawable.favorite_green);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    private void initSimilarList() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarSimilar.setVisibility(View.VISIBLE);
        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    if(!list.isEmpty()){
                        binding.recyclerViewsimilar.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewsimilar.setAdapter(new SimilarAdapter(list));
                    }
                    binding.progressBarSimilar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToCart(int id, double quantityKg, double price) {
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<ItemCart>>(){}.getType();
        ArrayList<ItemCart> cartList;

        String json = sharedPreferences.getString("cart_items", "");
        if (!json.isEmpty()) {
            cartList = gson.fromJson(json, type);
        } else {
            cartList = new ArrayList<>();
        }

        boolean itemExists = false;
        for (ItemCart item : cartList) {
            if (item.getId() == id) {
                itemExists = true;
                item.setQuantityKg(item.getQuantityKg() + quantityKg);
                break;
            }
        }

        if (!itemExists) {
            cartList.add(new ItemCart(id, quantityKg, price));
        }

        editor.putString("cart_items", gson.toJson(cartList));
        editor.apply();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());

        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.img);

        binding.priceKgTxt.setText(object.getPrice() + " €/Kg");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingBar.setRating((float) object.getStar());
        binding.ratingTxt.setText("(" + object.getStar() + ")");
        binding.totalTxt.setText((weight * object.getPrice()) + "€");

        binding.plusBtn.setOnClickListener(view -> {
            weight++;
            binding.weightTxt.setText(weight + " Kg");
            binding.totalTxt.setText((weight * object.getPrice()) + "€");
        });

        binding.minusBtn.setOnClickListener(view -> {
            if(weight > 1){
                weight--;
                binding.weightTxt.setText(weight + " Kg");
                binding.totalTxt.setText((weight * object.getPrice()) + "€");
            }
        });

        binding.addToCartBtn.setOnClickListener(view -> {
            addToCart(object.getId(), weight, object.getPrice());
            Snackbar.make(binding.getRoot(), "Item added to your Cart!", Snackbar.LENGTH_SHORT).show();
        });

        binding.favBtn.setOnClickListener(view -> {
            toggleFavoriteStatus();
        });
    }

    private void getBundles() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }

    private void toggleFavoriteStatus() {
        usersRef.child(currentUserId).child("wishlist").child(String.valueOf(object.getId()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            usersRef.child(currentUserId).child("wishlist").child(String.valueOf(object.getId()))
                                    .removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            binding.favBtn.setImageResource(R.drawable.favorite_green);
                                            Toast.makeText(DetailActivity.this, "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            usersRef.child(currentUserId).child("wishlist").child(String.valueOf(object.getId()))
                                    .setValue(object)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            binding.favBtn.setImageResource(R.drawable.heart_check_24px);
                                            Toast.makeText(DetailActivity.this, "Item added to wishlist", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}