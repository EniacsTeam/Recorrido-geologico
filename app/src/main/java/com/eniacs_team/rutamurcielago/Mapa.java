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
import java.util.Set;

/**
 * Created by Johan Duran Cerdas on 19/4/2017.
 */

public class Mapa implements LocationListener  {
    MapView mapView;
    private Location mLastLocation;
    private Location mCurrentLocation;
    MainActivity mainActivity;

    public static final GeoPoint pacific = new GeoPoint(11.028670, -85.704637);

    public Mapa(MapView map){
        this.mapView = map;
    }

    public void setupMap(Context context){
        /*En caso de error muestra este layout*/
        mapView.getTileProvider().setTileLoadFailureImage(context.getResources().getDrawable(R.drawable.notfound));

        /*Elementos correspondietes a funcionalidades*/
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        mapView.setBuiltInZoomControls(true);

        /*Ajustes en el zoom y el enfoque inicial*/
        final MapController mapViewController = (MapController) mapView.getController();
        mapViewController.setZoom(14);
        mapViewController.animateTo(pacific);
        mapView.setMaxZoomLevel(16);
        mapView.setMinZoomLevel(14);

        /*Limitar el area de movimiento del mapa*/
        mapView.setScrollableAreaLimitDouble(getBoundingBox());

        /*En caso de querer dar algun enfoque a un bounding utilizar:*/
        //mapView.zoomToBoundingBox(getBoundingBox(),false);


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
        mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                /*Aca van las acciones al mover el mapa con dedo*/
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                //event.getZoomLevel();
                /*En caso de hacer zoom enfocar nuevamente el punto*/
                mapViewController.animateTo(pacific);
                return true;
            }
        });
    }



    /*Define el bounding box se pueden definir distintos para cada zoom, lat,lon, lat,lon*/
    private BoundingBox getBoundingBox() {
        return new BoundingBox(11.049064, -85.739971, 11.024071, -85.687384);
    }






public void findFiles(Context context){
    /*Se busca el archivo dentro del path*/
    File tiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/tiles.zip");
    if (tiles.exists()) {
                    try {
                        //Se crea y utiliza en archivo
                        File[] file=new File[]{tiles};
                        OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context),file);

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
        Toast.makeText(context, tiles.getAbsolutePath() + " No existe mapa, en el dispositivo, cargando desde internet", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(context, tiles.getAbsolutePath() + "El directorio no existe", Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Toast.makeText(mainActivity, "Latitud: "+mCurrentLocation.getLatitude()+" y Longitud: "+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


}
/*
Clase de https://mobiledevstories.wordpress.com/2014/03/01/osmdroid-bonus-pack-markers-with-clickable-infowindows/
Sirve para crear un infowindow
private class MyInfoWindow extends InfoWindow {
    public MyInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }
    public void onClose() {
    }

    public void onOpen(Object arg0) {
        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.bubble_layout);
        Button btnMoreInfo = (Button) mView.findViewById(R.id.bubble_moreinfo);
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView.findViewById(R.id.bubble_subdescription);

        txtTitle.setText("Title of my marker");
        txtDescription.setText("Click here to view details!");
        txtSubdescription.setText("You can also edit the subdescription");
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Override Marker's onClick behaviour here
            }
        });
    }
}*/
