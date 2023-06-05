package com.example.tfg_mypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class register_animal extends AppCompatActivity {

    private Spinner tamaños, genero, tipo, edad;
    TextView tNombre, tRaza, tDescripcion, tEdad;
    String sNombre, sRaza, sDescripcion, numeroEdad, sTamaño, sGenero, sTipo, añomesEdad, urlImagen;
    ;
    Button insertarAnimal;

    ImageView imgAnimal;

    // private FirebaseFirestore mfirestore;
    //private FirebaseAuth mAuth;
    StorageReference storageReference;
    String path = "pet/";
    String nombreImagen = "";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri imagenURL;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_register_animal);
        BBDD miBBDD = new BBDD(this);
        tamaños = (Spinner) findViewById(R.id.spinnerTamaño);
        genero = (Spinner) findViewById(R.id.spinnerGenero);
        tipo = (Spinner) findViewById(R.id.spinnerTipo);
        edad = (Spinner) findViewById(R.id.spinnerEdad);
        insertarAnimal = findViewById(R.id.registrar);

        tNombre = findViewById(R.id.registerNombre);
        tRaza = findViewById(R.id.raza);
        tDescripcion = findViewById(R.id.descripcionAnimal);
        tEdad = findViewById(R.id.edad);

        imgAnimal = findViewById(R.id.imgAnimal);

        String emailDueño = getIntent().getStringExtra("emailUsuario");
        storageReference = FirebaseStorage.getInstance().getReference();

        String [] Stamaños = {"Pequeño", "Mediano", "Grande"};
        String [] Sgenero = {"Macho", "Hembra"};
        String [] Stipo = {"Perro", "Gato", "Pez", "Ave", "Hamster"};
        String [] Sedad = {"Años", "Meses"};



        ArrayAdapter <String> arrayTamaños = new ArrayAdapter<String>(this, R.layout.spinner_item, Stamaños);
        ArrayAdapter <String> arrayGeneros = new ArrayAdapter<String>(this, R.layout.spinner_item, Sgenero);
        ArrayAdapter <String> arrayTipos   = new ArrayAdapter<String>(this, R.layout.spinner_item, Stipo);
        ArrayAdapter <String> arrayEdad    = new ArrayAdapter<String>(this, R.layout.spinner_item, Sedad);
        tamaños.setAdapter(arrayTamaños);
        genero.setAdapter(arrayGeneros);
        tipo.setAdapter(arrayTipos);
        edad.setAdapter(arrayEdad);


        imgAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirFoto();
            }

            private void subirFoto(){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, COD_SEL_IMAGE);

            }

        });

        insertarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sNombre = tNombre.getText().toString();
                sRaza = tRaza.getText().toString();
                sDescripcion = tDescripcion.getText().toString();
                String numeroEdad = tEdad.getText().toString();
                int idDueño = getIntent().getIntExtra("id", 1);

                int edadnumero = Integer.parseInt(numeroEdad);
                if(edad.getSelectedItem().toString().equals("Años"))
                {
                    edadnumero = edadnumero * 12;
                }

                //spinners
                sTamaño = tamaños.getSelectedItem().toString();
                sGenero = genero.getSelectedItem().toString();
                sTipo = tipo.getSelectedItem().toString();
                añomesEdad = edad.getSelectedItem().toString();
                Boolean insert = miBBDD.insertarAnimal(idDueño, sNombre, sRaza, sGenero, sTamaño, edadnumero, sTipo, sDescripcion, urlImagen);
                Intent intent = new Intent(register_animal.this, Perfil.class);
                intent.putExtra("emailUsuario", emailDueño);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK && data != null) {
            // La imagen se seleccionó correctamente
            Uri imageUri = data.getData();

            // Obtener la fecha actual del sistema
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fechaActual = dateFormat.format(calendar.getTime());

            // Crear el nombre de la imagen con la fecha actual
            nombreImagen = "animal_" + fechaActual  + ".png";

            // Sube la imagen a Firebase Storage y guarda su URL en la base de datos
            subirImagenAFirebase(imageUri);
        }
    }

    private void subirImagenAFirebase(Uri imageUri) {
        // Obtiene la referencia al almacenamiento de Firebase
        StorageReference storageRef = storageReference.child(path + nombreImagen);

        // Sube la imagen al almacenamiento de Firebase
        UploadTask uploadTask = storageRef.putFile(imageUri);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo imagen...");
        progressDialog.show();

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // La imagen se subió exitosamente
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Obtiene la URL de descarga de la imagen
                        imagenURL = uri;

                        // Guarda la URL en la base de datos
                        urlImagen = imagenURL.toString();

                        progressDialog.dismiss();

                        Glide.with(register_animal.this)
                                .load(imageUri)
                                .into(imgAnimal);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al subir la imagen
                progressDialog.dismiss();
                Toast.makeText(register_animal.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
        finish(); // Opcionalmente, puedes llamar a finish() si no deseas mantener la actividad actual en la pila de actividades.
    }

}