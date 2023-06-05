package com.example.tfg_mypet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BBDD extends SQLiteOpenHelper {


    public static final String miBBDD = "BBDD.db";

    public BBDD(Context context) {
        super(context, "BBDD.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table IF NOT EXISTS Usuario" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre text NOT NULL," +
                "apellidos text NOT NULL," +
                "email text NOT NULL UNIQUE," +
                "password text NOT NULL," +
                "telefono integer NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Animal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idDueño INTEGER NOT NULL," +
                "nombre TEXT NOT NULL," +
                "raza TEXT," +
                "genero TEXT," +
                "tamaño TEXT NOT NULL," +
                "edad INTEGER," +
                "tipo TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "imagen TEXT NOT NULL," +
                "FOREIGN KEY(idDueño) REFERENCES Usuario(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS Favoritos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "idAnimal INTEGER," +
                "FOREIGN KEY (idUsuario) REFERENCES Usuario(id)," +
                "FOREIGN KEY (idAnimal) REFERENCES Animal(id));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists Usuario");

        db.execSQL("drop table if exists Animal");

    }

    public Boolean registerUser(String nombre, String apellidos, String email, String password, int telefono)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre", nombre);
        valores.put("apellidos", apellidos);
        valores.put("email", email);
        valores.put("password", password);
        valores.put("telefono", telefono);

        long result = db.insert("Usuario", null, valores);
        if(result==-1)
        {
            return false;
        }
        else
            return true;

    }

    public Boolean comprobarEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Usuario where email = ?", new String[]{email});
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }

    public Boolean comprobarEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Usuario where email = ? and password = ?", new String[]{email, password});
        if(cursor.getCount()>0)
        {
            return true;
        }
        else
            return false;
    }
    
    public Cursor getData(String tipoAnimal, int idDueño)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Animal where tipo = '" + tipoAnimal + "' AND idDueño <> " + idDueño, null);
        return cursor;
    }

    public Cursor getPublicacionesDueño(String emailDueño)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.* " +
                "FROM Animal a " +
                "JOIN Usuario u " +
                "ON a.iddueño = u.id " +
                "WHERE u.email = '" + emailDueño + "'", null);

        return cursor;
    }

    public Cursor getFavoritos(int idDueño)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.id, a.iddueño, a.nombre, a.raza, a.genero, a.tamaño, a.edad, a.tipo, a.descripcion, a.imagen from Animal a join Favoritos f on a.id = f.idanimal where f.idUsuario =" + idDueño + ";";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public String getEmailDueño(int idAnimal) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Usuario.email " +
                "FROM Usuario " +
                "JOIN Animal ON Usuario.id = Animal.idDueño " +
                "WHERE Animal.Id = " + idAnimal + ";" ;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            return email;
        } else {
            cursor.close();
            return null;
        }
    }
    public int getIdUsuario(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id from Usuario where email = '" + email + "';",null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            return 0;
        }
    }


    //favoritos
    public boolean añadirFavorito(int idUsuario, int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("INSERT into Favoritos (idUsuario, idAnimal)VALUES(" + idUsuario + ", " + idAnimal + ");", null);

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }

    public boolean eliminarFavorito(int idUsuario, int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM Favoritos WHERE idAnimal = " + idAnimal + " AND idUsuario = " + idUsuario, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public int contarFavoritos(int idAnimal)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT idUsuario) AS total_usuarios FROM Favoritos WHERE idAnimal = " + idAnimal + ";", null);
        if (cursor.moveToFirst()) {
            int contador = cursor.getInt(0);
            cursor.close();
            return contador;
        } else {
            cursor.close();
            return 0;
        }
    }

    public boolean insertarAnimal(int idDueño, String nombre, String raza, String genero, String tamaño, int edad, String tipo, String descripcion, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("INSERT INTO Animal (idDueño, nombre, raza, genero, tamaño, edad, tipo, descripcion, imagen) VALUES (" + idDueño + ", '" + nombre + "', '" + raza + "', '" + genero + "', '" + tamaño + "', "+ edad + ", '" + tipo + "', '" + descripcion + "', '" + url + "');" , null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
    public boolean eliminarPublicación(int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM Animal WHERE id = " + idAnimal, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    public boolean isFavorito(int idUsuario, int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favoritos WHERE idUsuario = " + idUsuario + " AND idAnimal =  " + idAnimal + ";", null);

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }
    public boolean isPublicacionPropia(int idUsuario, int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Animal where iddueño = " + idUsuario + "  and id =  " + idAnimal, null);

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }
    public Cursor getDatosUsuario(String emailDueño)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select nombre, apellidos, telefono from Usuario where email = '" + emailDueño + "';", null);
        return cursor;
    }
    public Cursor getDatosAnimal(int idAnimal)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Animal where id = " + idAnimal + ";", null);
        return cursor;
    }

    public boolean updateUser(String emailDueño, String nombre, String apellidos, int telefono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("apellidos", apellidos);
        valores.put("telefono", telefono);

        int filasActualizadas = db.update("Usuario", valores, "email = ?", new String[]{emailDueño});

        db.close();

        return filasActualizadas > 0;
    }
    public boolean update(int idAnimal, String nombre, String raza,String genero, String tamaño, String tipo, int edad, String descripcion, String url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("raza", raza);
        valores.put("tipo", tipo);
        valores.put("edad", edad);
        valores.put("descripcion", descripcion);
        valores.put("genero", genero);
        valores.put("tamaño", tamaño);
        valores.put("imagen", url);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idAnimal)};
        int rowsAffected = db.update("Animal", valores, whereClause, whereArgs);
        db.close();
        return rowsAffected > 0;
    }


    public boolean borrarCuenta(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM Usuario where email = '" + email + "';", null);

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
            return false;
    }
    public boolean borrarCuentaFavs(int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete("Favoritos", "idUsuario = ?", new String[]{String.valueOf(idUsuario)});

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean borrarCuentaPublicaciones(int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete("Animal", "idDueño = ?", new String[]{String.valueOf(idUsuario)});

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean cambiarPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Usuario SET email = '" + email + "', password = '" + password + "' WHERE email = '" + email + "';";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
    public boolean borrarAnimal(int idAnimal) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete("Favoritos", "idAnimal = ?", new String[]{String.valueOf(idAnimal)});

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }


}
