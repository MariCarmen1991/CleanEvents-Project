package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.color_corporativo));
        bottomNavigationView.setOnNavigationItemSelectedListener(bNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener bNavigationView =  new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectFragment = new HomeFragment();
                        break;
                    case R.id.perfil_usuario:
                        selectFragment = new PerfilFragment();
                        break;
                    case R.id.anadir_evento:
                        selectFragment = new NuevoEventoFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectFragment).commit();
                return true;
            }
    };

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
                Intent politica_privacidad = new Intent(MainActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(politica_privacidad);
                return true;
            case R.id.quienes_somos:
                Intent quienes_somos = new Intent(MainActivity.this, QuienesSomosActivity.class);
                startActivity(quienes_somos);
                return true;
            case R.id.acerca_de:
                Intent acerca_de = new Intent(MainActivity.this, AcercaDeActivity.class);
                startActivity(acerca_de);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}