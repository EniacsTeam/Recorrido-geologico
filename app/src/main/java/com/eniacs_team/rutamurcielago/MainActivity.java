package com.eniacs_team.rutamurcielago;

import android.net.Uri;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import java.util.Iterator;
import java.util.Map;

/**
 * Esta clase se encarga de inicializar el mapa y su base de datos asociada, ademas crea un enlace a la actividad encargada
 * de realidad aumentada.
 *
 * @author  EniacsTeam
 */
public class MainActivity extends AppCompatActivity {
    MapView mapView;
    //Se ocupan en el onResume
    Ubicacion ubicacionListener;
    LocationManager mlocManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                //startActivity(intent);
                requestPermission("android.permission.CAMERA",1);
            }
        });

        /*Pido por el permiso de acceso al almacenamiento
        Permisos permisos = new Permisos(MainActivity.this);
        permisos.requestPermission("android.permission.WRITE_EXTERNAL_STORAGE",1);*/

        BaseDatos base = BaseDatos.getInstanciaInicial(this);

        //Se busca el mapa
        mapView = (MapView) findViewById(R.id.map);
        //se copia el archivo de assets a /osmdroid si no ha sido copiado
        if (base.selectEstadoDatos(1) == 0){
            CopyFolder.copyAssets(this);
            base.actualizarEstado(1);
        }
        //se le pasa el mapa y actividad a la clase encargada de controlarlo
        Mapa mapa = new Mapa(mapView,this);
        //se inicializa el mapa. Zoom, bounding box etc
        mapa.setupMap();
        //se inserta el mapa offline dentro del mapview
        mapa.findMapFiles();

        //Se obtienen el marcador que controla la ubucación actual
        Marker currentMarker=mapa.agregarCurrentLocation();

        //se agregan los marcadores del mapa
        List<Marker> marcadores = mapa.agregarMarcadores();
        //se inicializa la escucha del GPS

        //se inicializa la escucha del GPS
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ubicacionListener = new Ubicacion(mapView,this,findViewById(R.id.fab),marcadores,currentMarker,findViewById(R.id.ic_center_map),this);
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }*/
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ubicacionListener);
            }
        }
        else
        {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ubicacionListener);
        }


    }

    public void goToSo (View view) {
        goToUrl( "http://stackoverflow.com/");
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse("http://stackoverflow.com/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocManager.removeUpdates(ubicacionListener);
        mapView.onDetach();
    }

    /**
     * Maneja el la seleccion de una opcion dentro del menu de opciones.
     *
     * @param item el item seleccionado dentro del menu
     * @return falso para permitir continuar procesamiento de menu, verdadero para terminarlo aqui.
     */
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

    /**
     * Metodo encargado de mostrar los dialogos de solicitud de permisos si es necesario.
     *
     * @param permiso hilera de permisos por pedir
     * @param permissionRequestCode resultado de obtencion de permisos
     */
    public void requestPermission(String permiso, int permissionRequestCode) {
        //Preguntar por permiso
        if(askPermissions())
        {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, permissionRequestCode);
        }
    }

    /**
     * Metodo encargado de cerciorarse si es o no necesaria la solicitud dinamica de permisos.
     *
     * @return verdadero si android del dispositivo es mayor a Lollipop, en caso contrario falso
     */
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

    /**
     * Clase para controlar el dialogo que indica que el usuario no acepto el permiso de camara necesario para el funcionamiento
     * de la realidad aumentada.
     *
     * @author  EniacsTeam
     */
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