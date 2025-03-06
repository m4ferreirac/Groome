package net.m4.onlineshop.Activity;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import net.m4.onlineshop.Fragments.CartFragment;
import net.m4.onlineshop.Fragments.ExplorerFragment;
import net.m4.onlineshop.Fragments.MyOrdersFragment;
import net.m4.onlineshop.Fragments.ProfileFragment;
import net.m4.onlineshop.Fragments.WishListFragment;
import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            loadFragment(new ExplorerFragment());
            setActiveNav(binding.explorerLayout);
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        binding.explorerLayout.setOnClickListener(v -> {
            loadFragment(new ExplorerFragment());
            setActiveNav(binding.explorerLayout);
        });
        binding.cartLayout.setOnClickListener(v -> {
            loadFragment(new CartFragment());
            setActiveNav(binding.cartLayout);
        });
        binding.wishlistLayout.setOnClickListener(v -> {
            loadFragment(new WishListFragment());
            setActiveNav(binding.wishlistLayout);
        });
        binding.myOrderLayout.setOnClickListener(v -> {
            loadFragment(new MyOrdersFragment());
            setActiveNav(binding.myOrderLayout);
        });
        binding.profileLayout.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            setActiveNav(binding.profileLayout);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.fragmentContainer.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resetNavColors() {
        binding.explorerIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        binding.cartIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        binding.wishlistIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        binding.myOrderIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        binding.profileIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
    }

    private void setActiveNav(View view) {
        resetNavColors();
        int yellow = ContextCompat.getColor(this, R.color.yellow);

        if (view == binding.explorerLayout) {
            binding.explorerIcon.setColorFilter(yellow);
        } else if (view == binding.cartLayout) {
            binding.cartIcon.setColorFilter(yellow);
        } else if (view == binding.wishlistLayout) {
            binding.wishlistIcon.setColorFilter(yellow);
        } else if (view == binding.myOrderLayout) {
            binding.myOrderIcon.setColorFilter(yellow);
        } else if (view == binding.profileLayout) {
            binding.profileIcon.setColorFilter(yellow);
        }
    }
}