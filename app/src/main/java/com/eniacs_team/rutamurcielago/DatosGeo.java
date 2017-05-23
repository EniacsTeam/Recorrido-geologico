package com.eniacs_team.rutamurcielago;

import android.location.Location;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;


public class DatosGeo {

    static int cantidadElementos=26;

    public static double[] latitudes(){
        double[] latitude = {10.95124, 10.94081, 10.94075, 10.96712, 10.91338, 10.92449, 10.92599, 10.92792, 10.91753,
                10.93645, 10.9502, 10.95016, 10.94015, 10.93171, 10.911, 10.89343, 10.89263, 10.89428, 10.884, 10.88,
                10.85402, 10.848, 10.8537, 10.85516, 10.85607, 10.85919};
        return latitude;
    }

    public static double[] longitudes(){
        double[] longitud = {-85.70945, -85.77404, -85.80209, -85.80062, -85.80195, -85.81871, -85.81824,
                -85.81947, -85.78693, -85.81037, -85.875, -85.88396, -85.87438, -85.87749, -85.911, -85.94944,
                -85.93188, -85.92604, -85.8998, -85.8791, -85.85996, -85.8591, -85.90864, -85.90895, -85.9103, - 85.93745};
        return longitud;
    }

    public static int[] radios(){
        int[] radios = {200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,100,200,200,200,200,200,200};
        return radios;
    }

    public static ArrayList<Location> getLocations(){

		ArrayList<Location>locations = new ArrayList<>();
        double[] latitude = DatosGeo.latitudes();
        double[] longitud =DatosGeo.longitudes();
        for (int i = 0; i < cantidadElementos; i++) {

            Location l=new Location(String.valueOf(i));
            l.setLatitude(latitude[i]);
            l.setLongitude(longitud[i]);
            locations.add(i,l);
        }
        return locations;
    }

    /**
     * Metodo que define el bounding box. Se pueden definir distintos para cada zoom, lat,lon, lat,lon
     *
     * @return
     */
    public static BoundingBox getBoundingBox(int i) {
        if(i==1){//para todos los niveles excepto 16
            return new BoundingBox(11.062255, -85.587361, 10.765595, -86.091224);
        }else{//nivel 16
            return new BoundingBox(10.929974, -85.816022, 10.921589, -85.821822);
        }
    }

    public  static boolean isIntoBoundingBox(Location l){
        Location bs=new Location("superior");//superior
        Location bi=new Location("inferior");//inferior
        if((bs.getLatitude()<l.getLatitude()&&l.getLatitude()<bi.getLatitude())&&(bi.getLongitude()<l.getLongitude()&&l.getLongitude()<bs.getLongitude())){
            return true;
        }else{
            return false;
        }
    }
}