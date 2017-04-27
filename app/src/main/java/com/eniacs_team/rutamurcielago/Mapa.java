package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

/**
 * Created by Johan Duran Cerdas on 19/4/2017.
 */

public class Mapa {
    MapView mapView;
    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);
    List<GeoPoint> locations;
    List<Marker> marcadores;
    Activity activity;

    private OSMWorldPlugin mOSMapPlugin;
    private World mWorld;

    public Mapa(MapView map,Activity activity){

    public Mapa(MapView map){
        this.mapView = map;
        this.locations=new ArrayList<>();
        this.marcadores=new ArrayList<>();
        this.activity=activity;

        double []latitude ={10.95124,10.94081,10.94075,10.96712,10.91338,10.92449,10.92599,10.92792,10.91753,
                10.93645,10.9502,10.95016,10.94015,10.93171,10.911,10.89343,10.89263,10.89428,10.884,10.88,
                10.85402,10.848,10.8537,10.85516,10.85607,10.85919};
        double []longitud ={-85.70945,-85.77404,-85.80209,-85.80062,-85.80195,-85.81871,-85.81824,
                -85.81947,-85.78693,-85.81037,-85.875,-85.88396,-85.87438,-85.87749,-85.911,-85.94944,
                -85.93188,-85.92604,-85.8998,-85.8791,-85.85996,-85.8591,-85.90864,-85.90895,-85.9103 -85.93745};

        for(int i=0;i<longitud.length;i++){
            locations.add(i, new GeoPoint(latitude[i],longitud[i]));
            marcadores.add(i,new Marker(map));
        }

    }

    public void setupMap(){
        /*En caso de error muestra este layout*/
        mapView.getTileProvider().setTileLoadFailureImage(activity.getResources().getDrawable(R.drawable.notfound));

        /*Elementos correspondietes a funcionalidades*/
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        //mapView.setBuiltInZoomControls(true);

        /*Ajustes en el zoom y el enfoque inicial*/
        final MapController mapViewController = (MapController) mapView.getController();
        mapViewController.setZoom(13);
        mapViewController.animateTo(routeCenter);
        mapView.setMinZoomLevel(12);
        mapView.setMaxZoomLevel(15);

        //Desactivar botones de zoom nativos.
        mapView.setBuiltInZoomControls(false);

        /*Limitar el area de movimiento del mapa*/
        mapView.setScrollableAreaLimitDouble(getBoundingBox());

        /*En caso de querer dar algun enfoque a un bounding utilizar:*/
        //mapView.zoomToBoundingBox(getBoundingBox(),false);

        // We create the world and fill the world
        mWorld = CustomWorldHelper.generateObjects(context);

        // As we want to use GoogleMaps, we are going to create the plugin and
        // attach it to the World
        mOSMapPlugin = new OSMWorldPlugin(context);
        // Then we need to set the map in to the GoogleMapPlugin
        mOSMapPlugin.setOSMap(mapView);
        // Now that we have the plugin created let's add it to our world.
        // NOTE: It is better to load the plugins before start adding object in to the world.
        mWorld.addPlugin(mOSMapPlugin);

        // Lets add the user position
        GeoObject user = new GeoObject(1000l);
        user.setGeoPosition(mWorld.getLatitude(), mWorld.getLongitude());
        user.setImageResource(R.drawable.chibi);
        user.setName("User position");
        mWorld.addBeyondarObject(user);


        /*Marcador de ejemplo. Crear metodo que haga esto.*/
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(new GeoPoint(11.028670, -85.704637));
        Drawable marker=context.getResources().getDrawable(R.mipmap.marker);
        startMarker.setIcon(marker);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        /*InfoWindow infoWindow = new MyInfoWindow(R.layout.bonuspack_bubble, mapView);
        startMarker.setInfoWindow(infoWindow);*/
        startMarker.setTitle("Title of the marker");
        mapView.getOverlays().add(startMarker);

        /*Evento asociado al zoom en el mapa*/
        /*Se puede utilizar para limitar que ver o no dependiendo del zoom*/
        /*mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                *//*Aca van las acciones al mover el mapa con dedo*//*
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return true;
            }
        });*/
    }



    /*Define el bounding box se pueden definir distintos para cada zoom, lat,lon, lat,lon*/
    private BoundingBox getBoundingBox() {
            return new BoundingBox(10.97422, -85.67276, 10.79958, -85.979);
    }






public void findFiles(){
    /*Se busca el archivo dentro del path*/
    File tiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/tiles.zip");
    if (tiles.exists()) {
                    try {
                        //Se crea y utiliza en archivo
                        File[] file=new File[]{tiles};
                        OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity),file);

                        //Se asigna el proveedor de tiles
                        mapView.setTileProvider(tileProvider);

                        /*Se obtienen los archivos dentro de la carpeta*/
                        String source = "";
                        IArchiveFile[] archives = tileProvider.getArchives();
                        /*En caso de no tener se utiliza proveedor por internet*/
                        if (archives.length > 0) {
                            Set<String> tileSources = archives[0].getTileSources();
                            if (!tileSources.isEmpty()) {
                                source = tileSources.iterator().next();
                                mapView.setTileSource(FileBasedTileSource.getSource(source));
                            } else {/*En caso de no existir archivo utiliza uno proporcionado por internet*/
                                mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                            }

                        } else {
                            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                        }

                        mapView.invalidate();
                        return;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    /*En los proximos dos casos podemos volver a llamar cargar assets porque es posible que la persona lo borro.*/
        Toast.makeText(activity, tiles.getAbsolutePath() + " No existe mapa, en el dispositivo, cargando desde internet", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(activity, tiles.getAbsolutePath() + "El directorio no existe", Toast.LENGTH_SHORT).show();
    }
}

public void agregarMarcadores(){

    /*Se crea un marcador para cada punto en el mapa*/
    for (int i =0;i<locations.size();i++){
    Marker marcador = marcadores.get(i);
        marcador.setPosition(locations.get(i));
        Drawable marker=activity.getResources().getDrawable(R.mipmap.marker);
        marcador.setIcon(marker);
        marcador.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        marcador.setTitle("Title of the marker");
        mapView.getOverlays().add(marcador);
    }
}
}

