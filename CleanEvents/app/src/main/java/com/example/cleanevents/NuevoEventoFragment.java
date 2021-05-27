package com.example.cleanevents;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class NuevoEventoFragment extends Fragment {

        View rootView;

        TextView get_fecha, tvlatitud,tvlongitud, get_coord;
        EditText lugar, descripcion, zona, pista, nombre_evento, horaI, horaF;
        Button btnFecha, btnGuardar, btnCargarFoto, btnMap;
        ImageView imagen;
        Switch anadir_tesoro;
        String txtLugar, txtDescripcion, txtZona, txtPista, txtNombreEvento, urlImagen;
        Double lon, lat;
        Map<String, Object> evento;
        Boolean tesoro = false;
        Boolean imagenGuardada=false;

        DatePickerDialog datePickerDialog;
        Calendar calendario = Calendar.getInstance();
        int ano, mes, dia;
        final int hora = calendario.get(Calendar.HOUR_OF_DAY);
        final int minuto = calendario.get(Calendar.MINUTE);



        String dateLog;
        String horaInicioEvento, horaFinalEvento;
        Uri txtImagen;

        Spinner tipo_actividad;
        ArrayList<String> actividadList;
        ArrayAdapter<String> actividadAdapter;
        String actividadTexto;
        private static String IMAGE_PATH="";
        private static final int REQUEST_PERMISSION_CAMERA = 101;
        private static final int REQUEST_IMAGE_CAMERA = 102;
        private static final int PICK_IMAGE = 100;
        Uri imageUri;
        ProgressBar progressImage;

        FirebaseFirestore db;
        FirebaseDatabase fb;
        FirebaseStorage fs = FirebaseStorage.getInstance();
        private StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference downloadsStorage = fs.getReference().child("fotos").child("21147");
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("evento").child("C6uBVl8H1c9UbEZhhTSM").child("descripcion");

        public NuevoEventoFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);




        }



        // función que carga  el mapa
        public void cargarMap() {

            Intent i=new Intent(getContext(), MuestraMapaActivity.class);
            startActivity(i);
        }



        @Override
        public void onStart() {
            super.onStart();
            leerlatlng();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate( R.layout.fragment_nuevo_evento, container, false );
            imagen = rootView.findViewById(R.id.image_lugar_tesoro);
            btnMap=rootView.findViewById(R.id.btn_map);
            tvlatitud=rootView.findViewById(R.id.tv_lat);
            tvlongitud=rootView.findViewById(R.id.tv_lon);
            horaI=rootView.findViewById(R.id.get_time_1);
            horaF=rootView.findViewById(R.id.get_time_2);
            progressImage=rootView.findViewById(R.id.progressimage);
            progressImage.setVisibility(View.GONE);
            evento=new HashMap<>();


            leerUsuario();
            /* Spiner Actividades */

            tipo_actividad = rootView.findViewById(R.id.actividades);
            actividadList = new ArrayList<String>();
            actividadList.add("Playa");
            actividadList.add("Montaña");
            actividadList.add("Fondo Marino");
            actividadList.add("Bosque");
            actividadList.add("Ciudad");
            actividadList.add("Rio");
            actividadAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, actividadList);
            tipo_actividad.setAdapter(actividadAdapter);
            tipo_actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    actividadTexto = actividadList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            /* SWITCH BUTTON */

            db = FirebaseFirestore.getInstance();
            fb = FirebaseDatabase.getInstance();

            anadir_tesoro = rootView.findViewById(R.id.anadir_tesoro);
            anadir_tesoro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(anadir_tesoro.isChecked()){
                        tesoro = true;
                        Log.d("TESORO:", "SI");
                    } else {
                        Log.d("TESORO:", "NO");
                    }
                }
            });

            /* CALENDAR+DATEPICKERDIALOG */

            btnFecha = rootView.findViewById(R.id.btn_date);
            btnFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.isClickable()) {
                        dia = calendario.get(Calendar.DAY_OF_MONTH);
                        mes = calendario.get(Calendar.MONTH);
                        ano = calendario.get(Calendar.YEAR);
                        get_fecha = rootView.findViewById(R.id.get_fecha);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateLog = dayOfMonth + "/" + (month + 1) + "/" + year;
                                get_fecha.setText(dateLog);
                            }
                        }, ano, mes, dia);
                        datePickerDialog.show();
                    }
                }
            });

            /* HORA */

            horaI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog tpi = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hora = (hourOfDay < 10)? String.valueOf(0 + hourOfDay) : String.valueOf(hourOfDay);
                            String min = (minute < 10)? String.valueOf(0 + minute) : String.valueOf(minute);
                            horaInicioEvento = hora + ":" + min;
                            horaI.setText(horaInicioEvento);
                        }
                    }, hora, minuto, true);
                    tpi.show();
                }
            });

            horaF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog tpf = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hora = (hourOfDay < 10)? String.valueOf(0 + hourOfDay) : String.valueOf(hourOfDay);
                            String min = (minute < 10)? String.valueOf(0 + minute) : String.valueOf(minute);
                            horaFinalEvento = hora + ":" + min;
                            horaF.setText(horaFinalEvento);
                        }
                    }, hora, minuto, true);
                    tpf.show();
                }
            });

            /* MAPA */

            btnMap = rootView.findViewById(R.id.btn_map);
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargarMap();
                }
            });
            /* BOTON GUARDAR */

                btnGuardar = rootView.findViewById(R.id.btn_guardar);
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        String title = "Confirmación";
                        String mensaje = "¿Seguro que quieres guardar?";
                        builder.setTitle(title);
                        builder.setMessage(mensaje);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Guardar evento
                                inputData();
                                crearEventos();


                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fragmentContainerView, new HomeFragment()).addToBackStack(null).commit();

                                Toast.makeText(getActivity(), "TU EVENTO SE HA PUBLICADO", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancelar evento
                                Toast.makeText(getActivity(), "Has Cancelado", Toast.LENGTH_LONG).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });





            /* CAMERA Y GALERIA DE FOTOS*/

            imagen = rootView.findViewById(R.id.image_lugar_tesoro);
            btnCargarFoto = rootView.findViewById(R.id.btn_cargar_foto);

            if(imagen.getDrawable()==null){
                imagen.setImageResource(R.drawable.user);
            }

            btnCargarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cargar Foto");
                    builder.setMessage("¿Quieres cargar una foto desde la galeria o hacer una foto ahota?");
                    builder.setPositiveButton("Hacer Foto", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hacerFoto();

                        }
                    });
                    builder.setNeutralButton("Cargar Foto", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cargarFoto();

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            return rootView;
        }

        public void inputData(){
            /* INPUT DATOS */
            nombre_evento = rootView.findViewById(R.id.input_nombre_evento);
            txtNombreEvento = nombre_evento.getText().toString();
            lugar = rootView.findViewById(R.id.input_lugar);
            txtLugar = lugar.getText().toString();
            descripcion = rootView.findViewById(R.id.input_descripcion);
            txtDescripcion = descripcion.getText().toString();
            zona = rootView.findViewById(R.id.input_zona);
            txtZona = zona.getText().toString();
            pista = rootView.findViewById(R.id.input_pista);


            txtPista = pista.getText().toString();
        }

        /*LEER LATITUD Y LONGITOD DE SHARED PREFERENCES*/

        public void leerlatlng(){
            String latitud="0";
            String longitud="0";

            SharedPreferences preferences= getContext().getSharedPreferences("prefsAlert", MODE_PRIVATE);
            if(!preferences.getString("Alatitud","").isEmpty() && !preferences.getString("Alongitud","").isEmpty()) {
                latitud = preferences.getString("Alatitud", "");
                longitud = preferences.getString("Alongitud", "");

                lat = Double.parseDouble(latitud);
                lon = Double.parseDouble(longitud);
                tvlatitud.setText(latitud);
                tvlongitud.setText(longitud);

                Log.d("MARICARMEN", "COORD"+ preferences.getString("Alatitud", ""));
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
            if(requestCode==REQUEST_PERMISSION_CAMERA){
                if(permissions.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    goTocamera();
                } else {
                    Toast.makeText(getActivity(), "Necesita activar permisos.", Toast.LENGTH_LONG).show();
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
                //assert data != null;
                Uri foto = data.getData();
                Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                imagen.setImageBitmap(bitmap);
                Log.d("IMG", "Resultado:"+bitmap+foto);
            }

            else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                progressImage.setVisibility(View.VISIBLE);
                subirImagenStorage(data);



            }
            super.onActivityResult(requestCode, resultCode, data);
        }


//-----------------------------carga de imagen al storage de firebase----------------------------
        public void subirImagenStorage(Intent data){
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
            Log.d("MARICARMEN",""+imageUri);

            StorageReference filepath=storage.child("fotosEventos").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            IMAGE_PATH=taskSnapshot.getMetadata().getPath();

                            Log.d("MARICARMEN",IMAGE_PATH);


                            Log.d("MARICARMEN","FOTOS DATOS " +taskSnapshot.getMetadata().getName()+" "+taskSnapshot.getMetadata().getPath());
                            Toast.makeText(getContext(),"Se ha subido la foto de tu Evento",Toast.LENGTH_SHORT).show();
                            progressImage.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(),"Ha habido un error: No se ha podido subir la foto",Toast.LENGTH_SHORT).show();

                        }
                    });



        }

//-----------descarga de url de la imagen cargada y guardar la url como imagen de evento---------------

        public void bajarImagenStorage( String IMAGE_PATH){
            StorageReference baseImage = FirebaseStorage.getInstance().getReference();
            StorageReference filepath=baseImage.child(IMAGE_PATH);
            Log.d("MARICARMEN","hhh"+IMAGE_PATH);

            filepath.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            urlImagen=uri.toString();
                            evento.put("imagen",urlImagen);
                            Log.d("MARICARMEN", "url"+urlImagen);


                            imagenGuardada=true;


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            Log.d("MARICARMEN","No se ha podido descargar la foto");
                            Log.d("MARICARMEN",""+e.getMessage());


                        }
                    });


        }

        public void goTocamera() {
            Intent hacerFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(hacerFoto.resolveActivity(getContext().getPackageManager())!=null){
                startActivityForResult(hacerFoto, REQUEST_IMAGE_CAMERA);
            }
        }

        public void hacerFoto() {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    goTocamera();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                }
            } else {
                goTocamera();
            }
        }

    public void cargarFoto() {
        //Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(gallery, "Selecciona una foto"), PICK_IMAGE);
    }

    Random r = new Random();
    long idEvento = r.nextInt(1000)+1;



    public void crearEventos(){

        long numParticipantes=0;
        bajarImagenStorage(IMAGE_PATH);
        evento.put("idEvento", idEvento);
        evento.put("nombreEvento", txtNombreEvento);
        evento.put("nombre", txtLugar);
        evento.put("dia", dateLog);
        evento.put("horaInicio", horaInicioEvento);
        evento.put("horaFinal", horaFinalEvento);
        evento.put("Longitud", lon);
        evento.put("Latitud", lat);
        evento.put("descripcion", txtDescripcion);
        evento.put("tesoro", tesoro);
        evento.put("pista", txtPista);
        evento.put("poblacion", txtZona);
        evento.put("tipoActividad", actividadTexto);
        evento.put("numParticipantes", numParticipantes);

        //DatabaseReference dr = fb.getReference().child("evento");
        //dr.setValue(evento);
        String n = String.valueOf(r.nextInt(1000));
        txtNombreEvento = "Evento nº:"+n;
        DocumentReference document = db.document("evento/"+txtNombreEvento);
        document.set(evento);

        //db.collection("evento").add(evento);
        Log.d("MARICARMEN","evento creado");
    }

    public void leerUsuario(){
        SharedPreferences preferences=getContext().getSharedPreferences("idUsuarioPref", MODE_PRIVATE);
        long idUsuario= preferences.getLong("idUsuario", 0);
        Log.d("MARICARMEN", "IDUSUARIOSHARED"+idUsuario);
        evento.put("idUsuario", idUsuario);

        db= FirebaseFirestore.getInstance();
        db.collection("usuario")
               .whereEqualTo("idUsuario",idUsuario)
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){

                           for(QueryDocumentSnapshot user: task.getResult()){

                               evento.put("nombreOrganizador", user.get("nombre"));
                               Log.d("MARICARMEN",""+ user.get("nombre"));
                           }
                       }
                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull @NotNull Exception e) {

           }
       });
    }
}