package com.example.cleanevents;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  {



    RecyclerView recyclerView;
    EventosAdapter adapter;
    FirebaseFirestore baseDatos;
    Evento eventObject;
    ArrayList<Evento> eventos= new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView =rootView.findViewById(R.id.recyclerView); //inicializa recycler


        ArrayList<Evento> lista = new ArrayList<>();
        Evento nuevoEvento = new Evento();
        nuevoEvento.setNombre("LIMPIEMOS LA PLAYA");
        nuevoEvento.setIdUsuario(2);
        nuevoEvento.setNumParticipantes(10);
        nuevoEvento.setPoblacion("LLORET DE MAR");
        lista.add(nuevoEvento);
        lista.add(nuevoEvento);
        lista.add(nuevoEvento);
        lista.add(nuevoEvento);
        //cargarRecycler(lista);
        leerBaseDatos("eventos");
        Log.d("maricarmen",""+leerBaseDatos("evento").toString());

        return rootView;//inflater.inflate(R.layout.fragment_home, container, false);

    }

    public  ArrayList<Evento> leerBaseDatos(String colection){
        baseDatos=FirebaseFirestore.getInstance();

        baseDatos.collection(colection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot evento: task.getResult()) {
                                Log.d("maricarmen"," EVENTO RECUPERADO--> "+evento.getData());
                                eventObject=new Evento();
                                eventObject.setNombre((String) evento.getData().get("nombre"));
                                eventObject.setPoblacion((String) evento.getData().get("poblacion"));
                                eventObject.setDescripcion((String) evento.getData().get("descripcion"));
                                eventObject.setLongitud(evento.getDouble("longitud"));
                                eventObject.setLatitud(evento.getDouble("latitud"));

                                //eventObject.setNumParticipantes( evento.getData().get("numParticipantes"));
                                eventObject.setImagen((String) evento.getData().get("imagen"));
                                eventos.add(eventObject);
                                cargarRecycler(eventos);

                                Log.d("maricarmen"," OBJETOGUARDADO--> "+eventos.toString());
                            }


                        }
                        else {
                            Log.w("maricarmen" ,"ha habido un error");
                        }
                    }
                });

        return eventos;
    }

    public void cargarRecycler(ArrayList<Evento> listaEvento){



        //Cargar recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EventosAdapter(listaEvento, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=recyclerView.getChildAdapterPosition(v);
                Intent i= new Intent(getContext(),DetalleActivity.class);
                i.putParcelableArrayListExtra("eventoActual",  listaEvento.get(position));
                startActivity(i);

            }
        });

    }



    /*private void cargarFragment(Fragment fragment) {
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView, fragment).addToBackStack(null).commit();
    }*/

}