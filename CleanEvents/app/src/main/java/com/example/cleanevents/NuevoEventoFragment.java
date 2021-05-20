package com.example.cleanevents;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NuevoEventoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuevoEventoFragment extends Fragment {

    TextView get_fecha, get_lonlat;
    EditText lugar, descripcion, zona, pista;
    Button btnFecha, btnGuardar, btnCargarFoto, btnMap;
    ImageView imagen;
    Switch anadir_tesoro;
    String txtLugar, txtDescripcion, txtZona, txtPista;
    double lon, lat;

    Boolean tesoro = false;

    DatePickerDialog datePickerDialog;
    Calendar calendario;
    int ano, mes, dia;

    String dateLog;
    Uri txtImagen;

    Spinner tipo_actividad;
    ArrayList<String> actividadList;
    ArrayAdapter<String> actividadAdapter;
    String actividadTexto;

    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    FirebaseFirestore db;
    FirebaseDatabase fb;
    FirebaseStorage fs = FirebaseStorage.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    StorageReference downloadsStorage = fs.getReference().child("fotos").child("21147");
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("evento").child("C6uBVl8H1c9UbEZhhTSM").child("descripcion");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NuevoEventoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NuevoEventoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NuevoEventoFragment newInstance(String param1, String param2) {
        NuevoEventoFragment fragment = new NuevoEventoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void cargarMap() {
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView, new AlertMapsFragment()).addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_nuevo_evento, container, false );
        imagen = rootView.findViewById(R.id.image_lugar_tesoro);

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
                    calendario = Calendar.getInstance();
                    dia = calendario.get(Calendar.DAY_OF_MONTH);
                    mes = calendario.get(Calendar.MONTH);
                    ano = calendario.get(Calendar.YEAR);
                    get_fecha = rootView.findViewById(R.id.get_fecha);
                    datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateLog = dayOfMonth + "/" + (month + 1) + "/" + year;
                            get_fecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, dia, mes, ano);
                    datePickerDialog.show();
                }
            }
        });

        /* MAPA */

        btnMap = rootView.findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarMap();
                //FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                //ft.replace(R.id.fragmentContainerView, new MapsFragment()).addToBackStack(null).commit();
            }
        });

        /* CAPTURAR DATOS DEL MAPA*/
        AlertMapsFragment dataMap = new AlertMapsFragment();
        lat = dataMap.lat;
        lon = dataMap.lon;
        get_lonlat = rootView.findViewById(R.id.input_cordenadas);
        get_lonlat.setText("Lon:"+String.valueOf(lon)+" - Lat:"+String.valueOf(lat));

        /* BOTON GUARDAR */

        btnGuardar = rootView.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmacion");
                builder.setMessage("¿Seguro que quieres guardar?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Guardar evento
                        lugar = rootView.findViewById(R.id.input_lugar);
                        txtLugar = lugar.getText().toString();
                        descripcion = rootView.findViewById(R.id.input_descripcion);
                        txtDescripcion = descripcion.getText().toString();
                        zona = rootView.findViewById(R.id.input_zona);
                        txtZona = zona.getText().toString();
                        pista = rootView.findViewById(R.id.input_pista);
                        txtPista = pista.getText().toString();
                        subirImagenStorage();
                        crearEventos();
                        bajarImagenStorage();
                        Toast.makeText(getActivity(), "Has Aceptado", Toast.LENGTH_LONG).show();
                        Log.d("spinner", actividadTexto);
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
                        //leerDatosFB();
                    }
                });
                builder.setNeutralButton("Cargar Foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cargarFoto();
                        //eliminarDatosFB();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_CAMERA){
            if(permissions.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                goTocamera();
            } else {
                Toast.makeText(getActivity(), "Necesitar activar permisos.", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap =(Bitmap) data.getExtras().get("data");
            imagen.setImageBitmap(bitmap);
            Log.d("IMG", "Resultado:"+bitmap);
        } else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            txtImagen = data.getData();
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
        }
    }

    public void subirImagenStorage(){
        StorageReference filepath = storage.child("fotos").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                Log.d("up", "LA IMAGEN SE HA SUBIDO CORECTAMENTE.");
            }
        });
    }

    String url;

    public void bajarImagenStorage(){
        FirebaseStorage down = FirebaseStorage.getInstance();
        StorageReference imageRef = down.getReference().child("fotos").child("21147");
        imageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory
                                .decodeByteArray(bytes, 0, bytes.length);
                        imagen.setImageBitmap(bitmap);
                    }
                });
    }

    public void goTocamera() {
        Intent hacerFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(hacerFoto.resolveActivity(getContext().getPackageManager())!=null){
            startActivityForResult(hacerFoto, 1);
        }
    }

    public void hacerFoto() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                goTocamera();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            }
        } else {
            goTocamera();
        }
    }

    public void cargarFoto() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    Random r = new Random();
    int idEvento = r.nextInt(1000)+1;
    int idUsuario = r.nextInt(1000)+1;

    public void crearEventos(){
        Map<String, Object> evento = new HashMap<>();
        evento.put("idEvento", idEvento);
        evento.put("idUsuario", idUsuario);
        evento.put("nombre", txtLugar);
        evento.put("dia", (String)dateLog);
        evento.put("Longitud", lon);
        evento.put("Latitud", lat);
        evento.put("descripcion", txtDescripcion);
        evento.put("tesoro", tesoro);
        evento.put("pista", txtPista);
        evento.put("poblacion", txtZona);
        evento.put("tipoActividad", actividadTexto);
        evento.put("imagen", url);
        DatabaseReference dr = fb.getReference().child("evento");
        dr.setValue(evento);
        db.collection("evento").add(evento);
    }

    /*public void leerDatosFB(){
        Query recuperar = (Query) data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String valor = (String) snapshot.getValue();
                get_fecha.setText(valor);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("TAGLOG", "Error!", error.toException());
            }
        });
    }

    public void eliminarDatosFB(){
        db.collection("evento").document("7Kr1STuyuIIcVNNMAXcW").delete();
    }*/
}