package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class DetalleActivity extends AppCompatActivity {

    ImageView imagenEvento;
    Evento eventoRecibido;
    Button btnUnirse;
    long idUsuario;
    TextView twNombre, twDescripcion, twPoblacion, horaInicio, horaFinal;
    ArrayList<String> id;
    long[] num;
    private FirebaseFirestore bd;
    private FirebaseDatabase baseDatos;
    private DatabaseReference databaseReference;
    Boolean sumado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        SharedPreferences preferences=DetalleActivity.this.getSharedPreferences("idUsuarioPref", MODE_PRIVATE);
        idUsuario= preferences.getLong("idUsuario",0);

        inicializar();
        recibirIntent();
        unirme();
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
        horaInicio.setText(eventoRecibido.getFecha().getHoraInicio());
        horaFinal.setText(eventoRecibido.getFecha().getHoraFinal());


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


                    guardarParticipante();



            }
        });


    }

    private void guardarParticipante(){

        HashMap eventoUsuario = new HashMap();
        eventoUsuario.put("idUsuario",idUsuario);
        eventoUsuario.put("idEvento",eventoRecibido.getIdEvento() );

        bd=FirebaseFirestore.getInstance();
        baseDatos=FirebaseDatabase.getInstance();
        Log.d("maricarmen","GUARDADO");
        databaseReference=baseDatos.getReference().child("EventoUsuario");
        bd.collection("EventoUsuario").add(eventoUsuario);
        Toast.makeText(getApplicationContext(), "TE HAS UNIDO CON ÉXITO",Toast.LENGTH_SHORT).show();

    }

    private Boolean sumarParticipante(){
        FirebaseFirestore db;
        FirebaseDatabase reference = FirebaseDatabase.getInstance();
        num = new long[0];
        id=new ArrayList<>();


        db= FirebaseFirestore.getInstance();
        db.collection("evento")
                .whereEqualTo("idEvento",eventoRecibido.getIdEvento())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot evento: task.getResult()){

                                long numParticipantes = (long) evento.get("numParticipantes");
                                numParticipantes=numParticipantes+1;

                                num[0]=numParticipantes;
                                id.add(evento.getId());
                                sumado=true;
                                Log.d("maricarmen","sumado"+sumado);



                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

        return true;
    }

    public void actualizarDato(){

        FirebaseFirestore db;
        db=FirebaseFirestore.getInstance();
        DocumentReference reference=db.collection("evento").document(id.get(0));
        reference.update("numParticipantes",num[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("MARICARMEN", "actualizado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }



    public  void participa(){
        FirebaseFirestore db;
        db= FirebaseFirestore.getInstance();
        db.collection("EventoUsuario")
                .whereEqualTo("idUsuario",idUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        Boolean existe;


                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){

                                Toast.makeText(getApplicationContext(), "TE HAS UNIDO CON ÉXITO",Toast.LENGTH_SHORT).show();
                                guardarParticipante();

                                if(sumarParticipante()){
                                    actualizarDato();
                                }
                                Log.d("MARICARMEN","vacío");
                            }



                            for(QueryDocumentSnapshot recibido: task.getResult()){
                                Log.d("MARICARMEN","DATOS"+recibido.getData());
                                if(recibido.getData().containsValue(eventoRecibido.getIdEvento())) {

                                    Toast.makeText(getApplicationContext(), "YA TE HAS UNIDO A ESTE EVENTO",Toast.LENGTH_SHORT).show();
                                break;
                                }
                            }





                        }







                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                Log.d("MARICARMEN","NO HAY DATOS");

            }
        });





    }

}