package com.example.chatterapp.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.chatterapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding; //binding with firebase
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();

        binding.phoneBox.requestFocus();

//To move from phone to otp activity send phone number
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.phoneBox.getText().toString().trim().isEmpty()) {
                    if (binding.phoneBox.getText().toString().trim().length() == 10) {
                        Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class); //to go from phone to otp
                        intent.putExtra("phoneNumber", binding.phoneBox.getText().toString()); //extra for sending phonenummber for phone to otp activity
                        startActivity(intent);
                    } else {
                        Toast.makeText(PhoneNumberActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PhoneNumberActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}