package com.eniacs_team.rutamurcielago;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.preference.PreferenceManager;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    String[] StringPermisos = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.FINE_LOCATION", "android.permission.COARSE_LOCATION", "android.permission.CAMERA"};
    Boolean[] BoolPermisos = {false};
    SharedPreferences sharedPreferences;
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

        //Carga de archivo y mapa
        mapView = (MapView) findViewById(R.id.map);
        CopyFolder.copyAssets(getApplicationContext());
        Mapa mapa = new Mapa(mapView, MainActivity.this);
        mapa.setupMap(getApplicationContext());
        mapa.findFiles(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
/*
        if(askPermissions()) //Si es mayor a Lollipop preguntar por permisos
        {
            for (int i =0; i < StringPermisos.length; ++i)
            {
                requestPermission(StringPermisos[i],i+1); //Pregunto por todos los permisos
            }
        }
        */
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




    private boolean askPermissions(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String permission){
        if(askPermissions())
        {
            return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    /**
     * Verifica que anteriormente se haya preguntado por los permisos.
     *
     * @param  permission  el permiso por el que pregunta
     * @return Si se pregunto o no anteriormente
     */
    private boolean shouldWeAsk(String permission){
        return (sharedPreferences.getBoolean(permission, true));
    }

    /**
     * Marca un permiso como que anteriormente pregunto por el.
     *
     * @param  permission  el permiso por el que pregunta
     */
    private void markAsAsked(String permission){
        sharedPreferences.edit().putBoolean(permission, false).apply();
    }

    /**
     * Metodo encargado de pedir que se le conceda a la aplicacion ciertos permisos.
     */
    private void requestPermission(String permiso, int permissionRequestCode) {
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, new String[]{permiso}, permissionRequestCode);
    }


    /**
     * Verifica que tenga los permisos apropiados para acceder a la ubicación de usuario.
     *
     * @param  requestCode  codigo del permiso
     * @param  permissions  los permisos que se solicitan
     * @param  grantResults  indica si permiso es concedido o no
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            /*WRITE_EXTERNAL_STORAGE*/
            case 1:
                markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

            /*READ_EXTERNAL_STORAGE*/
            case 2:
                markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

            /*FINE_LOCATION*/
            case 3:
                markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

            /*COARSE_LOCATION*/
            case 4:
                markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

            /*CAMERA*/
            case 5:
                markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                if (grantResults.length > 0 && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
