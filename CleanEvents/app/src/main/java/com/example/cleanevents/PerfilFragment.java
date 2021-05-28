package com.example.cleanevents;

import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    Usuario usuarioActual= new Usuario();
    View root;
    TextView txtNombreUsuario, txtDescripcion;
    Button organizados,participados, editar;
    RecyclerView recyclerView;
    ArrayList<Evento> eventoOrganizado;
    Evento tuEvento;
    Fecha fecha;
    EventosAdapter adapter;
    ImageView imagenPerfil;

    private static String IMAGE_PATH="";
    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private static final int REQUEST_IMAGE_CAMERA = 102;
    private static final int PICK_IMAGE = 100;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }


    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        root= inflater.inflate( R.layout.fragment_perfil, container, false );

        txtNombreUsuario=root.findViewById(R.id.txt_nombre_usuario);
        txtDescripcion=root.findViewById(R.id.txt_descripcion);
        descargarUsuario();

        recyclerView=root.findViewById(R.id.recycler);
        imagenPerfil=root.findViewById(R.id.image_perfil);
        organizados=root.findViewById(R.id.btn_eventos_organizados);
        participados=root.findViewById(R.id.btn_eventos_participados);
        editar=root.findViewById(R.id.btn_editar_perfil);

        organizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventosOrganizados();
            }
        });
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });


        return root;
    }



    public void descargarUsuario(){
        SharedPreferences preferences=getContext().getSharedPreferences("idUsuarioPref", MODE_PRIVATE);
        long idUsuario= preferences.getLong("idUsuario", 0);
        Log.d("MARICARMEN", "IDUSUARIOS fragment"+idUsuario);
        FirebaseFirestore db;

        db= FirebaseFirestore.getInstance();
        db.collection("usuario")
                .whereEqualTo("idUsuario",idUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot user: task.getResult()){

                                usuarioActual.setNombre((String) user.get("nombre"));
                                usuarioActual.setDescripcion((String) user.get("descripcion"));
                                usuarioActual.setIdUsuario((Long) user.get("idUsuario"));
                                txtNombreUsuario.setText(usuarioActual.getNombre());
                                txtDescripcion.setText(usuarioActual.getDescripcion());
                                Log.d("MARICARMEN", "USER "+usuarioActual.toString());





                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }


    public void eventosOrganizados(){

        FirebaseFirestore db;

        Log.d("MARICARMEN", "eventoOrganizado ");
        eventoOrganizado = new ArrayList<Evento>();

        db= FirebaseFirestore.getInstance();
        db.collection("evento")
                .whereEqualTo("idUsuario",usuarioActual.getIdUsuario())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot evento: task.getResult()){


                                Log.d("MARICARMEN", "eventoorganizado "+evento.getData());
                                tuEvento= new Evento();
                                fecha= new Fecha();
                                tuEvento.setNombre((String) evento.getData().get("nombreEvento"));
                                tuEvento.setPoblacion((String) evento.getData().get("poblacion"));
                                tuEvento.setDescripcion((String) evento.getData().get("descripcion"));
                                tuEvento.setLongitud(evento.getDouble("Longitud"));
                                tuEvento.setLatitud(evento.getDouble("Latitud"));
                                tuEvento.setImagen((String) evento.getData().get("imagen"));
                                tuEvento.setTipoActividad((String) evento.getData().get("tipoActividad"));
                                tuEvento.setNumParticipantes((Long) evento.getData().get("numParticipantes"));
                                tuEvento.setIdUsuario((Long)evento.getData().get("idUsuario"));
                                tuEvento.setIdEvento((Long)evento.getData().get("idEvento"));
                                tuEvento.setNombreOrganizador((String) evento.get("nombreOrganizador"));
                                fecha.setDia((String) evento.get("dia"));
                                fecha.setHoraInicio((String) evento.get("horaInicio"));
                                fecha.setHoraFinal((String) evento.get("horaFinal"));
                                tuEvento.setFecha(fecha);
                                eventoOrganizado.add(tuEvento);

                                cargarRecycler(eventoOrganizado);


                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


    }

    public void  eventosParticipados(){

        FirebaseFirestore db;



        db= FirebaseFirestore.getInstance();

        db.collection("evento")
                .whereEqualTo("idUsuario",usuarioActual.getIdUsuario())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot organizado: task.getResult()){


                                Log.d("MARICARMEN", "USER "+organizado.getData());


                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


    }



    public void cargarRecycler(ArrayList<Evento> listaEvento) {
        if(listaEvento!=null) {
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
                    int position = recyclerView.getChildAdapterPosition(v);
                    Intent i = new Intent(getContext(), DetalleActivity.class);
                    i.putExtra("eventoActual", listaEvento.get(position));
                    startActivity(i);
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "NO TIENES EVENTOS QUE MOSTRAR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            Uri ruta= data.getData();
            try {
                InputStream stream= getActivity().getContentResolver().openInputStream(ruta);

                Bitmap bmp= BitmapFactory.decodeStream(stream);

                imagenPerfil.setImageBitmap(bmp);
            }
            catch (FileNotFoundException e){}


        }
    }

    public void cargarFoto(){

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);


    }



















}














