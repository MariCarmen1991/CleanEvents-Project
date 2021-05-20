package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {

    ImageView imagenEvento;
    Evento eventoRecibido;
    TextView twNombre, twDescripcion, twPoblacion, horaInicio, horaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        imagenEvento=findViewById(R.id.imagen_evento);
        twNombre=findViewById(R.id.nombre_evento_id);
        twDescripcion=findViewById(R.id.descripcion_id);
        twPoblacion=findViewById(R.id.localizacion_id);
        horaInicio=findViewById(R.id.hora_inicio);
        horaFinal=findViewById(R.id.hora_final);

        recibirIntent();
    }

    public void recibirIntent(){

        eventoRecibido= (Evento) getIntent().getExtras().getParcelable("eventoActual");
        twPoblacion.setText(eventoRecibido.getPoblacion());
        twDescripcion.setText(eventoRecibido.getDescripcion());
        twNombre.setText(eventoRecibido.getNombre());

        Log.d("MARICARMEN", "evento"+eventoRecibido.toString());

        Picasso.get()
                .load(eventoRecibido.getImagen()) // internet path
                .placeholder(R.mipmap.ic_launcher)  // preload
                .error(R.mipmap.ic_launcher_round)        // load error
                .into(imagenEvento);

        double lat= eventoRecibido.getCoordenadas().latitude;
        double lon= eventoRecibido.getCoordenadas().longitude;




        SharedPreferences preferences=DetalleActivity.this.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor=preferences.edit();
        editor.putString("latitude", ""+lat);
        editor.apply();


    }




   /* //función que guarda datos con sharedpreferences
    public static void guardarValor(Context context, String keyPref, String valor){
        SharedPreferences preferences=context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor=preferences.edit();
        editor.putString(keyPref, valor);
        editor.apply();
    }
    //devuelve valores guardados en preferences segun su key

    public static String leerValor(Context context, String keyPref){
        SharedPreferences preferences = context.getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }
*/


}