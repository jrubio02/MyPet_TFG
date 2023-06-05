package com.example.tfg_mypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetallesAnimal extends AppCompatActivity {
    TextView nombre, edad, genero, raza, detalles, email, perfil, contadorFavs, tamaño;
    String Snombre, Sraza, Sedad, Sgenero, Sdetalles, Surl, Semail, SemailDueño, Stamaño;
    int SidAnimal, idDueño;
    ImageView miImagen, estrellaContador, lapiz;
    private String emailDueño;
    FloatingActionButton estrella, papelera;
    BBDD miBBDD;
    Boolean animalFav = false;
    Boolean animalDeDueño = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_animal);

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        miBBDD = new BBDD(this);
        nombre = findViewById(R.id.perroNombre);
        edad = findViewById(R.id.perroEdad);
        genero = findViewById(R.id.perroGenero);
        raza = findViewById(R.id.perroRaza);
        detalles = findViewById(R.id.perroDescripcion);
        email = findViewById(R.id.email);
        miImagen = findViewById(R.id.imagenAnimal);
        perfil = findViewById(R.id.txtPerfilDueño);
        estrella = findViewById(R.id.btnFav);
        tamaño = findViewById(R.id.perroTamaño);
        papelera = findViewById(R.id.btnPapelera);
        contadorFavs = findViewById(R.id.countFavs);
        estrellaContador = findViewById(R.id.estrellaCount);
        lapiz = findViewById(R.id.lapiz);

        getIntentData();
        idDueño =  miBBDD.getIdUsuario(SemailDueño);
        //saber si el animal esta en los fav de ese usuario
        animalFav = miBBDD.isFavorito(idDueño, SidAnimal);
        if(animalFav)
        {
            estrella.setImageResource(R.drawable.estrella_llena);
        }
        else
        {
            estrella.setImageResource(R.drawable.estrella_vacia);
        }

        animalDeDueño = miBBDD.isPublicacionPropia(idDueño, SidAnimal);
        if(animalDeDueño)
        {
            int contador = miBBDD.contarFavoritos(SidAnimal);
            contadorFavs.setText(String.valueOf(contador));
            contadorFavs.setVisibility(View.VISIBLE);
            lapiz.setVisibility(View.VISIBLE);
            estrellaContador.setVisibility(View.VISIBLE);
            perfil.setVisibility(View.INVISIBLE);
            estrella.setVisibility(View.INVISIBLE);
            papelera.setVisibility(View.VISIBLE);
        }

        papelera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetallesAnimal.this);
                builder.setTitle("Confirmación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta publicación?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acción a realizar después de la confirmación

                                miBBDD.eliminarPublicación(SidAnimal);
                                miBBDD.borrarAnimal(SidAnimal);
                                Intent intent = new Intent(DetallesAnimal.this, Perfil.class);
                                intent.putExtra("emailUsuario", SemailDueño);
                                startActivity(intent);
                                Toast.makeText(DetallesAnimal.this, "Publicación eliminada", Toast.LENGTH_SHORT).show();
                                recreate();

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acción a realizar si se cancela la confirmación (opcional)

                            }
                        })
                        .show();
            }
        });

        lapiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesAnimal.this, EditarAnimal.class);
                intent.putExtra("idAnimal", SidAnimal);
                intent.putExtra("emailDueño", emailDueño);
                startActivity(intent);
            }
        });
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesAnimal.this, PerfilAjeno.class);
                intent.putExtra("email", emailDueño);

                startActivity(intent);
            }
        });


    }

    void getIntentData() {
        if(getIntent().hasExtra("nombre") && getIntent().hasExtra("raza") && getIntent().hasExtra("edad") && getIntent().hasExtra("genero") && getIntent().hasExtra("id") && getIntent().hasExtra("descripcion")  && getIntent().hasExtra("tamaño")) {
            Snombre = getIntent().getStringExtra("nombre");
            Sraza = getIntent().getStringExtra("raza");
            Sedad = getIntent().getStringExtra("edad");
            Sgenero = getIntent().getStringExtra("genero");
            Sdetalles = getIntent().getStringExtra("descripcion");
            SidAnimal = getIntent().getIntExtra("id", -1);
            Surl = getIntent().getStringExtra("imagen");
            Semail = getIntent().getStringExtra("email");
            SemailDueño =  getIntent().getStringExtra("emailUsuario");
            Stamaño =  getIntent().getStringExtra("tamaño");

            BBDD miBBDD = new BBDD(this);
             emailDueño = miBBDD.getEmailDueño(SidAnimal);

            nombre.setText(Snombre);
            raza.setText("Raza: " + Sraza);

            int edadInt = Integer.parseInt(Sedad);
            if (edadInt < 12) {
                edad.setText("Edad: " + Sedad + " meses");
            } else {
                int edadAnios = (int) Math.floor(edadInt / 12.0);
                edad.setText("Edad: " + edadAnios + " años");
            }

            genero.setText("Género: " + Sgenero);
            tamaño.setText("Tamaño: " + Stamaño);

            detalles.setText(Sdetalles);

            // También puedes mostrar el ID del dueño si lo necesitas
            email.setText(emailDueño);

            // Carga la imagen usando Glide
            Glide.with(this).load(Surl).into(miImagen);

            //favoritos
            estrella.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(animalFav)
                    {
                        miBBDD.eliminarFavorito(idDueño, SidAnimal);
                        animalFav = false;
                        estrella.setImageResource(R.drawable.estrella_vacia);
                        Toast.makeText(DetallesAnimal.this, "Animal eliminado de favoritos", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        miBBDD.añadirFavorito(idDueño, SidAnimal);
                        animalFav = true;
                        estrella.setImageResource(R.drawable.estrella_llena);
                        Toast.makeText(DetallesAnimal.this, "Animal añadido a favoritos", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
        else {
            Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
        }
    }

}