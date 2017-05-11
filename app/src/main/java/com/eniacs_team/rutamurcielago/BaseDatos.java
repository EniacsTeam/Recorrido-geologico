package com.eniacs_team.rutamurcielago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base de datos --- Clase que gestiona las consultas a la base de datos
 * @author    EniacsTeam
 */
public class BaseDatos extends SQLiteOpenHelper {
    private Context context;

    public BaseDatos(Context context) {
        super(context, "IslaMurcielagoDB", null, 1);
        this.context=context;
        cargarBase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Carga una nueva base de datos en el caso de que no exista
     */
    public void cargarBase(){
        boolean existeBase = existenciaBase();

        if(!existeBase){
            Log.i("Base de datos", "Creando base");
            this.getReadableDatabase();
            this.close();
            copiarBase();
        }
    }
    /**
     * Verifica si la base de datos existe
     * @return checkDB como booleano
     */
    private boolean existenciaBase(){
        String DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        String DB_NAME = "IslaMurcielagoDB";
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            Log.i("Base de datos", "No existe la base");
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Carga la base de datos inicial
     * Solo se requiere ejecutar la primera vez que se abre el programa
     */
    public void copiarBase()
    {
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        InputStream myInput = null;
        try
        {
            String package_name = context.getPackageName();
            String DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
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
            Log.i("Base de datos", "Se terminó de copiar la base de datos");
        }
        catch(IOException e)
        {
            Log.i("Base de datos", "Error en la copia de la base de datos");
            e.printStackTrace();
        }
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
     * Verifica si el mapa ya ha sido cargado
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
     * Verifica si el mapa ya ha sido cargado
     * @param punto El lugar de consulta
     * @param tipoMedia El tipo de archivo que se requiere
     * @return existe como entero 1->Existe, 0->No existe
     */
    public int existenciaPunto(int punto, String tipoMedia) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Lugares";
        String[] columns = {tipoMedia};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getString(0).isEmpty()){
                    estado = 0;
                } else {
                    estado = 1;
                }
            }
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        Log.i("Base de datos", Integer.toString(estado));
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
            Log.i("Base de datos", "Se actualizo un valor en la base de datos");
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "Error al insertar en la base");
        }
    }
}