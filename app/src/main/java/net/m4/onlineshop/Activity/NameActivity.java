package net.m4.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.ActivityMainBinding;
import net.m4.onlineshop.databinding.ActivityNameBinding;

public class NameActivity extends BaseActivity {
    ActivityNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nextBtn.setOnClickListener(view -> {
            String name = binding.nameTxt.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(binding.getRoot().getContext(), "Please enter your name.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(NameActivity.this, EmailActivity.class);
                intent.putExtra("USER_NAME", name);
                startActivity(intent);
            }
        });
    }
}