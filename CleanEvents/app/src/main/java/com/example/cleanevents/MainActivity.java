package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Toolbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar=findViewById(R.id.toolBar);
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
                Toast.makeText(this, "POLITICA PRIVACIDAD", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.quienes_somos:
                Toast.makeText(this, "QUIENES SOMOS", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.acerca_de:
                Toast.makeText(this, "ACERCA DE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                Toast.makeText(this, "HAS CERRADO SESIÓN", Toast.LENGTH_SHORT).show();
                logout();


        }
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationView = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.perfil_usuario:
                    Toast.makeText(MainActivity.this, "PERFIL USUARIO", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.anadir_evento:
                    Toast.makeText(MainActivity.this, "NUEVO EVENTO", Toast.LENGTH_SHORT).show();
                    return true;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer ,selectFragment).commit();
            return false;
        }
    };

    private void logout(){
        //cierra sesión y vuelve a la activity de Inicio de Sesión borra el id de usuario de preferences
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.clear().apply();
        Intent i= new Intent(MainActivity.this, AccesoActivity.class);
        startActivity(i);
        finish();
    }


}