package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

import org.osmdroid.events.MapEventsReceiver;
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
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.SENSOR_SERVICE;
import static com.eniacs_team.rutamurcielago.R.mipmap.marker;
import static org.osmdroid.views.overlay.infowindow.InfoWindow.getOpenedInfoWindowsOn;

/**
 * Esta clase representa un mapa de OpenStreet Maps. Contiene distintos metodos para su correcto funcionamiento en la aplicacion.
 *
 * @author  EniacsTeam
 */

public class Mapa implements MapEventsReceiver{
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    protected ImageButton btCenterMap;

    MapView mapView;
    Context mContext;
    public static final GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);
    List<GeoPoint> locations;
    List<Marker> marcadores;
    Activity activity;
    CustomDialogClass dialogo;
    InfoWindow infoWindow;

    private OSMWorldPlugin mOSMapPlugin;
    private World mWorld;
    Marker marcador_anterior;
    Marker marcador_actual;

    boolean isMarker = true;

    Marker.OnMarkerClickListener markerClickListener;
    MapView.OnClickListener mapViewListener;
    MapEventsOverlay mapEventsOverlay;

    /**
     * Constructor de la clase mapa
     *
     * @param map      vista del mapa
     * @param activity actividad donde se crea el mapa
     */
    public Mapa(final MapView map, final Activity activity) {

        this.mapView = map;
        this.locations = new ArrayList<>();
        this.marcadores = new ArrayList<>();
        this.activity = activity;
        marcador_actual = null;

        double[] latitude = DatosGeo.latitudes();;
        double[] longitud = DatosGeo.longitudes();

        for (int i = 0; i < longitud.length; i++) {
            locations.add(i, new GeoPoint(latitude[i], longitud[i]));
            marcadores.add(i, new Marker(map));
            marcadores.get(i).setTitle(String.valueOf(i));
        }

        markerClickListener = new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                isMarker = true;
                if(marcador_anterior == null)
                {
                    marcador_anterior = new Marker(map);
                    marcador_actual = marker;
                    //marker.setIcon(activity.getDrawable(R.drawable.ic_marker_selected));
                    marker.setAlpha(0.7f);
                    marker.showInfoWindow();
                }
                else if (marker != marcador_actual)
                {
                    marcador_anterior = marcador_actual;
                    marcador_anterior.closeInfoWindow();
                    //marcador_anterior.setIcon(activity.getDrawable(R.drawable.ic_marker_naranja));
                    marcador_anterior.setAlpha(1);
                    marcador_actual = marker;
                    //marcador_actual.setIcon(activity.getDrawable(R.drawable.ic_marker_selected));
                    marcador_actual.setAlpha(0.7f);
                    marcador_actual.showInfoWindow();
                }else{
                    if (marcador_actual.isInfoWindowShown()){
                        marcador_actual.closeInfoWindow();
                        //marcador_actual.setIcon(activity.getDrawable(R.drawable.ic_marker_naranja));
                        marcador_actual.setAlpha(1);
                    }else{
                        //marcador_actual.setIcon(activity.getDrawable(R.drawable.ic_marker_selected));
                        marcador_actual.setAlpha(0.7f);
                        marker.showInfoWindow();
                    }

                }
                return false;
            }
        };
    }

    /**
     * Metodo para configurar el mapa
     *
     */
    public void setupMap() {
        /*En caso de error muestra este layout*/
        mapView.getTileProvider().setTileLoadFailureImage(activity.getResources().getDrawable(R.drawable.notfound));
        this.mContext = activity;

        /*Elementos correspondietes a funcionalidades*/
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        //mapView.setUseDataConnection(true);
        mapView.setTilesScaledToDpi(true);

        this.mCompassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity),
                mapView);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(activity),
                mapView);
        mapView.getOverlays().add(this.mLocationOverlay);
        mapView.getOverlays().add(this.mCompassOverlay);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.setOptionsMenuEnabled(true);
        mCompassOverlay.enableCompass();


        /*Ajustes en el zoom y el enfoque inicial*/
        final MapController mapViewController = (MapController) mapView.getController();
        mapViewController.setZoom(13);
        mapViewController.animateTo(routeCenter);
        mapViewController.setCenter(routeCenter);
        mapView.setMinZoomLevel(12);
        mapView.setMaxZoomLevel(16);

        //Desactivar botones de zoom nativos.
        mapView.setBuiltInZoomControls(false);

        /*Limitar el area de movimiento del mapa*/
        mapView.setScrollableAreaLimitDouble(DatosGeo.getBoundingBox(1));

        mapEventsOverlay = new MapEventsOverlay(mContext,this);
        mapView.getOverlays().add(0, mapEventsOverlay);

        /*Creo el dialogo que se despliega en ver mas si no estoy cerca del punto*/
        dialogo = new CustomDialogClass(activity);

        mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if(event.getZoomLevel()!=16){
                    mapView.setScrollableAreaLimitDouble(DatosGeo.getBoundingBox(1));
                }else{
                    mapViewController.animateTo(new GeoPoint(10.925547, -85.818351));
                    mapView.setScrollableAreaLimitDouble(DatosGeo.getBoundingBox(2));
                }
                return true;
            }
        });


    }




    /**
     * Metodo que busca el mapa descargado en el telefono para poder cargarlo a la aplicacion
     */
    public void findMapFiles() {
    /*Se busca el archivo dentro del path*/
        File tiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/osmdroid/tiles.zip");
        if (tiles.exists()) {
            try {
                //Se crea y utiliza en archivo
                File[] file = new File[]{tiles};
                OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity), file);

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

    /**
     * Metodo que agrega los marcadores al mapa
     */
    public List<Marker> agregarMarcadores() {

    /*Se crea un marcador para cada punto en el mapa*/
        for (int i = 0; i < locations.size(); i++) {
            Marker marcador = marcadores.get(i);
            marcador.setPosition(locations.get(i));
            Drawable marker=activity.getResources().getDrawable(R.drawable.ic_marker_naranja);
            marcador.setIcon(marker);
            marcador.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            marcador.setTitle("Title of the marker");
            infoWindow = new MyInfoWindow(R.layout.bonuspack_bubble, mapView, i + 1,false, mContext,dialogo);
            marcador.setInfoWindow(infoWindow);
            marcador.setOnMarkerClickListener(markerClickListener);

            mapView.getOverlays().add(marcador);
            marcadores.set(i,marcador);
        }
        return marcadores;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if (!isMarker) {
            InfoWindow.closeAllInfoWindowsOn(mapView);
            //marcador_actual.setIcon(activity.getDrawable(R.drawable.ic_marker_naranja));
            marcador_actual.setAlpha(1);
        }
        isMarker = false;
        return true;
    }
    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }



    /**
     * Clase para generar la ventana de informacion para cada punto de interes
     */

    public class MyInfoWindow extends InfoWindow {
        int puntoCargado;
        boolean tipo;
        Context mContext;
        CustomDialogClass dialogo;
        TextView txtVerMas;
        /**
         * Constructor de la ventana de informacion
         *
         * @param layoutResId es el layout que se quiere para mostrar la ventana
         * @param mapView     es el mapa
         * @param puntoCargar es el punto de interes asociado a la ventana
         */
        public MyInfoWindow(int layoutResId, MapView mapView, int puntoCargar,boolean tipo, Context context,
                            CustomDialogClass dialogo) {
            super(layoutResId, mapView);
            puntoCargado = puntoCargar;
            this.tipo = tipo;
            this.mContext= context;
            this.dialogo= dialogo;
        }

        public void onClose() {
        }

        public void setTipo(boolean t){
            tipo=t;
        }

        /**
         * Metodo para controlar lo que debe cargarse cada vez que se accede a una ventana
         *
         * @param arg0 es la ventana que se quiere acceder
         */
        public void onOpen(Object arg0) {

            LinearLayout layout = (LinearLayout) mView.findViewById(R.id.bonuspack_bubble);


            BaseDatos base = new BaseDatos(mContext);

            TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
            TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
            txtVerMas = (TextView) mView.findViewById(R.id.ver_mas);
            View viewLinea = mView.findViewById(R.id.linea_centro);


            txtVerMas.setOnClickListener(new View.OnClickListener() {

                /**
                 * Metodo para mostrar el dialogo en caso de que el usuario se encuentre fuera del rango del punto
                 * de interes
                 * @param v es la vista donde se muestra el dialogo
                 */
                @Override
                public void onClick(View v) {
                    if(tipo){
                        Intent intent = new Intent( mContext,MenuMultimediaMapa.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", puntoCargado);
                        mContext.startActivity(intent);
                    }else{
                        dialogo.show();
                    }
                }


            });
            txtTitle.setText("Punto #" + puntoCargado);
            txtDescription.setText(base.selectDescripcion(puntoCargado));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(txtDescription.getMaxWidth(), 3);
            lp.setMargins(0, 20, 15, 0);
            viewLinea.setLayoutParams(lp);

        }

    }

    /**
     * Clase para controlar el dialogo que indica que el usuario esta fuera del rango del
     * punto de interes
     */
    public static class CustomDialogClass extends Dialog implements
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