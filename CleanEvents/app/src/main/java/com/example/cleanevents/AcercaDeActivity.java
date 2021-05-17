package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AcercaDeActivity extends AppCompatActivity {
    Toolbar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        bar=findViewById(R.id.toolbar_id);
        setTitle("Acerca de...");
        setSupportActionBar(bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navegation_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.politica_privacidad:
                Toast.makeText(this, "POLÍTICA PRIVACIDAD", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(AcercaDeActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(intent);
                return true;
            case R.id.quienes_somos:
                Toast.makeText(this, "QUIÉNES SOMOS", Toast.LENGTH_SHORT).show();
                Intent i= new Intent(AcercaDeActivity.this, QuienesSomosActivity.class);
                startActivity(i);
                return true;
            case R.id.acerca_de:
                Intent j= new Intent(AcercaDeActivity.this, AcercaDeActivity.class);
                startActivity(j);
                Toast.makeText(this, "ACERCA DE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                Toast.makeText(this, "HAS CERRADO SESIÓN", Toast.LENGTH_SHORT).show();
                LogOut.cerrarSesion(AcercaDeActivity.this, AccesoActivity.class);
                //logout();


        }
        return super.onOptionsItemSelected(item);
    }



}