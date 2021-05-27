package com.example.cleanevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  {

    RecyclerView recyclerView;
    EventosAdapter adapter;
    Evento eventObject;
    FirebaseFirestore baseDatos;
    ArrayList<Evento> eventos;
    ProgressBar progress;
    Boolean datosCargados=false;
    Fecha fecha;

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
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView =(RecyclerView) rootView.findViewById(R.id.recyclerView); //inicializa recycler
        progress=rootView.findViewById(R.id.progress);
        leerBaseDatos("evento");

        return rootView;
    }

    public  ArrayList<Evento> leerBaseDatos(String colection){
        baseDatos=FirebaseFirestore.getInstance();
        eventos=new ArrayList<>();
        baseDatos.collection(colection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot evento: task.getResult()) {
                                Log.d("maricarmen"," EVENTO RECUPERADO--> "+evento.getData());
                                eventObject=new Evento();
                                fecha= new Fecha();
                                eventObject.setNombre((String) evento.getData().get("nombreEvento"));
                                eventObject.setPoblacion((String) evento.getData().get("poblacion"));
                                eventObject.setDescripcion((String) evento.getData().get("descripcion"));
                                eventObject.setLongitud(evento.getDouble("Longitud"));
                                eventObject.setLatitud(evento.getDouble("Latitud"));
                                eventObject.setImagen((String) evento.getData().get("imagen"));
                                eventObject.setTipoActividad((String) evento.getData().get("tipoActividad"));
                                eventObject.setNumParticipantes((Long) evento.getData().get("numParticipantes"));
                                eventObject.setIdUsuario((Long)evento.getData().get("idUsuario"));
                                eventObject.setIdEvento((Long)evento.getData().get("idEvento"));
                                eventObject.setNombreOrganizador((String) evento.get("nombreOrganizador"));
                                fecha.setDia((String) evento.get("dia"));
                                fecha.setHoraInicio((String) evento.get("horaInicio"));
                                fecha.setHoraFinal((String) evento.get("horaFinal"));
                                eventObject.setFecha(fecha);
                                Log.d("maricarmen"," EVENTO RECUPERADO--> "+evento.get("dia"));



                                eventos.add(eventObject);
                                cargarRecycler(eventos);
                                datosCargados=true;
                                Bundle datosOk= new Bundle();
                                datosOk.putBoolean("datosCargados", datosCargados);

                                //guardar datos
                                Log.d("maricarmen"," OBJETOGUARDADO--> "+eventos.toString()+datosCargados);
                            }


                        }
                        else {
                            Log.w("maricarmen" ,"ha habido un error");
                        }
                        progress.setVisibility(View.GONE);

                    }
                });
        Bundle objeto= new Bundle();
        objeto.putSerializable("eventos",eventos);
        getParentFragmentManager().setFragmentResult("parent",objeto);
        return eventos;
    }

    public void cargarRecycler(ArrayList<Evento> listaEvento)
    {
        LinearLayoutManager linearlayout = new LinearLayoutManager(getLayoutInflater().getContext());
        linearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.setHasFixedSize(true);
        adapter = new EventosAdapter(listaEvento, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=recyclerView.getChildAdapterPosition(v);
                Intent i= new Intent(getContext(),DetalleActivity.class);
                i.putExtra("eventoActual",  listaEvento.get(position));
                startActivity(i);
            }
        });
    }

  //  @Override
    public void onResume() {
        super.onResume();

        if(((MainActivity)getActivity()).eventoArrayList != null)
        {
            eventos.clear();
            eventos.addAll(((MainActivity)getActivity()).eventoArrayList);
            // cargarRecycler(((MainActivity)getActivity()).eventoArrayList);
            adapter.notifyDataSetChanged();
        }
    }
}