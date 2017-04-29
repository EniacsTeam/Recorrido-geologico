package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by acer main on 24/04/2017.
 */

public class Ubicacion implements LocationListener {

  //   private Location mLastLocation;
  //  private Location mCurrentLocation;
    MainActivity mainActivity;
    Marker markerLocation;
    Location mCurrentLocation;
    Snackbar snackbar;
    int i=0;
    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);

    public Ubicacion (MapView map,MainActivity main,View v){
        this.mainActivity = main;
        this.markerLocation = new Marker(map);
        markerLocation.setPosition(routeCenter);
        Drawable marker=main.getResources().getDrawable(R.mipmap.marker);
        markerLocation.setIcon(marker);
        markerLocation.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        markerLocation.setTitle("My location");
        map.getOverlays().add(markerLocation);

        gpsActivo(v);


    }

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
    public void mostrarMsjGpsDesactivado(){
        snackbar.show();
    }
    @Override
    public void onLocationChanged(Location location) {
        markerLocation.setPosition(new GeoPoint(location));
        //Toast.makeText(mainActivity, "Latitud: "+mCurrentLocation.getLatitude()+" y Longitud: "+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
       // Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
        snackbar.show();
    }

    @Override
    public void onProviderEnabled(String provider) {
       // Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
        snackbar.dismiss();
    }

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
}

