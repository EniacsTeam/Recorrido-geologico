package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {
    MapView mapView;

    //Se ocupan en el onResume
    Ubicacion ubicacionListener;
    LocationManager mlocManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent intent = new Intent(this, CameraOSMaps.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(intent);
                requestPermission("android.permission.CAMERA",1);
            }
        });

        /*Pido por el permiso de acceso al almacenamiento
        Permisos permisos = new Permisos(MainActivity.this);
        permisos.requestPermission("android.permission.WRITE_EXTERNAL_STORAGE",1);*/

        BaseDatos base = new BaseDatos(getApplicationContext());

        //Carga de la base de datos, quitar el comentario si se modifico el archivo en assets
        base.copyDataBase();


        //Se busca el mapa
        mapView = (MapView) findViewById(R.id.map);
        //se copia el archivo de assets a /osmdroid si no ha sido copiado
        if (base.selectEstadoMapa() == 0){
            CopyFolder.copyAssets(this);
            base.actualizarEstadoMapa();
        }
        //se le pasa el mapa y actividad a la clase encargada de controlarlo
        Mapa mapa = new Mapa(mapView,this);
        //se inicializa el mapa. Zoom, bounding box etc
        mapa.setupMap(getApplicationContext());
        //se inserta el mapa offline dentro del mapview
        mapa.findFiles();
        //se agregan los marcadores del mapa
        mapa.agregarMarcadores();
        //se inicializa la escucha del GPS


        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ubicacionListener = new Ubicacion(mapView,this,findViewById(R.id.fab));
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }*/
        if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ubicacionListener);
        }

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

    /**
     * Se ejecuta cuando se vuelve a activar la activity
     */
    @Override protected void onResume() {
        super.onResume();
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            ubicacionListener.mostrarMsjGpsDesactivado();
        }
        // Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    public void requestPermission(String permiso, int permissionRequestCode) {
        //Preguntar por permiso
        if(askPermissions())
        {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, permissionRequestCode);
        }
    }

    private boolean askPermissions(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

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
        final Intent intent = new Intent(this, CameraOSMaps.class);
        switch (requestCode) {
            /*WRITE_EXTERNAL_STORAGE*/
            case 1:
                //markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                Boolean camera = this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                if (camera) {
                    //se crea bien
                    startActivity(intent);
                    // close splash activity
                } else {
                    //Toast.makeText(this, "Need your storage", Toast.LENGTH_SHORT).show();
                    MainActivity.CustomDialogClass dialogo = new CustomDialogClass(this);
                    dialogo.show();
                }
                break;

        }
    }

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Button btnAceptar;
        public TextView txt;

        public CustomDialogClass(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogo_ver_mas);
            txt = (TextView) findViewById(R.id.texto_dialogo);
            txt.setText("Se necesita acceso a la cámara para el uso de Realidad Aumentada");
            btnAceptar = (Button) findViewById(R.id.aceptar);
            btnAceptar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.aceptar:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}