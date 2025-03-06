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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.m4.onlineshop.R;
import net.m4.onlineshop.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signinBtn.setOnClickListener(view -> {
            String email = binding.emailTxt.getText().toString().trim();
            String password = binding.passwordTxt.getText().toString().trim();

            if(email.isEmpty()){
                Toast.makeText(binding.getRoot().getContext(), "Please enter your email.", Toast.LENGTH_SHORT).show();
            }else if(password.isEmpty()) {
                Toast.makeText(binding.getRoot().getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
            } else{
                signInUser(email, password);
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                        Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}