package com.example.tfg_mypet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Perfil<ImagenView> extends AppCompatActivity {

    TextView nombre, email, telefono, contP, contF,  favs, publicaciones, aviso;
    RecyclerView recyclerView;
    ArrayList<String> nombreList, edad, genero, raza, emailDueño, descripcion, imagen,tamaño;
    ArrayList <Integer> id;
    BBDD miBBD;
    Button editar;
    AnimalAdapter miAdapter;
    ImageView imgPerfil;
    Boolean publiVisible = true;
    public String Semail;
    public int idUsuario;
    //Barras naranjas
    ConstraintLayout barraPub, barraFav;
    FloatingActionButton plus;

    int contadorAnimales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        miBBD = new BBDD(this);
        id = new ArrayList<>();
        nombreList = new ArrayList<>();
        edad = new ArrayList<>();
        genero = new ArrayList<>();
        raza = new ArrayList<>();
        descripcion = new ArrayList<>();
        imagen = new ArrayList<>();
        recyclerView = findViewById(R.id.recycledPerfil);
        tamaño = new ArrayList<>();
        miAdapter = new AnimalAdapter(this, id, nombreList, raza, genero, edad, descripcion, imagen, getIntent().getStringExtra("emailemail"), getIntent().getStringExtra("emailUsuario"), tamaño);

        recyclerView.setAdapter(miAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        BBDD miBBDD= new BBDD(this);
        SQLiteDatabase db = miBBDD.getReadableDatabase();
        editar = findViewById(R.id.editarPerfil);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, EditarPerfil.class);
                intent.putExtra("emailUsuario", Semail);
                startActivity(intent);
            }
        });
        //nombre
        String correoElectronico = getIntent().getStringExtra("emailUsuario");
        String[] columns = {"nombre", "apellidos", "telefono"};
        String selection = "email = ?";
        String[] selectionArgs = {correoElectronico};
        Cursor cursor = db.query("Usuario", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String Snombre = cursor.getString(cursor.getColumnIndex("nombre"));
            @SuppressLint("Range") String Sapellidos = cursor.getString(cursor.getColumnIndex("apellidos"));
            @SuppressLint("Range") String Stelefono = cursor.getString(cursor.getColumnIndex("telefono"));

            // Usa la información obtenida como quieras, por ejemplo:
            nombre = findViewById(R.id.nombre);
            nombre.setText(Snombre + " " + Sapellidos);

            telefono = findViewById(R.id.telefono);
            telefono.setText(Stelefono);
        }

       //boolean esFav = miBBD.isFavorito()
        //telefono
        cursor.close();
        db.close();

        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.correo);
        Semail = getIntent().getStringExtra("emailUsuario");
        idUsuario = miBBDD.getIdUsuario(Semail);
        email.setText(Semail);
        contP = findViewById(R.id.countPublicaciones);
        contF = findViewById(R.id.countFavs);

        favs =  findViewById(R.id.txtFavs);
        publicaciones = findViewById(R.id.txtPublicaciones);
        barraFav = findViewById(R.id.barraFavoritos);
        barraPub = findViewById(R.id.barraPublicacion);
        aviso = findViewById(R.id.avisoPubli);
        plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, register_animal.class);
                intent.putExtra("id", idUsuario);
                intent.putExtra("emailUsuario", Semail);
                startActivity(intent);
            }
        });


            id.clear();
            nombreList.clear();
            edad.clear();
            genero.clear();
            raza.clear();
            descripcion.clear();
            imagen.clear();
            tamaño.clear();
            cursor = miBBD.getFavoritos(idUsuario);

            if(cursor.getCount()==0)
            {

                return;
            }
            else
            {
                while(cursor.moveToNext())
                {
                    contadorAnimales = cursor.getCount();
                }
                contF.setText(String.valueOf(contadorAnimales));
            }


        favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //naranjas
                favs.setTextColor(Color.parseColor("#FD4635"));
                contF.setTextColor(Color.parseColor("#FD4635"));
                barraFav.setBackgroundColor(Color.parseColor("#FD4635"));

                //negros
                publicaciones.setTextColor(Color.parseColor("#FF000000"));
                contP.setTextColor(Color.parseColor("#FF000000"));
                barraPub.setBackgroundColor(Color.parseColor("#FF000000"));
                displayFavs(idUsuario);
                barraFav.setVisibility(View.VISIBLE);
                barraPub.setVisibility(View.INVISIBLE);
                publiVisible = false;
                aviso.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);


            }
        });



        publicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaydata(Semail);
                barraFav.setVisibility(View.INVISIBLE);
                barraPub.setVisibility(View.VISIBLE);
                publiVisible = true;
                //naranjas
                favs.setTextColor(Color.parseColor("#FF000000"));
                contF.setTextColor(Color.parseColor("#FF000000"));
                barraFav.setBackgroundColor(Color.parseColor("#FF000000"));


                //negros
                publicaciones.setTextColor(Color.parseColor("#FD4635"));
                contP.setTextColor(Color.parseColor("#FD4635"));
                barraPub.setBackgroundColor(Color.parseColor("#FD4635"));

            }
        });
        displaydata(Semail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Realiza las acciones necesarias para actualizar la información de la página anterior
        // por ejemplo, volver a cargar datos desde una base de datos o actualizar vistas
        if(publiVisible)
        {
            displaydata(Semail);
        }
        else
        {
            displayFavs(idUsuario);
        }
    }
    private void displaydata(String emailDueño) {
        id.clear();
        nombreList.clear();
        edad.clear();
        genero.clear();
        raza.clear();
        descripcion.clear();
        imagen.clear();
        tamaño.clear();
        Cursor cursor = miBBD.getPublicacionesDueño(emailDueño);


        if(cursor.getCount()==0)
        {

            aviso.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }
        else
        {
            aviso.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            while(cursor.moveToNext())
            {
                id.add(cursor.getInt(0));
                nombreList.add(cursor.getString(2));
                raza.add(cursor.getString(3));
                genero.add(cursor.getString(4));
                tamaño.add(cursor.getString(5));
                edad.add(cursor.getString(6));
                descripcion.add(cursor.getString(8));
                imagen.add((cursor.getString(9)));

                contadorAnimales = cursor.getCount();
            }
            contP.setText(String.valueOf(contadorAnimales));

        }
        miAdapter.notifyDataSetChanged();
    }

    private void displayFavs(int idUsuario) {
        id.clear();
        nombreList.clear();
        edad.clear();
        genero.clear();
        raza.clear();
        descripcion.clear();
        imagen.clear();
        tamaño.clear();
        Cursor cursor = miBBD.getFavoritos(idUsuario);


        if(cursor.getCount()==0)
        {


        }
        else
        {

            while(cursor.moveToNext())
            {
                id.add(cursor.getInt(0));
                nombreList.add(cursor.getString(2));
                raza.add(cursor.getString(3));
                genero.add(cursor.getString(4));
                tamaño.add(cursor.getString(5));
                edad.add(cursor.getString(6));
                descripcion.add(cursor.getString(8));
                imagen.add((cursor.getString(9)));

                contadorAnimales = cursor.getCount();
            }
            contF.setText(String.valueOf(contadorAnimales));

        }
        miAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PantallaPpal.class);
        intent.putExtra("emailUsuario", Semail);
        startActivity(intent);
        finish(); // Opcionalmente, puedes llamar a finish() si no deseas mantener la actividad actual en la pila de actividades.
    }

}