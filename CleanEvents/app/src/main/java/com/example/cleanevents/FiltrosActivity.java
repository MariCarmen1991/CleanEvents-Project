package com.example.cleanevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class FiltrosActivity extends AppCompatActivity {
    //obtener medidas de ventana para el efecto POP-UP
    DisplayMetrics medidasVentana;


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
    static String filtro_dia = "";
    static String filtro_hora_inicio = "";
    static String filtro_hora_final = "";
    static String filtro_zona_lugar = "";

    EditText et_PlannedDate, et_PlannedHora, et_FinishHora, et_zona_lugar;
    Button btn_filtro_playa, btn_filtro_ciudad, btn_filtro_montanya, btn_borrar_filtros;
    Button btn_filtro_fondo_marino,btn_filtro_bosque, btn_filtro_rio, btn_aplicar;
    FirebaseFirestore db;

    Evento eventObject;
    ArrayList<Evento> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        inicializar();
        cambiaMedidas();
        filtros_tipo();
        filtros_tiempo();
        filtros_zona_lugar();

        btn_borrar_filtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_filtro_playa.clearFocus();
                btn_filtro_montanya.clearFocus();
                btn_filtro_fondo_marino.clearFocus();
                btn_filtro_bosque.clearFocus();
                btn_filtro_ciudad.clearFocus();
                btn_filtro_rio.clearFocus();

                et_FinishHora.setText("");
                et_PlannedHora.setText("");
                et_PlannedDate.setText("");
                et_zona_lugar.setText("");

                filtro_tipo = "";
                filtro_hora_final = "";
                filtro_hora_inicio = "";
                filtro_dia = "";
                filtro_zona_lugar = "";
            }
        });

        btn_aplicar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hacer_query();
            }
        });



    }

    private void filtros_zona_lugar() {
        et_zona_lugar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filtro_zona_lugar = et_zona_lugar.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void inicializar()
    {
        et_PlannedDate = findViewById(R.id.et_PlannedDate);
        et_PlannedHora = findViewById(R.id.et_PlannedHora);
        et_FinishHora = findViewById(R.id.et_FinishHora);
        et_zona_lugar = findViewById(R.id.et_zona_lugar);

        btn_filtro_playa = findViewById(R.id.btn_filtro_playa);
        btn_filtro_montanya = findViewById(R.id.btn_filtro_montanya);
        btn_filtro_fondo_marino = findViewById(R.id.btn_filtro_fondo_marino);
        btn_filtro_bosque = findViewById(R.id.btn_filtro_bosque);
        btn_filtro_ciudad = findViewById(R.id.btn_filtro_ciudad);
        btn_filtro_rio = findViewById(R.id.btn_filtro_rio);

        btn_aplicar = findViewById(R.id.btn_aplicar);
        btn_borrar_filtros = findViewById(R.id.btn_borrar_filtros);

        db = FirebaseFirestore.getInstance();

        //inicializa displaymetrics
        medidasVentana=new DisplayMetrics();

    }

    //MÉTODO PARA CAMBIAR LAS MEDIDAS DE LA ACTIVITY
    private void cambiaMedidas(){
        getWindowManager().getDefaultDisplay().getRealMetrics(medidasVentana);
        int ancho=medidasVentana.widthPixels;
        int alto=medidasVentana.heightPixels;
        getWindow().setLayout((int)(ancho*0.85), (int) (alto*0.7) );
    }

    // CLICK LISTENER DE LOS FILTROS DE TIPO DE ACTIVIDAD
    private void filtros_tipo()
    {
        btn_filtro_playa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "PLAYA";
            }
        });

        btn_filtro_montanya.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "MONTAÑA";
            }
        });

        btn_filtro_fondo_marino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "FONDO MARINO";
            }
        });

        btn_filtro_bosque.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "BOSQUE";
            }
        });

        btn_filtro_ciudad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "CIUDAD";
            }
        });

        btn_filtro_rio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                filtro_tipo = "RÍO";
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
                filtro_hora_inicio = et_PlannedHora.getText().toString();
            }
        });

        et_FinishHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora(et_FinishHora);
                filtro_hora_final = et_FinishHora.getText().toString();
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
                String diaFormateado = String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                et_PlannedDate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                filtro_dia = et_PlannedDate.getText().toString();
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

    public void guardar_en_array_list(QueryDocumentSnapshot evento)
    {
        Log.d("ian", " EVENTO RECUPERADO--> " + evento.getData());
        eventObject = new Evento();
        eventObject.setNombre((String) evento.getData().get("nombre"));
        eventObject.setPoblacion((String) evento.getData().get("poblacion"));
        eventObject.setDescripcion((String) evento.getData().get("descripcion"));
        eventObject.setLongitud(evento.getDouble("Longitud"));
        eventObject.setLatitud(evento.getDouble("Latitud"));
        eventObject.setImagen((String) evento.getData().get("imagen"));
        eventObject.setTipoActividad((String) evento.getData().get("tipoActividad"));
        eventos.add(eventObject);
        //cargarRecycler(eventos); // TODO ARREGLAR ESTO

        //guardar datos

        Log.d("ian", " OBJETOGUARDADO--> " + eventos.toString());

    }

    public void hacer_query()
    {
        //filtro_zona_lugar = et_zona_lugar.getText().toString();
        // TODO: CREACION DE LA QUERY PARA LA BASE DE DATOS (las horas tienen k ser exactas)

        if(!filtro_tipo.isEmpty() && filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {
            db.collection("evento").whereEqualTo("tipoActividad", filtro_tipo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento").whereEqualTo("dia", filtro_dia).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento").whereEqualTo("poblacion", filtro_zona_lugar).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(!filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("tipoActividad",filtro_tipo)
                    .whereEqualTo("dia", filtro_dia)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(!filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("tipoActividad",filtro_tipo)
                    .whereEqualTo("dia", filtro_dia)
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(!filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("tipoActividad",filtro_tipo)
                    .whereEqualTo("dia", filtro_dia)
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .whereEqualTo("poblacion", filtro_zona_lugar)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("dia", filtro_dia)
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .whereEqualTo("poblacion", filtro_zona_lugar)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .whereEqualTo("poblacion", filtro_zona_lugar)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }


        if(!filtro_tipo.isEmpty() && filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("tipoActividad", filtro_tipo)
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(!filtro_tipo.isEmpty() && filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("tipoActividad", filtro_tipo)
                    .whereEqualTo("poblacion", filtro_zona_lugar)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && !filtro_hora_inicio.isEmpty() && !filtro_hora_final.isEmpty() && filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("dia",filtro_dia)
                    .whereEqualTo("horaInicio", filtro_hora_inicio)
                    .whereEqualTo("horaFinal", filtro_hora_final)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        if(filtro_tipo.isEmpty() && !filtro_dia.isEmpty() && filtro_hora_inicio.isEmpty() && filtro_hora_final.isEmpty() && !filtro_zona_lugar.isEmpty())
        {

            db.collection("evento")
                    .whereEqualTo("dia",filtro_dia)
                    .whereEqualTo("poblacion", filtro_zona_lugar)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot evento : task.getResult())
                        {
                            guardar_en_array_list(evento);
                        }
                    }
                    else
                    {
                        Log.d("ian", "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    }
}