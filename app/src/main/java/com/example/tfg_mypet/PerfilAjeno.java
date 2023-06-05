package com.example.tfg_mypet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PerfilAjeno<ImagenView> extends AppCompatActivity {

    TextView nombre, email, telefono, contP, contF,  favs, publicaciones, aviso;
    RecyclerView recyclerView;
    ArrayList<String> nombreList, edad, genero, raza, emailDueño, descripcion, imagen, tamaño;
    ArrayList <Integer> id;
    BBDD miBBD;
    AnimalAdapter miAdapter;
    ImageView imgPerfil;
    Boolean publiVisible = true;
    public String Semail;
    public int idUsuario;
    //Barras naranjas
    ConstraintLayout barraPub, barraFav;

    int contadorAnimales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_ajeno);
        miBBD = new BBDD(this);
        id = new ArrayList<>();
        nombreList = new ArrayList<>();
        edad = new ArrayList<>();
        genero = new ArrayList<>();
        raza = new ArrayList<>();
        descripcion = new ArrayList<>();
        imagen = new ArrayList<>();
        tamaño = new ArrayList<>();
        recyclerView = findViewById(R.id.recycledPerfilAjeno);
        miAdapter = new AnimalAdapter(this, id, nombreList, raza, genero, edad, descripcion, imagen, getIntent().getStringExtra("email"), getIntent().getStringExtra("emailUsuario"), tamaño);

        recyclerView.setAdapter(miAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        BBDD miBBDD= new BBDD(this);
        SQLiteDatabase db = miBBDD.getReadableDatabase();

        //nombre
        String correoElectronico = getIntent().getStringExtra("email");
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
        Semail = getIntent().getStringExtra("email");
        idUsuario = miBBDD.getIdUsuario(Semail);
        email.setText(Semail);
        contP = findViewById(R.id.countPublicaciones);



        publicaciones = findViewById(R.id.txtPublicaciones);

        barraPub = findViewById(R.id.barraPublicacion);
        aviso = findViewById(R.id.avisoPubli);


        id.clear();
        nombreList.clear();
        edad.clear();
        genero.clear();
        raza.clear();
        descripcion.clear();
        tamaño.clear();
        imagen.clear();
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

        }


        displaydata(Semail);
    }

    @Override
    protected void onResume() {
        super.onResume();
            displaydata(Semail);
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
            return;
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


}