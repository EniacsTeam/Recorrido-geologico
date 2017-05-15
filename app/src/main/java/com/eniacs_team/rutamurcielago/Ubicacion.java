package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

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
    Marker markerLocation;
    Location mCurrentLocation;
    Snackbar snackbar;
    MapView map;
    ArrayList<Location>locations;
    int[] distancias;
    Mapa mapa;
    int puntocercano =-1;

    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);

    /**
     * Constructor de clase, Se inicializan variables globales.
     * @param map
     * @param main: Activity main
     * @param v : View contiene:( layout, drawing, focus change, scrolling, etc..)
     */
    public Ubicacion (final MapView map,final MainActivity main,final View v, final Mapa mapa){
        this.locations = DatosGeo.getLocations();
        this.distancias=DatosGeo.radios();
        this.mapa= mapa;

        this.map = map;
        this.mainActivity = main;
        this.markerLocation = new Marker(map);
        markerLocation.setPosition(routeCenter);
        Drawable marker=main.getResources().getDrawable(R.drawable.ic_here);
        markerLocation.setIcon(marker);
        markerLocation.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        markerLocation.setTitle("My location");
        this.map.getOverlays().add(markerLocation);

        gpsActivo(v);


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
        markerLocation.setPosition(new GeoPoint(location));
        int marcador = distanciaEntrePuntos(location);
        Marker marker;
        if (marcador == -1){
            if (puntocercano!= -1){
                marker= mapa.marcadores.get(puntocercano);
                marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                mapa.marcadores.set(puntocercano, marker);
            }
        }else{
            if (puntocercano != marcador) {
                if (puntocercano!= -1) {
                    marker = mapa.marcadores.get(puntocercano);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_naranja));
                    mapa.marcadores.set(puntocercano, marker);

                    marker = mapa.marcadores.get(marcador);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));
                    mapa.marcadores.set(marcador, marker);
                    puntocercano = marcador;
                }else {
                    marker = mapa.marcadores.get(marcador);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_azul));
                    mapa.marcadores.set(marcador, marker);
                }
            }
        }
        puntocercano = marcador;

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

    public int distanciaEntrePuntos(Location current){
        for(int i=0;i<locations.size();i++){
            if(current.distanceTo(locations.get(i))<distancias[i]){
                return i;
            }
        }
        return -1;
    }
}

