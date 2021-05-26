package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapaEventosFragment extends Fragment {


    ArrayList<Evento> eventos;
    GoogleMap mapa;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mapa=googleMap;
                recargarMapa();


            Log.d("MARICARMEN", "ONRESUME"+mapa.toString());






        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mapa_eventos, container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaEventos);


        return rootView;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaEventos);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }





    }




    public void recargarMapa(){

        getParentFragmentManager().setFragmentResultListener("parent", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull @NotNull String requestKey, @NonNull @NotNull Bundle result) {
                eventos=new ArrayList<>();

                eventos=(ArrayList<Evento>) result.getSerializable("eventos");

            }
        });

           if (eventos != null) {
               for (int i = 0; i < eventos.size(); i++) {
                   Log.d("MARICARMEN", "555 " + eventos.toString());

                   LatLng ubicacionEvento = new LatLng(eventos.get(i).latitud, eventos.get(i).getLongitud());
                   MarkerOptions markerOptions = new MarkerOptions();
                   markerOptions.position(ubicacionEvento).title(eventos.get(i).getNombre());
                   if(mapa!=null) {
                       mapa.addMarker(markerOptions);
                       mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionEvento, 13));
                   }

               }
           }

           //--Función que muestra el detalle del evento si haces click en el nombre del marcador
        if (mapa!=null) {
            mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {


                @Override
                public void onInfoWindowClick(Marker marker) {

                    for (int i = 0; i < eventos.size(); i++) {
                        Log.d("MARICARMEN", "DATOS DE LOS MARKERS   " + eventos.get(i).getNombre() + marker.getTitle());

                        if (eventos.get(i).getNombre().contains(marker.getTitle())) {
                            Intent intent = new Intent(getActivity(), DetalleActivity.class);
                            intent.putExtra("eventoActual", eventos.get(i));
                            startActivity(intent);
                        }

                    }

                }
            });
        }


    }






}