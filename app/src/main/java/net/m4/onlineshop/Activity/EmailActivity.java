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
import net.m4.onlineshop.databinding.ActivityEmailBinding;
import net.m4.onlineshop.databinding.ActivityNameBinding;

public class EmailActivity extends BaseActivity {
    ActivityEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("USER_NAME");

        binding.nextBtn.setOnClickListener(view -> {
            String email = binding.emailTxt.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(binding.getRoot().getContext(), "Please enter your email.", Toast.LENGTH_SHORT).show();
            }else if (!isValidEmail(email)) {
                Toast.makeText(binding.getRoot().getContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(EmailActivity.this, PasswordActivity.class);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
}