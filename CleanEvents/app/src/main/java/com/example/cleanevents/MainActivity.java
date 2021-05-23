package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnFiltros, btnMapa, btnListado;
    Toolbar bar;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = findViewById(R.id.mytoolbar);
        setSupportActionBar(bar);
        setTitle("");
        btnFiltros = findViewById(R.id.btn_filtros);
        btnMapa = findViewById(R.id.btn_mapa);
        btnListado = findViewById(R.id.btn_listado);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(bNavigationView);
        btnListado.setVisibility(View.GONE);


        //cargarFragment(new HomeFragment());
        cargarMapa();
        cargarListado();
        cargarFiltro();

    }


    //BOTTOM NAVIGATION BAR  : Carga los fragments del menú inferior (listado eventos, añdir eventos, perfil usuario)
    private BottomNavigationView.OnNavigationItemSelectedListener bNavigationView =  new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectFragment = new HomeFragment();
                    setTitleColor(R.color.white);
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
                Toast.makeText(this, "POLITICA PRIVACIDAD", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(intent);
                return true;
            case R.id.quienes_somos:
                Toast.makeText(this, "QUIENES SOMOS", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, QuienesSomosActivity.class);
                startActivity(i);
                return true;
            case R.id.acerca_de:
                Intent j = new Intent(MainActivity.this, AcercaDeActivity.class);
                startActivity(j);
                Toast.makeText(this, "ACERCA DE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                Toast.makeText(this, "HAS CERRADO SESIÓN", Toast.LENGTH_SHORT).show();
                LogOut.cerrarSesion(MainActivity.this, AccesoActivity.class);

        }
        return super.onOptionsItemSelected(item);
    }

    //función para abrir el fragment mapa con los eventos: cuando
    public void cargarMapa(){

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new MapaEventosFragment());
                btnMapa.setVisibility(View.GONE); //Oculta botón mapa
                btnListado.setVisibility(View.VISIBLE); //muestra botón listado

            }
        });

    }




    //función para cargar el listado de los eventos
    public void cargarListado(){

        btnListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new HomeFragment());
                btnListado.setVisibility(View.GONE);
                btnMapa.setVisibility(View.VISIBLE);


            }
        });
    }

    public void cargarFiltro()
    {
        btnFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FiltrosActivity.class));
            }
        });
    }

    //función para cargar fragments.

    private void cargarFragment(Fragment fragment) {
        FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView, fragment).addToBackStack(null).commit();
    }

}


