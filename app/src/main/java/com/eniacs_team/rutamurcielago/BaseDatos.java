package com.eniacs_team.rutamurcielago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base de datos --- Clase que gestiona las consultas a la base de datos
 * @author    ENIACS
 */
public class BaseDatos extends SQLiteOpenHelper {
    private Context context;

    public BaseDatos(Context context) {
        super(context, "IslaMurcielagoDB", null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    /**
     * Devuelve la primera imagen de un punto dado
     * @param id El identificador del lugar de consulta
     * @return imagen como Drawable
     */
    public Drawable selectImagen(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Fotos";
        String[] columns = {"Descripcion", "Ruta"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";

        Drawable imagen = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                String descripcion = cursor.getString(0);
                String ruta = cursor.getString(1);

                try
                {
                    InputStream ims = context.getAssets().open(ruta);
                    imagen = Drawable.createFromStream(ims, null);
                    ims.close();
                }
                catch(IOException ex)
                {
                    Log.i("Base de datos", "Archivo no encontrado.");
                }
            }
        }

        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return imagen;
    }
    /**
     * Devuelve la descripcion de un punto dado
     * @param id El identificador del lugar de consulta
     * @return descripcion como String
     */
    public String selectDescripcion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Descripcion"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        String descripcion = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                descripcion = cursor.getString(0);
            }
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return descripcion;
    }

    /**
     * Verifica si el mapa ya ha sido cargadp
     * @return estado como entero
     */
    public int selectEstadoMapa() {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "MapaCargado";
        String[] columns = {"Estado"};
        String selection = "Condicion =?";
        String[] selectionArgs = {"1"};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                estado = Integer.parseInt(cursor.getString(0));
            }
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return estado;
    }

    /**
     * Actualiza el estado del mapa cuando lo carga
     */
    public void actualizarEstadoMapa() {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "MapaCargado";
        String selection = "Condicion =?";
        String[] selectionArgs = {"1"};
        ContentValues estado = new ContentValues();
        estado.put("Estado","1");
        try {
            db.update(table, estado, selection, selectionArgs);
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "Error al insertar en la base");
        }
        Log.i("Base de datos", "Se actualizo un valor en la base de datos");
    }

    /**
     * Carga la base de datos inicial
     * Solo se requiere ejecutar la primera vez que se abre el programa
     */
    public void copyDataBase()
    {
        Log.i("Database", "Se inicia la copia de la base de datos");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        InputStream myInput = null;
        try
        {
            String package_name = context.getPackageName();
            String DB_PATH = "/data/data/"+package_name+"/databases/";
            String DB_NAME = "IslaMurcielagoDB";
            myInput = context.getAssets().open(DB_NAME);
            myOutput =new FileOutputStream(DB_PATH+ DB_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database", "Se terminó de copiar la base de datos");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}