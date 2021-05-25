package com.example.cleanevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class BuscadorMapaActivity extends AppCompatActivity {

    Button aceptar;
    EditText busqueda;

    String lugar, coordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador_mapa);
        busqueda = findViewById(R.id.buscador);
        aceptar = findViewById(R.id.aceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lonlng = coordenadas.split(",");
                String lng = lonlng[0];
                String[] lng2 = lng.split("\\(");
                lng = lng2[1];
                String lon = lonlng[1];
                lon = lon.substring(0, lon.length()-1);
                Toast.makeText(BuscadorMapaActivity.this, "Bienvenido a "+lugar+" en las coordenadas LAT: "+lng+" y LON: "+lon, Toast.LENGTH_LONG).show();
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyDXMuPCd5oSLeT3Ecm3bBAoSok6G80X-bA");
        busqueda.setFocusable(false);
        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                ,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(BuscadorMapaActivity.this);
                startActivityForResult(intent, 100);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            busqueda.setText(place.getAddress());
            lugar = place.getName();
            coordenadas = String.valueOf(place.getLatLng());
            Toast.makeText(this,"[NAME:"+place.getName()+"LATLNG:"+place.getLatLng()+"]", Toast.LENGTH_LONG).show();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this,""+status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }
}