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


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {


            getParentFragmentManager().setFragmentResultListener("parent", getActivity(), new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull @NotNull String requestKey, @NonNull @NotNull Bundle result) {
                    eventos=new ArrayList<>();

                    eventos=(ArrayList<Evento>) result.getSerializable("eventos");
                    Log.d("MARICARMEN","555 "+       eventos.toString());

                }
            });

            //cargar imagen para el marker
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marcadormapa);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            if(eventos!=null) {
                for (int i = 0; i < eventos.size(); i++) {

                    LatLng ubicacionEvento = new LatLng(eventos.get(i).latitud, eventos.get(i).getLongitud());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(ubicacionEvento).title(eventos.get(i).getNombre());
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    googleMap.addMarker(markerOptions);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionEvento, 10));

                }
            }

            //--Función que muestra el detalle del evento si haces click en el nombre del marcador
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {


                @Override
                public void onInfoWindowClick(Marker marker) {

                    for(int i=0; i<eventos.size(); i++){
                        Log.d("MARICARMEN", "DATOS DE LOS MARKERS   "+eventos.get(i).getNombre()+marker.getTitle());

                        if(eventos.get(i).getNombre().contains(marker.getTitle())){
                            Intent intent= new Intent(getActivity(),DetalleActivity.class);
                            intent.putExtra("eventoActual",  eventos.get(i));
                            startActivity(intent);
                        }

                    }

                }
            });

        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mapa_eventos, container, false);
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











}