package com.example.chatterapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chatterapp.R;
import com.example.chatterapp.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    ActivityOtpactivityBinding binding; //binding
    FirebaseAuth auth;

    String verificationId;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater()); //to bind layout with objeect
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Sending OTP...");
        dialog.setCancelable(false);
        dialog.show();

        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();


        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.phoneLbl.setText("Verify " + phoneNumber);


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth) //For authentication check otp and number
                .setPhoneNumber("+91" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS) //valid for 60 seconds
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //   dialog.dismiss(); //added
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // dialog.dismiss();
                        //Toast.makeText(OTPActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    //If code is sent then
                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        dialog.dismiss();
                        verificationId = verifyId;

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        binding.otpView.requestFocus();
                    }
                }).build();
        //following the options send code on number
        PhoneAuthProvider.verifyPhoneNumber(options);

////same code just added onclick

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    Toast.makeText(OTPActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        });

        //       binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
//            @Override
//            public void onOtpCompleted(String otp) {
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
//
//                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
//                            startActivity(intent);
//                            finishAffinity();
//                        } else {
//                            Toast.makeText(OTPActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
    }
}