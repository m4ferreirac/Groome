package net.m4.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.m4.onlineshop.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
     ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }
        binding.startBtn.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this, NameActivity.class)));

        binding.signInTxt.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}