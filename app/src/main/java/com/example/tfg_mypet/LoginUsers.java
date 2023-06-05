package com.example.tfg_mypet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginUsers extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;
    BBDD miBBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_login); // Inflar el diseño de la actividad


        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.editarPerfil);
        TextView txtRegistro =  findViewById(R.id.txtRegistro);
        miBBDD = new BBDD(this);

        txtRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginUsers.this, registerUser.class);
                startActivity(register);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String email = loginEmail.getText().toString();
               String password = loginPassword.getText().toString();

               if(email.equals("")|| password.equals(""))
               {
                   Toast.makeText(LoginUsers.this, "Por favor, rellene los campos", Toast.LENGTH_SHORT).show();
               }
               else {

                   Boolean checkUserPass = miBBDD.comprobarEmailPassword(email, password);
                   if(checkUserPass == true)
                   {

//                       Toast.makeText(LoginUsers.this, "Login correcto", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(LoginUsers.this, PantallaPpal.class);
                       intent.putExtra("email", loginEmail.getText().toString());
                       intent.putExtra("emailUsuario", loginEmail.getText().toString());
                       intent.putExtra("password", loginPassword.getText().toString());

                       startActivity(intent);

                   }
                   else
                   {
                       Toast.makeText(LoginUsers.this, "Login incorrecto", Toast.LENGTH_SHORT).show();
                   }
               }



            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Opcionalmente, puedes llamar a finish() si no deseas mantener la actividad actual en la pila de actividades.
    }

}