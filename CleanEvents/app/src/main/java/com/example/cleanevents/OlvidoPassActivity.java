package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class OlvidoPassActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button btnSolicitar;
    EditText etMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_pass);
        inicializa();
        recordarPass();

    }

    public void inicializa(){
        firebaseAuth=FirebaseAuth.getInstance();
        btnSolicitar=findViewById(R.id.btn_solicitar);
        etMail=findViewById(R.id.et_mail);

    }

    public void recordarPass(){
     btnSolicitar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           if(!etMail.getText().toString().isEmpty()) {
               firebaseAuth.sendPasswordResetEmail(etMail.getText().toString())
                       .addOnCompleteListener(OlvidoPassActivity.this, new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull @NotNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(OlvidoPassActivity.this, "Se ha enviado un mensaje a tu correo", Toast.LENGTH_LONG)
                                           .show();
                               } else {
                                   Toast.makeText(OlvidoPassActivity.this, "Ha habido un error en el proceso", Toast.LENGTH_LONG)
                                           .show();
                               }
                           }
                       });
           }
           else{
               Toast.makeText(OlvidoPassActivity.this, "Comprueba que el correo electrónico es correcto", Toast.LENGTH_LONG)
                       .show();
           }
         }
     });

    }
}