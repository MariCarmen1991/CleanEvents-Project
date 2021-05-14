package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


public class AccesoActivity extends AppCompatActivity {

    EditText etPassword, etCorreo;
    Button btnRegistrarse, btnAcceder;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        inicializar();
        accesoRegistro();
        inicioSesion();
    }


    public void inicializar(){
        etPassword=findViewById(R.id.et_password);
        etCorreo=findViewById(R.id.et_correo);
        btnRegistrarse=findViewById(R.id.btn_registrarse);
        btnAcceder=findViewById(R.id.btn_acceso);
        firebaseAuth=FirebaseAuth.getInstance();

    }

    public void logInOut(){


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
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        Intent i = new Intent(AccesoActivity.this, MainActivity.class);
                                        startActivity(i);

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
}