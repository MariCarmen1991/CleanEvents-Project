package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AccesoActivity extends AppCompatActivity {

    private static String PREF_KEY="prefs";

    EditText etPassword, etCorreo;
    TextView twOlvido, twPoltica;
    Button btnRegistrarse, btnAcceder;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        inicializar();
        accesoRegistro();
        muestraPolitica();
        if(!leerValor(AccesoActivity.this,"userId").isEmpty()){
            muestraHome();
        }
        else{
            inicioSesion();
        }
        cambioPassword();
    }


    public void inicializar(){
        etPassword=findViewById(R.id.et_password);
        etCorreo=findViewById(R.id.et_correo);
        btnRegistrarse=findViewById(R.id.btn_registrarse);
        btnAcceder=findViewById(R.id.btn_acceso);
        twOlvido=findViewById(R.id.tw_olvido);
        twPoltica=findViewById(R.id.etiqueta_politica);
        firebaseAuth=FirebaseAuth.getInstance();


    }

    public void logInOut(){
    firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {

        }
    });

    }


    public void accesoRegistro(){
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AccesoActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });
    }

    public void inicioSesion(){
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etCorreo.getText().toString().isEmpty()  && !etPassword.getText().toString().isEmpty()){
                    Log.d("maricarmen","no has entrado en el else");

                    firebaseAuth.signInWithEmailAndPassword(etCorreo.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(AccesoActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = firebaseAuth.getCurrentUser();
                                        //guardo usuario y contrase??a para mantener la conexi??n
                                        Log.d("maricarmen",user.getUid());
                                        guardarValor(AccesoActivity.this, "userId",user.getUid());
                                        muestraHome();
                                        comprobarmail();

                                    } else {
                                        Toast.makeText(AccesoActivity.this, "El acceso ha fallado, revisa tus datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else {
                    Toast.makeText(AccesoActivity.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    Log.d("maricarmen","has entrado en el else");
                }
            }
        });
    }

    public void cambioPassword(){

            twOlvido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AccesoActivity.this, OlvidoPassActivity.class);
                    startActivity(i);
                }
            });


    }


    public void comprobarmail(){

        FirebaseFirestore db;
        FirebaseDatabase reference = FirebaseDatabase.getInstance();



        db= FirebaseFirestore.getInstance();
        db.collection("usuario")
                .whereEqualTo("email",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot usuario: task.getResult()){

                                SharedPreferences preferences=getApplicationContext().getSharedPreferences("idUsuarioPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor;
                                editor=preferences.edit();
                                editor.putLong("idUsuario", (Long) usuario.getData().get("idUsuario"));
                                editor.apply();


                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }

    private void muestraHome(){
        Intent i = new Intent(AccesoActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    // shared preferences
    //funci??n que guarda datos con sharedpreferences
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

    //acceso a pol??tica de privacidad
    public void muestraPolitica(){
        twPoltica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AccesoActivity.this, PoliticaPrivacidadActivity.class);
                startActivity(i);
            }
        });
    }
}