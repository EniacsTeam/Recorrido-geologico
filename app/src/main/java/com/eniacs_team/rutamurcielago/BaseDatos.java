package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public int select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Descripcion", "Video"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {"45"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (cursor != null) {
            cursor.moveToFirst();
            String ID = cursor.getString(0);
            String video = cursor.getString(1);
            Log.i("DatabaseMur", ID);
            Log.i("DatabaseMur", video);
        }
        return 1;
    }

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