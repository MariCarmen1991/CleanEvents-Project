package com.example.cleanevents;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

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

        return root;
    }



    public void descargarUsuario(){
        SharedPreferences preferences=getContext().getSharedPreferences("idUsuarioPref", MODE_PRIVATE);
        long idUsuario= preferences.getLong("idUsuario", 0);
        Log.d("MARICARMEN", "IDUSUARIOS fragment"+idUsuario);
        FirebaseFirestore db;
        FirebaseDatabase database;

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
        FirebaseDatabase database;

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

    public void  eventosParticipados(){

        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();
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


}














