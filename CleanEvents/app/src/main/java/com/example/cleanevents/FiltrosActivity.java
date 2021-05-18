package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class FiltrosActivity extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Variables para los filtros
    static String filtro_tipo = "";

    EditText et_PlannedDate, et_PlannedHora, et_FinishHora;
    Button btn_filtro_playa, btn_filtro_ciudad, btn_filtro_montanya;
    Button btn_filtro_fondo_marino,btn_filtro_bosque, btn_filtro_rio, btn_aplicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        inicializar();
        filtros_tipo();
        filtros_tiempo();
        hacer_query();

    }

    private void inicializar()
    {
        et_PlannedDate = findViewById(R.id.et_PlannedDate);
        et_PlannedHora = findViewById(R.id.et_PlannedHora);
        et_FinishHora = findViewById(R.id.et_FinishHora);

        btn_filtro_playa = findViewById(R.id.btn_filtro_playa);
        btn_filtro_montanya = findViewById(R.id.btn_filtro_montanya);
        btn_filtro_fondo_marino = findViewById(R.id.btn_filtro_fondo_marino);
        btn_filtro_bosque = findViewById(R.id.btn_filtro_bosque);
        btn_filtro_ciudad = findViewById(R.id.btn_filtro_ciudad);
        btn_filtro_rio = findViewById(R.id.btn_filtro_rio);

        btn_aplicar = findViewById(R.id.btn_aplicar);
    }

    // CLICK LISTENER DE LOS FILTROS DE TIPO DE ACTIVIDAD
    private void filtros_tipo()
    {
        btn_filtro_playa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "playa";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
            }
        });

        btn_filtro_montanya.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "montaña";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
            }
        });

        btn_filtro_fondo_marino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "fondo marino";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
            }
        });

        btn_filtro_bosque.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "bosque";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
            }
        });

        btn_filtro_ciudad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "ciudad";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
            }
        });

        btn_filtro_rio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "rio";
                Toast.makeText(FiltrosActivity.this, filtro_tipo, Toast.LENGTH_SHORT).show();
                //btn_filtro_rio.setFocusableInTouchMode(true);
            }
        });
    }

    // CLICK LISTENER DE LOS FILTROS DE TIPO TIEMPO
    private void filtros_tiempo()
    {
        et_PlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

        et_PlannedHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora(et_PlannedHora);
            }
        });

        et_FinishHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora(et_FinishHora);
            }
        });
    }

    public void hacer_query()
    {
        btn_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: CREACION DE LA QUERY PARA LA BASE DE DATOS (las horas tienen k ser exactas)

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Log.d("ian",filtro_tipo);

                db.collection("evento").whereEqualTo("tipoActividad", filtro_tipo)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("ian", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d("ian", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                et_PlannedDate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private void obtenerHora(EditText ed)
    {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario

                //Muestro la hora con el formato deseado
                ed.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
                //time = horaFormateada + DOS_PUNTOS + minutoFormateado;
            }
        }, hora, minuto, true);

        recogerHora.show();
    }
}