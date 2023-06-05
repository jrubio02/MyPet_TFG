package com.example.tfg_mypet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PantallaPpal<ImagenView> extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList <String> nombre, edad, genero, raza, emailDueño, descripcion, imagen, tamaño;
    ArrayList <Integer> id;
    BBDD miBBD;
    AnimalAdapter miAdapter;
    ImageView imgPerfil;
    int idDueño;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ppal);


        miBBD = new BBDD(this);
        id = new ArrayList<>();
        nombre = new ArrayList<>();
        edad = new ArrayList<>();
        genero = new ArrayList<>();
        raza = new ArrayList<>();
        descripcion = new ArrayList<>();
        imagen = new ArrayList<>();
        tamaño = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        miAdapter = new AnimalAdapter(this, id, nombre, raza, genero, edad, descripcion, imagen, getIntent().getStringExtra("email"),getIntent().getStringExtra("emailUsuario"), tamaño );

        recyclerView.setAdapter(miAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        ImageView imgPerro = findViewById(R.id.categoriaPerro);
        ImageView imgGato = findViewById(R.id.categoriaGato);
        ImageView imgPez = findViewById(R.id.categoriaPez);
        ImageView imgAve = findViewById(R.id.categoriaPajaro);
        ImageView imgHamster = findViewById(R.id.categoriaHamster);
        TextView miCategoria = findViewById(R.id.txtCategoria);
        //primero ejecuto los perros
        displaydata("Perro");
        View.OnClickListener imageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPerro.setImageResource(R.drawable.perro_negro);
                imgGato.setImageResource(R.drawable.gato);
                imgAve.setImageResource(R.drawable.ave);
                imgHamster.setImageResource(R.drawable.hamster);
                imgPez.setImageResource(R.drawable.pez);
                switch (v.getId()) {
                    case R.id.categoriaPerro:
                        miCategoria.setText("Perros");
                        imgPerro.setImageResource(R.drawable.perro);
                        displaydata("Perro");

                        break;
                    case R.id.categoriaGato:
                        miCategoria.setText("Gatos");
                        imgGato.setImageResource(R.drawable.gato_naranja);
                        displaydata("Gato");
                        break;
                    case R.id.categoriaPez:
                        miCategoria.setText("Peces");
                        imgPez.setImageResource(R.drawable.pez_naranja);
                        displaydata("Pez");
                        break;
                    case R.id.categoriaPajaro:
                        miCategoria.setText("Aves");
                        imgAve.setImageResource(R.drawable.pajaro_naranja);
                        displaydata("Ave");
                        break;
                    case R.id.categoriaHamster:
                        miCategoria.setText("Hamsters");
                        imgHamster.setImageResource(R.drawable.hamster_naranja);
                        displaydata("Hamster");
                        break;
                }
            }
        };
        imgPerro.setOnClickListener(imageClickListener);
        imgGato.setOnClickListener(imageClickListener);
        imgPez.setOnClickListener(imageClickListener);
        imgAve.setOnClickListener(imageClickListener);
        imgHamster.setOnClickListener(imageClickListener);

        imgPerfil =  findViewById(R.id.btnPerfil);



        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPpal.this, Perfil.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("emailUsuario", getIntent().getStringExtra("emailUsuario"));
                startActivity(intent);
            }


        });
    }

    private void displaydata(String tipoAnimal) {
        id.clear();
        nombre.clear();
        edad.clear();
        genero.clear();
        raza.clear();
        descripcion.clear();
        imagen.clear();
        tamaño.clear();
        idDueño = miBBD.getIdUsuario(getIntent().getStringExtra("email"));
        Cursor cursor = miBBD.getData(tipoAnimal, idDueño);

        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {

            while(cursor.moveToNext())
            {
                id.add(cursor.getInt(0));
                nombre.add(cursor.getString(2));
                raza.add(cursor.getString(3));
                genero.add(cursor.getString(4));
                tamaño.add(cursor.getString(5));
                edad.add(cursor.getString(6));
                descripcion.add(cursor.getString(8));
                imagen.add((cursor.getString(9)));

            }
        }
        miAdapter.notifyDataSetChanged();
    }

    private static final int INTERVALO_TIEMPO_SALIDA = 2000; // Intervalo de tiempo en milisegundos
    private long tiempoPrimerClick = 0;

    @Override
    public void onBackPressed() {
        // Obtener el tiempo actual
        long tiempoActual = System.currentTimeMillis();

        // Verificar si es el primer clic o no
        if (tiempoActual - tiempoPrimerClick > INTERVALO_TIEMPO_SALIDA) {
            // Si es el primer clic, mostrar un mensaje o realizar alguna acción
            Toast.makeText(this, "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();

            // Actualizar el tiempo del primer clic
            tiempoPrimerClick = tiempoActual;
        } else {
            // Si es el segundo clic dentro del intervalo de tiempo, cerrar la aplicación
            finishAffinity();
        }
    }



}