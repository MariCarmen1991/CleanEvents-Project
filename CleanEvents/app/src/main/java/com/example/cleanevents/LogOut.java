package com.example.cleanevents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class LogOut {

    public LogOut() {
    }

    public static void cerrarSesion(Context packageContext, Class<?>  clase){

    FirebaseAuth.getInstance().signOut();
    SharedPreferences.Editor editor = packageContext.getSharedPreferences("prefs", MODE_PRIVATE).edit();
    editor.clear().apply();
    Intent i= new Intent(packageContext, clase);
    packageContext.startActivity(i);
}

}

