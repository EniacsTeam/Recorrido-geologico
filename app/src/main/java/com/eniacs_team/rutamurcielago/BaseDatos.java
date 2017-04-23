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

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private Context context;
    private static final String DATABASE_NAME = "IslaMurcielagoDB";
    // Contacts table name
    private static final String TABLE_SHOPS = "Lugares";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SH_ADDR = "shop_address";
    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_SH_ADDR + " TEXT" + ")";
        //db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
// Creating tables again
        onCreate(db);
    }

    public String select(int id) {
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
        if (cursor != null)
            cursor.moveToFirst();
        String ID = cursor.getString(0);
        String video = cursor.getString(1);
        Log.i("DatabaseMur", ID);
        Log.i("DatabaseMur", video);
// return shop
        return ID;
    }

    public void copyDataBase()
    {
        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try
        {
            String package_name = context.getPackageName();
            String DB_PATH = "/data/data/"+package_name+"/databases/";
            String DB_NAME = "IslaMurcielagoDB";
            myInput = context.getAssets().open(DB_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput =new FileOutputStream(DB_PATH+ DB_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");


        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
/*
     public void copyDataBase() throws IOException{
            Context context = getApplicationContext();
            String package_name=context.getPackageName();
            String DB_PATH = "/data/data/"+package_name+"/databases/";
            String DB_NAME = "IslaMurcielagoDB";
            try {
                InputStream myInput = context.getAssets().open(DB_NAME);

                File dbFile=new File(DB_PATH);
                dbFile.mkdirs();

                String outputFileName = DB_PATH + DB_NAME;
                OutputStream myOutput = new FileOutputStream(outputFileName);

                byte[] buffer = new byte[1024];
                int length;

                while((length = myInput.read(buffer))>0){
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
     }

 */