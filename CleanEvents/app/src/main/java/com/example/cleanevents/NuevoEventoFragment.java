package com.example.cleanevents;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.StorageReferenceUri;
import com.google.firestore.v1.WriteResult;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NuevoEventoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuevoEventoFragment extends Fragment {

    TextView get_fecha;
    EditText lugar, descripcion, zona, pista;
    Button btnFecha, btnGuardar, btnCargarFoto, btnMap;
    ImageView imagen;
    Switch anadir_tesoro;
    String txtLugar, txtDescripcion, txtZona, txtPista;

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
    StorageReference storage = FirebaseStorage.getInstance().getReference();

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
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView, new MapsFragment()).addToBackStack(null).commit();
            }
        });



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
                        crearEventos();
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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("image/240");

        //Glide.with(getActivity()).load();
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

    public void bajarImagenStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("image/*");

        //Glide.with(getActivity()).load(new FirebaseImage)
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

    public void crearEventos(){
        Map<String, Object> evento = new HashMap<>();
        evento.put("lugar", txtLugar);
        evento.put("fecha", (String)dateLog);
        evento.put("descripcion", txtDescripcion);
        evento.put("tesoro", tesoro);
        evento.put("pista", txtPista);
        evento.put("zona", txtZona);
        evento.put("actividad", actividadTexto);
        //evento.put("imagen", bajarImagenStorage(path));
        DatabaseReference dr = fb.getReference().child("evento");
        dr.setValue(evento);
        db.collection("evento").add(evento);
    }
}