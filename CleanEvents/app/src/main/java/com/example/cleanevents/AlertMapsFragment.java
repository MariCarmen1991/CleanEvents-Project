package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class AlertMapsFragment extends Fragment {
     GoogleMap mMap;
     EditText busqueda;
     Button buscar, guardarCoordenadas;
     String lugar, coordenadas;
     final Double[] latitud = new Double[1];
     final Double[] longitud = new Double[1];


    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap=googleMap;



        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_alert_maps, container, false);

        busqueda= rootView.findViewById(R.id.buscador);
        buscar=rootView.findViewById(R.id.aceptar);
        guardarCoordenadas=rootView.findViewById(R.id.guardar_coordenadas);

        buscar();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }




    public void buscar(){
        latitud [0]=49.0;
        longitud [0]=2.9;


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lonlng = coordenadas.split(",");
                String lat = lonlng[0];
                String[] lng2 = lat.split("\\(");
                lat = lng2[1];
                String lon = lonlng[1];
                lon = lon.substring(0, lon.length()-1);

                Log.d("MARICARMEN", " "+lat+lon);
                latitud[0] = Double.parseDouble(lat);
                longitud[0] =Double.parseDouble(lon);

                Log.d("MARICARMEN", "COORD"+latitud[0]+","+longitud[0]);
                LatLng ubicacionEvento = new LatLng(latitud[0],longitud[0]);
                mMap.clear();
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(ubicacionEvento);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionEvento,13));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Log.d("MARICARMEN", "HELLO");
                        LatLng ubicacionEvento = latLng;
                        mMap.clear();
                        MarkerOptions markerOptions=new MarkerOptions();
                        markerOptions.position(ubicacionEvento);
                        mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionEvento,16));
                        latitud[0]=latLng.latitude;
                        longitud[0]=latLng.longitude;
                    }
                });


                guardarCoordenadas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setTitle("Guardar Coordenadas de mi Evento");
                        builder.setMessage("¿Estás seguro que quieres guardar estas coordenadas?");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences=getContext().getSharedPreferences("prefsAlert", 0);
                                SharedPreferences.Editor editor;
                                editor=preferences.edit();
                                editor.putString("Alatitud",String.valueOf(latitud[0]));
                                editor.putString("Alongitud",String.valueOf(longitud[0]));
                                editor.apply();

                                getActivity().onBackPressed();

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });



            }


        });


        Places.initialize(getContext(), "AIzaSyDXMuPCd5oSLeT3Ecm3bBAoSok6G80X-bA");
        busqueda.setFocusable(false);
        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                        ,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                startActivityForResult(intent, 100);
            }
        });





    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == getActivity().RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            busqueda.setText(place.getAddress());
            lugar = place.getName();
            coordenadas = String.valueOf(place.getLatLng());
            //Toast.makeText(this,"[NAME:"+place.getName()+"LATLNG:"+place.getLatLng()+"]", Toast.LENGTH_LONG).show();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(),""+status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

}