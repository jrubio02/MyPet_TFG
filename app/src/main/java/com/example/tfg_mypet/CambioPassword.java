package com.example.tfg_mypet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CambioPassword extends AppCompatActivity {
    TextView primera, segunda;
    ImageView check, cruz;
    String emailDueño;
    BBDD miBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        miBBDD = new BBDD(this);
        primera = findViewById(R.id.primeraPass);
        segunda = findViewById(R.id.segundaPass);
        check = findViewById(R.id.check);
        cruz = findViewById(R.id.cruz);
        emailDueño = getIntent().getStringExtra("emailUsuario");

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Sprimera = primera.getText().toString();
                String Ssegunda = segunda.getText().toString();
                if (!Sprimera.equals(Ssegunda)) {
                    Toast.makeText(CambioPassword.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (Sprimera.length() < 10) {
                    Toast.makeText(CambioPassword.this, "Contraseña demasiado corta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CambioPassword.this, "Contraseña restablecida", Toast.LENGTH_SHORT).show();
                    miBBDD.cambiarPassword(emailDueño, Sprimera);
                    Intent register = new Intent(CambioPassword.this, Perfil.class);

                    register.putExtra("emailUsuario", emailDueño);
                    startActivity(register);
                }
            }
        });

        cruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(CambioPassword.this, EditarPerfil.class);
                register.putExtra("emailUsuario", emailDueño);
                startActivity(register);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EditarPerfil.class);
        intent.putExtra("emailUsuario", emailDueño);
        startActivity(intent);
        finish(); // Opcionalmente, puedes llamar a finish() si no deseas mantener la actividad actual en la pila de actividades.
    }

}
