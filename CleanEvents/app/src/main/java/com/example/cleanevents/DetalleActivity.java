package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {
    ImageView imagenEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        imagenEvento=findViewById(R.id.imagen_evento);

        Picasso.get()
                .load(Uri.parse("https://www.ecestaticos.com/image/clipping/557/418/149b110413c188544b89212ad842c8c1/dos-playas-espanolas-entran-en-el-ranking-de-las-10-mejores-de-europa.jpg")) // internet path
                .placeholder(R.mipmap.ic_launcher)  // preload
                .error(R.mipmap.ic_launcher_round)        // load error
                .into(imagenEvento);
    }
}