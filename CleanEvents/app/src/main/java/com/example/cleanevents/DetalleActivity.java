package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {

    ImageView imagenEvento;
    Evento eventoRecibido;
    Button btnUnirse;
    TextView twNombre, twDescripcion, twPoblacion, horaInicio, horaFinal;
    private FirebaseFirestore bd;
    private FirebaseDatabase baseDatos;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);


        inicializar();
        recibirIntent();
    }

    private void inicializar(){
        imagenEvento=findViewById(R.id.imagen_evento);
        twNombre=findViewById(R.id.nombre_evento_id);
        twDescripcion=findViewById(R.id.descripcion_id);
        twPoblacion=findViewById(R.id.localizacion_id);
        horaInicio=findViewById(R.id.hora_inicio);
        horaFinal=findViewById(R.id.hora_final);
        btnUnirse=findViewById(R.id.btn_unirse);


    }

    public void recibirIntent(){

        eventoRecibido= (Evento) getIntent().getExtras().getSerializable("eventoActual");
        twPoblacion.setText(eventoRecibido.getPoblacion());
        twDescripcion.setText(eventoRecibido.getDescripcion());
        twNombre.setText(eventoRecibido.getNombre());

        Log.d("MARICARMEN", "evento"+eventoRecibido.toString());

        Picasso.get()
                .load(eventoRecibido.getImagen()) // internet path
                .placeholder(R.mipmap.ic_launcher)  // preload
                .error(R.mipmap.ic_launcher_round)        // load error
                .into(imagenEvento);

        //guardar coordenadas del evento

        SharedPreferences preferences=DetalleActivity.this.getSharedPreferences("coordprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor=preferences.edit();
        editor.putString("lat", String.valueOf(eventoRecibido.getLatitud()));
        editor.putString("lon", String.valueOf(eventoRecibido.getLongitud()));
        editor.apply();

    }




private void unirme(){

        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


}

private void guardarParticipante(){

/*
    bd= FirebaseFirestore.getInstance();
    baseDatos= FirebaseDatabase.getInstance();
    Log.d("maricarmen","ha funcionado");
    databaseReference=baseDatos.getReference().child("usuario");
    databaseReference.setValue(usuario);
    bd.collection("usuario").add(usuario);*/

}


}