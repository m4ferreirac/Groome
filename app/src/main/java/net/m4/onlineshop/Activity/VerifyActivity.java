package net.m4.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.m4.onlineshop.Models.User;
import net.m4.onlineshop.databinding.ActivityWaitBinding;

public class VerifyActivity extends BaseActivity {
    ActivityWaitBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra("USER_NAME");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        binding.verifyBtn.setOnClickListener(view -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            createUserInDatabase(user, username);
                        } else {
                            Toast.makeText(binding.getRoot().getContext(), "Email isn't verified.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(binding.getRoot().getContext(), "Error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void createUserInDatabase(FirebaseUser user, String username) {
        DatabaseReference usersRef = mDatabase.getReference("Users");

        User userData = new User(user.getUid(), user.getEmail(), username);

        usersRef.child(user.getUid()).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(binding.getRoot().getContext(), "Failed to create user in database.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}