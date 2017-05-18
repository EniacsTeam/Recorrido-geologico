package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

import static android.os.Build.VERSION_CODES.M;
import static com.eniacs_team.rutamurcielago.R.mipmap.marker;

/**
 * Esta clase se encarga de obtener la ubicación del usuario.
 *
 * @author  EniacsTeam
 */
public class Ubicacion implements LocationListener {

  //   private Location mLastLocation;
  //  private Location mCurrentLocation;
    MainActivity mainActivity;
   // Marker markerLocation;
    Snackbar snackbar;
    MapView map;
    List<Marker> marcadores;
    ArrayList<Location> locations;
    int[] distancias;

    int marcadorActual=-1;
    Location currentLocation;
    ImageButton btCenterMap;
    Context mContext;
    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);

    /**
     * Constructor de clase, Se inicializan variables globales.
     * @param map
     * @param main: Activity main
     * @param v : View contiene:( layout, drawing, focus change, scrolling, etc..)
     */

    public Ubicacion (final MapView map, MainActivity main, View v, List<Marker> markers, View center, Activity activity){
        mContext= activity;
        this.locations = DatosGeo.getLocations();
        this.distancias=DatosGeo.radios();

        this.marcadores=markers;
        this.map = map;
        this.mainActivity = main;
       // this.markerLocation = new Marker(map);
        //markerLocation.setPosition(routeCenter);

       // Drawable marker=main.getResources().getDrawable(R.drawable.ic_here);
       // markerLocation.setIcon(marker);
       // markerLocation.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
       // markerLocation.setTitle("My location");
       // this.map.getOverlays().add(markerLocation);
        gpsActivo(v);
        this.btCenterMap = (ImageButton) center;

        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity, "Sin GPS", Toast.LENGTH_LONG).show();
                if (currentLocation != null) {
                    GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    map.getController().animateTo(myPosition);
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
                .make(v, "Se necesita activar el GPS!", Snackbar.LENGTH_INDEFINITE)
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

    @Override
    public void onLocationChanged(Location location) {
       // markerLocation.setPosition(new GeoPoint(location));
        MapController mapController=(MapController) map.getController();
        int marcador = distanciaEntrePuntos(location);
        mapController.animateTo(new GeoPoint(map.getMapCenter().getLatitude()+0.0001,map.getMapCenter().getLongitude()));
        Marker marker;
        Mapa.CustomDialogClass dialogo = new Mapa.CustomDialogClass(mainActivity);
        if (marcador == -1){
            if (marcadorActual!= -1){
                marker= marcadores.get(marcadorActual);
                marker.setInfoWindow(new Mapa.MyInfoWindow(R.layout.bonuspack_bubble, map, marcadorActual,false,mContext,dialogo));
                marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                marcadores.set(marcadorActual, marker);
            }
        }else{
            if (marcadorActual!= marcador) {
                if (marcadorActual!= -1) {
                    marker = marcadores.get(marcadorActual);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                    marker.setInfoWindow(new Mapa.MyInfoWindow(R.layout.bonuspack_bubble, map, marcadorActual,false,mContext,dialogo));
                    marcadores.set(marcadorActual, marker);
                    marker = marcadores.get(marcador);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));

                    marker.setInfoWindow(new Mapa.MyInfoWindow(R.layout.bonuspack_bubble, map, marcador,true, mContext, dialogo));
                    marcadores.set(marcador, marker);
                    marcadorActual= marcador;
                }else {
                    marker = marcadores.get(marcador);
                    marker.setInfoWindow(new Mapa.MyInfoWindow(R.layout.bonuspack_bubble, map, marcador,true, mContext ,dialogo));
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));
                    marcadores.set(marcador, marker);

                }
            }
        }
        marcadorActual = marcador;

        //si title de marcador_actual = marcador(int) no hacer nada: caso contario desabilitarlo y habilitar marcador con title = marcador(int)
        //ademas si es marcador 4-7 los pegados se debe hacer zoom a ese espacio aumentar a zoom 16 y
        //Toast.makeText(mainActivity, "Latitud: "+mCurrentLocation.getLatitude()+" y Longitud: "+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Se ejecuta cuando se desactiva el GPS
     * @param provider: proveedor de GPS
     */
    @Override
    public void onProviderDisabled(String provider) {
       // Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
        snackbar.show();
    }

    /**
     * Se ejecuta cuando se Activa el GPS
     * @param provider : proveedor de GPS
     */
    @Override
    public void onProviderEnabled(String provider) {
       // Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
        snackbar.dismiss();
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

