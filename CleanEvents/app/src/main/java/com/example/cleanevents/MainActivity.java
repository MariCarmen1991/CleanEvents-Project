package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int FILTROS_ACTIVITY_REQUEST_CODE = 17;

    Button btnFiltros, btnMapa, btnListado;
    Toolbar bar;
    BottomNavigationView bottomNavigationView;
    ProgressBar barra;
    ArrayList<Evento> eventoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layoutsplah;
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
                startActivityForResult(new Intent(MainActivity.this, FiltrosActivity.class), FILTROS_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == FILTROS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get ARRAYLIST data from Intent
                //String returnString = data.getStringExtra("keyName");
                eventoArrayList = (ArrayList<Evento>) data.getSerializableExtra("arrayList");

            }
        }
    }

    //función para cargar fragments.

    private void cargarFragment(Fragment fragment) {
        FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView, fragment).addToBackStack(null).commit();
    }

}


