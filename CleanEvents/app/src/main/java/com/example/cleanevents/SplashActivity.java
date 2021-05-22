package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;

import java.util.logging.LogRecord;

public class SplashActivity extends AppCompatActivity {
    ProgressDialog    progressDialog;
    ProgressBar progressBar;
    Handler handler;
    TextView progreso;
    int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progreso=findViewById(R.id.progreso);
        progressBar=findViewById(R.id.progressBar);
        handler= new Handler();


       Thread thread= new Thread(new Runnable() {
           @Override
           public void run() {
               startProgress();
               cargaActivity();
           }
       });
       thread.start();

    }
    public void startProgress(){
        for( value=0; value<100; value++) {
            try {
                progressBar.setProgress(value);
                Thread.sleep(30);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progreso.setText(String.valueOf(value+"%"));

                }
            });
        }


    }
    public void cargaActivity(){
        Intent i=new Intent(SplashActivity.this, AccesoActivity.class);
        startActivity(i);
        SplashActivity.this.finish();
    }
}