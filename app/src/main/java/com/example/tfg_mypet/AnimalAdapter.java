package com.example.tfg_mypet;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> nombre, raza, genero, edad, descripcion, imagen, tamaño;
    private ArrayList<Integer> id;
    int miPosicion;
    private String email, emailUsuario;

    public AnimalAdapter(Context context, ArrayList<Integer> id, ArrayList<String> nombre, ArrayList<String> raza, ArrayList<String> genero, ArrayList<String> edad, ArrayList<String> descripcion, ArrayList<String> imagen, String email, String emailUsuario, ArrayList<String> tamaño) {

        this.context = context;
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.genero = genero;
        this.edad = edad;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.email = email;
        this.emailUsuario = emailUsuario;
        this.tamaño = tamaño;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_animal,  parent, false);

        return new MyViewHolder(v);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int pos = holder.getAbsoluteAdapterPosition();
        holder.id.setText(String.valueOf(id.get(position)));

        holder.nombre.setText(nombre.get(position));
        holder.raza.setText(raza.get(position));
        holder.genero.setText(genero.get(position));

        int edadInt = Integer.parseInt(edad.get(position));
        if (edadInt < 12) {
            if (edadInt == 1) {
                holder.edad.setText(edad.get(position) + " mes");
            } else {
                holder.edad.setText(edad.get(position) + " meses");
            }
        } else {
            int edadAnios = (int) Math.floor(edadInt / 12.0);
            if (edadAnios == 1) {
                holder.edad.setText(edadAnios + " año");
            } else {
                holder.edad.setText(edadAnios + " años");
            }
        }

        Glide.with(context)
                .load(imagen.get(position))
                .placeholder(R.drawable.logo_mypet)
                .into(holder.imagen);



        holder.perro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPosition = holder.getAbsoluteAdapterPosition();
                Intent intent = new Intent(context, DetallesAnimal.class);

                intent.putExtra("id", id.get(currentPosition));
                intent.putExtra("nombre", nombre.get(currentPosition));
                intent.putExtra("raza", raza.get(currentPosition));
                intent.putExtra("edad", edad.get(currentPosition));
                intent.putExtra("genero", genero.get(currentPosition));
                intent.putExtra("descripcion", descripcion.get(currentPosition));
                intent.putExtra("imagen", imagen.get(currentPosition));
                intent.putExtra("email", email);
                intent.putExtra("emailUsuario", emailUsuario);
                intent.putExtra("tamaño", tamaño.get(currentPosition));


                context.startActivity(intent);
            }

        });


        // Modificar la distancia vertical entre elementos
        // Modificar la distancia vertical entre elementos
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        int heightInDp = 280; // Cambiar la altura en dp aquí
        int heightInPx = (int) (heightInDp * context.getResources().getDisplayMetrics().density);
        params.height = heightInPx;
        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return nombre.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, raza, edad, genero, id, tamaño;
        ImageView imagen;
        ConstraintLayout perro;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.animalNombre);
            raza = itemView.findViewById(R.id.animalRaza);
            edad = itemView.findViewById(R.id.animalEdad);
            genero = itemView.findViewById(R.id.animalGenero);
            id = itemView.findViewById(R.id.id);
            imagen = itemView.findViewById(R.id.imagen);
            perro = itemView.findViewById(R.id.paginaDetalles);
            tamaño = itemView.findViewById(R.id.perroTamaño);
        }
    }
}
