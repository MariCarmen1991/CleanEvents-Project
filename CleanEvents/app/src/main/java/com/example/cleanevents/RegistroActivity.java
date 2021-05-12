package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    EditText etNombre, etApellido, etCorreo, etPassword;
    Button btnRegistro;
    FirebaseAuth firebaseAut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializar();
        registro();
    }


    public void inicializar(){
        etNombre=findViewById(R.id.et_nombre);
        etApellido=findViewById(R.id.et_apellido);
        etCorreo=findViewById(R.id.et_correo);
        etPassword=findViewById(R.id.et_password);
        firebaseAut=FirebaseAuth.getInstance();
        btnRegistro=findViewById(R.id.btn_acceso);
    }

    public void registro(){
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNombre.getText().toString().isEmpty()){
                    if(!etApellido.getText().toString().isEmpty()){
                        if(validarEmail(etCorreo.getText().toString())&& !etCorreo.getText().toString().isEmpty()){
                            if(etPassword.getText().toString()!=null){

                                firebaseAut.createUserWithEmailAndPassword(etCorreo.getText().toString(),etPassword.getText().toString())
                                        .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseUser user=firebaseAut.getCurrentUser();
                                                    user.sendEmailVerification();

                                                    Toast.makeText(RegistroActivity.this, "Registro Completado", Toast.LENGTH_SHORT).show();
                                                    Intent i= new Intent(RegistroActivity.this,MainActivity.class);
                                                    startActivity(i);
                                                }
                                                else{
                                                    Toast.makeText(RegistroActivity.this, "El registro ha fallado", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                //Registrar Usuario
                            }
                            else {
                                //la contraseña no es válida
                            }
                        }
                        else{
                            //el correo no es valido
                        }
                    }
                    else{
                        //el apellido es obligatorio
                    }
                }
                else{
                    //el Nombre es obligatorio
                }
            }
        });
    }


    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


}