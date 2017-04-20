package com.eniacs_team.rutamurcielago;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Carga de archivo y mapa
        mapView = (MapView) findViewById(R.id.map);
        CopyFolder.copyAssets(getApplicationContext());
        Mapa mapa = new Mapa(mapView);
        mapa.setupMap(getApplicationContext());
        mapa.findFiles(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
}
