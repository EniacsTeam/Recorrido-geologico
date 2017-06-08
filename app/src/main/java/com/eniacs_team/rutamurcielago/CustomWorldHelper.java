package com.eniacs_team.rutamurcielago;

import android.annotation.SuppressLint;
import android.content.Context;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

/**
 * Clase ayudante para crear el mundo de realidad aumentada el cual poseera objetos georeferenciados.
 *
 * @author EniacsTeam
 */
@SuppressLint("SdCardPath")
public class CustomWorldHelper {

    public static World sharedWorld;

    private static BaseDatos baseDatos;

    private static final double[] longitudes = DatosGeo.longitudes();
    private static final double[] latitudes = DatosGeo.latitudes();

    private static final int[] resources
            = {R.drawable.inicial,R.drawable.descartes,R.drawable.vegetacion,R.drawable.peridotitas,R.drawable.santa_ana,
            R.drawable.rocas_sedimentarias,R.drawable.descartes2,R.drawable.pliegues,R.drawable.maravillas,
            R.drawable.rocas_inclinadas,R.drawable.erosion,R.drawable.intrusiones,R.drawable.peridotitas2, R.drawable.dunas,
            R.drawable.santa_rosa,R.drawable.isla,R.drawable.arcos,R.drawable.upwelling,R.drawable.paleodunas};

    /**
     * Metodo que agrega los carteles de realidad aumentada a puntos georeferenciados
     */
    private static void agregarCarteles() {
        /*Se crea un cartel para cada punto en el mapa*/
        for (int i = 0; i < DatosGeo.cantidadElementos; i++)
        {
            // Create an object with an image in the app resources.
            GeoObject go = new GeoObject(100+i);
            go.setGeoPosition(latitudes[i], longitudes[i]);
            go.setImageResource(resources[i]);
            go.setName(baseDatos.selectDescripcion(i+1));
            // Add the GeoObjects to the world
            sharedWorld.addBeyondarObject(go);
        }

        /* Para muestra al cliente
         * Comentar bloque for arriba
         * Descomentar lo siguiente */

        /*
        GeoObject go1 = new GeoObject(100);
        go1.setGeoPosition(10.951271d, -85.709449d);
        go1.setImageResource(R.drawable.descartes);
        go1.setName(baseDatos.selectDescripcion(1));

        // Create an object with an image in the app resources.
        GeoObject go2 = new GeoObject(101);
        go2.setGeoPosition(10.951394d, -85.709024d);
        go2.setImageResource(R.drawable.vegetacion);
        go2.setName(baseDatos.selectDescripcion(2));

        // Create an object with an image in the app resources.
        GeoObject go3 = new GeoObject(102);
        go3.setGeoPosition(10.951771d, -85.709807d);
        go3.setImageResource(R.drawable.peridotitas);
        go3.setName(baseDatos.selectDescripcion(3));

        sharedWorld.addBeyondarObject(go1);
        sharedWorld.addBeyondarObject(go2);
        sharedWorld.addBeyondarObject(go3);
        */
    }

    /**
     * Si no existe un mundo lo crea, genera objetos georeferenciados y los introduce dentro de dicho mundo.
     *
     * @param context contexto del estado actual de la aplicacion
     * @return el mundo de realidad aumentada
     */
    public static World generateObjects(Context context) {
        if (sharedWorld != null) {
            return sharedWorld;
        }
        sharedWorld = new World(context);
        baseDatos = BaseDatos.getInstancia();

        // User position (you can change it using the GPS listeners form Android
        // API)
        //sharedWorld.setGeoPosition(10.926201d, -85.818870d);
        sharedWorld.setGeoPosition(10.951398d, -85.709450d); //Cerca de descartes

        //sharedWorld.setGeoPosition(10.9408d, -85.774d); //Cerquisima de vegetacion

        agregarCarteles();

        return sharedWorld;
    }

}
