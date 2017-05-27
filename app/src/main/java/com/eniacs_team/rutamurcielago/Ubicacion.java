package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

import static android.os.Build.VERSION_CODES.M;
import static com.eniacs_team.rutamurcielago.R.mipmap.marker;

/**
 * Esta clase se encarga de obtener la ubicación del usuario y controlar lo que sucede en el onLocationChanged.
 *
 * @author  EniacsTeam
 */
public class Ubicacion implements LocationListener {

    MainActivity mainActivity;
    MyLocationNewOverlay mLocationOverlay;
    Snackbar snackbar;
    MapView map;
    List<Marker> marcadores;
    ArrayList<Location> locations;
    int[] distancias;

    int marcadorActual=-1;
    Location currentLocation;
    FloatingActionButton btCenterMap;
    boolean isFollowing= false;
    Context mContext;
    View v;
    CustomDialogClass dialog;
    FloatingActionButton btFollowMe;
    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);
    /**
     * Constructor de clase, Se inicializan variables globales.
     * @param map
     * @param main: Activity main
     * @param v : View contiene:( layout, drawing, focus change, scrolling, etc..)
     */

    public Ubicacion (final MapView map, final MainActivity main, View v, List<Marker> markers, View center, Activity activity){
        mContext= activity;
        this.locations = DatosGeo.getLocations();
        this.distancias=DatosGeo.radios();
        this.v= v;
        this.marcadores=markers;
        this.map = map;
        this.mainActivity = main;
        this.dialog=new CustomDialogClass(mainActivity,1);
        this.btFollowMe = (FloatingActionButton) this.mainActivity.findViewById(R.id.ic_follow_me);
        gpsActivo(v);
        this.btCenterMap = (FloatingActionButton) center;
        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View center) {
                if (currentLocation != null) {
                    map.getController().setZoom(13);
                    map.getController().animateTo(new GeoPoint(currentLocation.getLatitude()+0.0001,currentLocation.getLongitude()));
                }
            }
        });

        btFollowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFollowing) {
                    if(currentLocation!=null){
                        map.getController().animateTo(new GeoPoint(currentLocation.getLatitude() + 0.0001, currentLocation.getLongitude()));
                    }
                    isFollowing= true;

                    btFollowMe.setBackgroundTintList(
                            ColorStateList.valueOf(mainActivity.getResources().getColor(R.color.rojo)));
                } else {
                    isFollowing= false;
                    btFollowMe.setBackgroundTintList(
                            ColorStateList.valueOf(mainActivity.getResources().getColor(R.color.blanco)));
                }
            }
        });
    }

    /**
     *  Este método lo que hace es crear el Snackbar que se muestra si el gps esta desactivado
     * @param v : contiene la información ( layout, drawing, focus change, scrolling, etc..)
     *          para poder imprimir en pantalla el snackbar
     */
    public void gpsActivo(View v){
        snackbar = Snackbar
                .make(v, "¡Se necesita activar el GPS!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Activar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(this.mainActivity.getResources().getColor(R.color.celeste));
        snackbar.setActionTextColor(this.mainActivity.getResources().getColor(R.color.naranja_dark));

    }

    /**
     * Hace visible el snackbar de gps desactivado
     */
    public void mostrarMsjGpsDesactivado(){
        snackbar.show();

    }
    public void desMsjGpsDesactivado(){
        snackbar.dismiss();
    }
    /**
     * Permite actualizar la vista del mapa, cambio de colores y funcionalidad del "ver más" en el marcador
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        //si está activado el boton de seguimiento
/*        if(isFollowing){
            map.getController().animateTo(new GeoPoint(location.getLatitude()+0.0001,location.getLongitude()));
            Toast.makeText(mainActivity, "FOllowing", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mainActivity, "NOO following", Toast.LENGTH_SHORT).show();
        }*/
        // markerLocation.setPosition(new GeoPoint(location));
        currentLocation= new Location(location);

        int marcador = distanciaEntrePuntos(location);
        map.getController().animateTo(new GeoPoint(map.getMapCenter().getLatitude()+0.0001,map.getMapCenter().getLongitude()));

        if(isFollowing) {
            map.getController().animateTo(new GeoPoint(location.getLatitude() + 0.0001, location.getLongitude()));
        }

        Marker marker;
        if (marcador == -1){
            if (marcadorActual!= -1){
                marker= marcadores.get(marcadorActual);
                Mapa.MyInfoWindow ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                ma.setTipo(false); marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                marcadores.set(marcadorActual, marker);
            }
        }else{
            if (marcadorActual!= marcador) {
                BaseDatos base = new BaseDatos(mContext);
                if(marcador==0){
                    Snackbar s = Snackbar
                            .make(v, "Esta cerca de " + base.selectDescripcion(marcador + 1) + ".", Snackbar.LENGTH_LONG);
                    s.show();
                }else {
                    Snackbar s = Snackbar
                            .make(v, "Esta cerca de " + base.selectDescripcion(marcador + 1) + ", seleccione el punto azul para más información.", Snackbar.LENGTH_LONG);
                    s.show();
                }
                if (marcadorActual!= -1) {
                    marker = marcadores.get(marcadorActual);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                    Mapa.MyInfoWindow ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                    ma.setTipo(false);

                    marcadores.set(marcadorActual, marker);
                    marker = marcadores.get(marcador);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));

                    ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                    ma.setTipo(true);
                    marcadores.set(marcador, marker);
                    marcadorActual= marcador;

                }else {
                    marker = marcadores.get(marcador);
                    Mapa.MyInfoWindow ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                    ma.setTipo(true);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));
                    marcadores.set(marcador, marker);

                }
            }
        }
        marcadorActual = marcador;
        //si la ubicación actual no está dentro del recorrido muestre el mensaje.
        if(!DatosGeo.isIntoBoundingBox(location)){
            //Toast.makeText(mainActivity, "inside"+location.getLatitude()+" "+location.getLatitude(), Toast.LENGTH_SHORT).show();
            if(!dialog.isShowing()){//si ya está abierto, no haga nada.
                dialog.show();
            }
        }
    }

    /**
     * Se ejecuta cuando se desactiva el GPS
     * @param provider: proveedor de GPS
     */
    @Override
    public void onProviderDisabled(String provider) {
       // Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
        mostrarMsjGpsDesactivado();

    }

    /**
     * Se ejecuta cuando se Activa el GPS
     * @param provider : proveedor de GPS
     */
    @Override
    public void onProviderEnabled(String provider) {
       // Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
        desMsjGpsDesactivado();
    }

    /**
     * Se ejecuta cuando el status del GPS cambia, puede estar disponible, fuera de servicio o temporalmente no disponible
     * @param provider: proveedor de GPS
     * @param status : AVAILABLE, OUT_OF_SERVICE or TEMPORARILY_UNAVAILABLEa
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                //Toast.makeText(mainActivity, "LocationProvider.AVAILABLE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.OUT_OF_SERVICE:
                //Toast.makeText(mainActivity, "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
               // Toast.makeText(mainActivity, "LocationProvider.TEMPORARILY_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Ahorita devuelve el punto más cercano dentro de los rangos
     * @param current
     * @return
     */
    public int distanciaEntrePuntos(Location current){
        float menorDist= Float.MAX_VALUE;
        int ret= -1;
        for(int i=0;i<locations.size();i++){
            if(current.distanceTo(locations.get(i))<distancias[i]){
                if (current.distanceTo(locations.get(i))<menorDist) {
                    menorDist = current.distanceTo(locations.get(i));
                    ret = i;
                }
            }
        }
        return ret;
    }
}

