package net.m4.onlineshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.snackbar.Snackbar;
import net.m4.onlineshop.databinding.ActivityPasswordBinding;

public class PasswordActivity extends BaseActivity {
    ActivityPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra("USER_NAME");
        String email = getIntent().getStringExtra("USER_EMAIL");

        binding.nextBtn.setOnClickListener(view -> {

            String password = binding.passwordTxt.getText().toString().trim();

            if(password.isEmpty()){
                Toast.makeText(binding.getRoot().getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
            } else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(verifyTask -> {
                                                if(verifyTask.isSuccessful()){
                                                    Intent intent = new Intent(PasswordActivity.this, VerifyActivity.class);
                                                    intent.putExtra("USER_NAME", username);
                                                    startActivity(intent);
                                                } else{
                                                    Toast.makeText(binding.getRoot().getContext(), "Error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(binding.getRoot().getContext(), "This email is already being used.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(binding.getRoot().getContext(), "Error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}