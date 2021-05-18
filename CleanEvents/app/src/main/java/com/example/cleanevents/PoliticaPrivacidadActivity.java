package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PoliticaPrivacidadActivity extends AppCompatActivity {
    Toolbar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);
        bar=findViewById(R.id.toolbar_id);
        setSupportActionBar(bar);
        setTitle("Política de Privacidad");
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
                Toast.makeText(this, "POLITICA PRIVACIDAD", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(PoliticaPrivacidadActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(intent);
                return true;
            case R.id.quienes_somos:
                Toast.makeText(this, "QUIENES SOMOS", Toast.LENGTH_SHORT).show();
                Intent i= new Intent(PoliticaPrivacidadActivity.this, QuienesSomosActivity.class);
                startActivity(i);
                return true;
            case R.id.acerca_de:
                Intent j= new Intent(PoliticaPrivacidadActivity.this, AcercaDeActivity.class);
                startActivity(j);
                Toast.makeText(this, "ACERCA DE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                Toast.makeText(this, "HAS CERRADO SESIÓN", Toast.LENGTH_SHORT).show();
                LogOut.cerrarSesion(PoliticaPrivacidadActivity.this, AccesoActivity.class);

        }
        return super.onOptionsItemSelected(item);
    }
}