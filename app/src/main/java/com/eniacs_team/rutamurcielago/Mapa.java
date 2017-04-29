package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.File;
import java.util.Set;

import static android.R.string.no;
import static android.R.string.yes;

/**
 * Created by Johan Duran Cerdas on 19/4/2017.
 */

public class Mapa extends Application {
    public static final GeoPoint pacific = new GeoPoint(11.028670, -85.704637);
    MapView mapView;
    Context mContext;
    Activity mActivity;
    CustomDialogClass dialogo;

    public Mapa(MapView map, Activity activity) {
        this.mapView = map;
        this.mActivity = activity;
    }

    public void setupMap(Context context) {
        /*En caso de error muestra este layout*/
        mapView.getTileProvider().setTileLoadFailureImage(context.getResources().getDrawable(R.drawable.notfound));
        this.mContext = context;




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
        Drawable marker = context.getResources().getDrawable(R.mipmap.marker);
        startMarker.setIcon(marker);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        InfoWindow infoWindow = new MyInfoWindow(R.layout.bonuspack_bubble, mapView);
        startMarker.setInfoWindow(infoWindow);
        startMarker.setTitle("Marcador");
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

        /*Creo el dialogo que se despliega en ver mas si no estoy cerca del punto*/
        dialogo = new CustomDialogClass(mActivity);
    }


    /*Define el bounding box se pueden definir distintos para cada zoom, lat,lon, lat,lon*/
    private BoundingBox getBoundingBox() {
        return new BoundingBox(11.049064, -85.739971, 11.024071, -85.687384);
    }


    public void findFiles(Context context) {
    /*Se busca el archivo dentro del path*/
        File tiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/tiles.zip");
        if (tiles.exists()) {
            try {
                //Se crea y utiliza en archivo
                File[] file = new File[]{tiles};
                OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context), file);

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
            Toast.makeText(context, tiles.getAbsolutePath() + "El directorio no existe.", Toast.LENGTH_SHORT).show();
        }
    }


//}

    //Clase de https://mobiledevstories.wordpress.com/2014/03/01/osmdroid-bonus-pack-markers-with-clickable-infowindows/
//Sirve para crear un infowindow
    private class MyInfoWindow extends InfoWindow {
        public MyInfoWindow(int layoutResId, MapView mapView) {
            super(layoutResId, mapView);
        }

        public void onClose() {
        }

        public void onOpen(Object arg0) {
            LinearLayout layout = (LinearLayout) mView.findViewById(R.id.bonuspack_bubble);

            ImageView img = (ImageView) mView.findViewById(R.id.bubble_image);

            BaseDatos base = new BaseDatos(mContext);
            img.setImageDrawable(base.selectImagen(2));

            TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
            TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
            TextView txtVerMas = (TextView) mView.findViewById(R.id.ver_mas);
            View viewLinea = mView.findViewById(R.id.linea_centro);

            //viewLinea.setMinimumWidth(txtDescription.getWidth());



            txtVerMas.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialogo.show();
                   // dialog.show();
                }


            });
            txtTitle.setText("Punto #1");
            txtDescription.setText(base.selectDescripcion(1));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(txtDescription.getMaxWidth(), 3);
            lp.setMargins(0,20,15,0);
            viewLinea.setLayoutParams(lp);


            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Override Marker's onClick behaviour here
                }
            });
        }


    }

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Button btnAceptar;

        public CustomDialogClass(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogo_ver_mas);
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