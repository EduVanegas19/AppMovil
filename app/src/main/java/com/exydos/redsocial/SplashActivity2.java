package com.exydos.redsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser usuario = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                if (usuario == null){
                    startActivity(new Intent(SplashActivity2.this, FragmentReplace.class));
                }
                else{
                    startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                }
                finish();
            }
        }, 2500);
    }
}