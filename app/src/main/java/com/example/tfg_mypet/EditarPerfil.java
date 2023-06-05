package com.example.tfg_mypet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditarPerfil extends AppCompatActivity {
    String nombre, apellidos;
    int telefono;
    TextView tNombre, tApellidos, tTelefono, eliminarCuenta, cambiarPassword, cerrarSesion;
    ImageView check, cruz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        tNombre = findViewById(R.id.editNombre);
        tApellidos = findViewById(R.id.editApellidos);
        tTelefono = findViewById(R.id.editTelefono);
        check = findViewById(R.id.check);
        cruz  = findViewById(R.id.cruz);

        eliminarCuenta = findViewById(R.id.eliminarCuenta);
        cambiarPassword = findViewById(R.id.cambiarPassword);
        cerrarSesion = findViewById(R.id.cerrarSesion);

        String correoElectronico = getIntent().getStringExtra("emailUsuario");
        BBDD miBBDD = new BBDD(this);
        Cursor miCursor = miBBDD.getDatosUsuario(correoElectronico);

        if (miCursor.getCount() == 0) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            miCursor.moveToFirst(); // Mover el cursor a la primera fila
            nombre = miCursor.getString(0);
            apellidos = miCursor.getString(1);
            telefono = miCursor.getInt(2);

            tNombre.setText(nombre);
            tApellidos.setText(apellidos);
            tTelefono.setText(String.valueOf(telefono));
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Obtén el email del dueño desde alguna fuente (por ejemplo, un método o un campo de clase)

                String nuevoNombre = tNombre.getText().toString();
                String nuevosApellidos = tApellidos.getText().toString();
                int nuevoTelefono = Integer.parseInt(tTelefono.getText().toString());

                boolean actualizacionExitosa = miBBDD.updateUser(correoElectronico, nuevoNombre, nuevosApellidos, nuevoTelefono);
                if (actualizacionExitosa) {
                    Toast.makeText(getApplicationContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarPerfil.this, Perfil.class);
                    intent.putExtra("emailUsuario", correoElectronico);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, Perfil.class);
                intent.putExtra("emailUsuario", correoElectronico);
                startActivity(intent);
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPerfil.this);
                builder.setTitle("Cerrar sesión")
                        .setMessage("¿Desea cerrar la sesión?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acción a realizar después de la confirmación
                                Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .show();
            }
        });
        eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPerfil.this);
                builder.setTitle("Confirmación")
                        .setMessage("¿Estás seguro de que quieres borrar la cuenta?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acción a realizar después de la confirmación

                                int idUsuario = miBBDD.getIdUsuario(correoElectronico);
                                miBBDD.borrarCuenta(correoElectronico);
                                miBBDD.borrarCuentaFavs(idUsuario);
                                miBBDD.borrarCuentaPublicaciones(idUsuario);

                                Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .show();

            }
        });
        cambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, CambioPassword.class);
                intent.putExtra("emailUsuario", correoElectronico);
                startActivity(intent);
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
