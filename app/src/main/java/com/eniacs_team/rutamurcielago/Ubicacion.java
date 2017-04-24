package com.eniacs_team.rutamurcielago;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by acer main on 24/04/2017.
 */

public class Ubicacion implements LocationListener {

    private Location mLastLocation;
    private Location mCurrentLocation;
    MainActivity mainActivity;

    public Ubicacion (MainActivity main){
        mainActivity = main;

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Toast.makeText(mainActivity, "Latitud: "+mCurrentLocation.getLatitude()+" y Longitud: "+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                //Toast.makeText(mainActivity, "LocationProvider.AVAILABLE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Toast.makeText(mainActivity, "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Toast.makeText(mainActivity, "LocationProvider.TEMPORARILY_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

