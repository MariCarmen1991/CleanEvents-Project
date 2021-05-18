package com.example.cleanevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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
        btn_filtro_playa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_playa.setFocusableInTouchMode(true);
                filtro_tipo = "playa";
            }
        });

        btn_filtro_montanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_montanya.setFocusableInTouchMode(true);
                filtro_tipo = "montaña";

            }
        });

        btn_filtro_fondo_marino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_fondo_marino.setFocusableInTouchMode(true);
                filtro_tipo = "fondo marino";

            }
        });

        btn_filtro_bosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_bosque.setFocusableInTouchMode(true);
                filtro_tipo = "bosque";

            }
        });

        btn_filtro_ciudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_ciudad.setFocusableInTouchMode(true);
                filtro_tipo = "ciudad";

            }
        });

        btn_filtro_rio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_filtro_rio.setFocusableInTouchMode(true);
                filtro_tipo = "rio";

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
                // TODO: CREACION DE LA QUERY PARA LA BASE DE DATOS
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