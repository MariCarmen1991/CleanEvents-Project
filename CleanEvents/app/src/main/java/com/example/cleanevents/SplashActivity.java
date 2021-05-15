package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        cargaActivity();
    }

    public void cargaActivity(){
        Intent i=new Intent(SplashActivity.this, AccesoActivity.class);
        startActivity(i);
        SplashActivity.this.finish();
    }
}