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

    private static final double[] longitudes = DatosGeo.longitudes();
    private static final double[] latitudes = DatosGeo.latitudes();

    /**
     * Metodo que agrega los carteles de realidad aumentada a puntos georeferenciados
     */
    private static void agregarCarteles() {
        /*Se crea un cartel para cada punto en el mapa*/

        // Create an object with an image in the app resources.
        GeoObject go1 = new GeoObject(100);
        go1.setGeoPosition(latitudes[0], longitudes[0]);
        go1.setImageResource(R.drawable.descartes);
        go1.setName("Formacion Descartes");

        // Create an object with an image in the app resources.
        GeoObject go2 = new GeoObject(101);
        go2.setGeoPosition(latitudes[1], longitudes[1]);
        go2.setImageResource(R.drawable.vegetacion);
        go2.setName("Vegetacion peninsula");

        // Create an object with an image in the app resources.
        GeoObject go3 = new GeoObject(102);
        go3.setGeoPosition(latitudes[2], longitudes[2]);
        go3.setImageResource(R.drawable.peridotitas);
        go3.setName("Peridotitas");

        // Create an object with an image in the app resources.
        GeoObject go4 = new GeoObject(103);
        go4.setGeoPosition(latitudes[3], longitudes[3]);
        go4.setImageResource(R.drawable.santa_ana);
        go4.setName("Formacion Santa Ana");

        // Create an object with an image in the app resources.
        GeoObject go5 = new GeoObject(104);
        go5.setGeoPosition(latitudes[4], longitudes[4]);
        go5.setImageResource(R.drawable.piedras_blancas);
        go5.setName("Formacion Piedras Blancas");

        // Create an object with an image in the app resources.
        GeoObject go6 = new GeoObject(105);
        go6.setGeoPosition(latitudes[5], longitudes[5]);
        go6.setImageResource(R.drawable.rivas);
        go6.setName("Formacion Rivas");

        // Create an object with an image in the app resources.
        GeoObject go7 = new GeoObject(106);
        go7.setGeoPosition(latitudes[6], longitudes[6]);
        go7.setImageResource(R.drawable.rocas_sedimentarias);
        go7.setName("Rocas Sedimentarias");

        // Create an object with an image in the app resources.
        GeoObject go8 = new GeoObject(107);
        go8.setGeoPosition(latitudes[7], longitudes[7]);
        go8.setImageResource(R.drawable.descartes2);
        go8.setName("De vuelta a Descartes");

        // Create an object with an image in the app resources.
        GeoObject go9 = new GeoObject(108);
        go9.setGeoPosition(latitudes[8], longitudes[8]);
        go9.setImageResource(R.drawable.pliegues);
        go9.setName("Pliegues sinsedimentarios");

        // Create an object with an image in the app resources.
        GeoObject go10 = new GeoObject(109);
        go10.setGeoPosition(latitudes[9], longitudes[9]);
        go10.setImageResource(R.drawable.maravillas);
        go10.setName("Maravillas naturales");

        // Create an object with an image in the app resources.
        GeoObject go11 = new GeoObject(110);
        go11.setGeoPosition(latitudes[10], longitudes[10]);
        go11.setImageResource(R.drawable.rocas_inclinadas);
        go11.setName("Rocas inclinadas");

        // Create an object with an image in the app resources.
        GeoObject go12 = new GeoObject(111);
        go12.setGeoPosition(latitudes[11], longitudes[11]);
        go12.setImageResource(R.drawable.erosion);
        go12.setName("Erosion peninsula");

        // Create an object with an image in the app resources.
        GeoObject go13 = new GeoObject(112);
        go13.setGeoPosition(latitudes[12], longitudes[12]);
        go13.setImageResource(R.drawable.intrusiones);
        go13.setName("Intrusiones");

        // Create an object with an image in the app resources.
        GeoObject go14 = new GeoObject(113);
        go14.setGeoPosition(latitudes[13], longitudes[13]);
        go14.setImageResource(R.drawable.peridotitas2);
        go14.setName("Mas peridotitas");

        // Create an object with an image in the app resources.
        GeoObject go15 = new GeoObject(114);
        go15.setGeoPosition(latitudes[14], longitudes[14]);
        go15.setImageResource(R.drawable.dunas);
        go15.setName("Dunas costeras");

        // Create an object with an image in the app resources.
        GeoObject go16 = new GeoObject(115);
        go16.setGeoPosition(latitudes[15], longitudes[15]);
        go16.setImageResource(R.drawable.santa_rosa);
        go16.setName("Complejo Acrecional");

        // Create an object with an image in the app resources.
        GeoObject go17 = new GeoObject(116);
        go17.setGeoPosition(latitudes[16], longitudes[16]);
        go17.setImageResource(R.drawable.isla);
        go17.setName("Isla Colorada");

        // Create an object with an image in the app resources.
        GeoObject go18 = new GeoObject(117);
        go18.setGeoPosition(latitudes[17], longitudes[17]);
        go18.setImageResource(R.drawable.sobrecorrimientos);
        go18.setName("Sobrecorrimientos");

        // Create an object with an image in the app resources.
        GeoObject go19 = new GeoObject(118);
        go19.setGeoPosition(latitudes[18], longitudes[18]);
        go19.setImageResource(R.drawable.arcos);
        go19.setName("Roca arco");

        // Create an object with an image in the app resources.
        GeoObject go20 = new GeoObject(119);
        go20.setGeoPosition(latitudes[19], longitudes[19]);
        go20.setImageResource(R.drawable.basaltos);
        go20.setName("Basaltos");

        // Create an object with an image in the app resources.
        GeoObject go21 = new GeoObject(120);
        go21.setGeoPosition(latitudes[20], longitudes[20]);
        go21.setImageResource(R.drawable.upwelling);
        go21.setName("Upwelling");

        // Create an object with an image in the app resources.
        GeoObject go22 = new GeoObject(121);
        go22.setGeoPosition(latitudes[21], longitudes[21]);
        go22.setImageResource(R.drawable.paleodunas);
        go22.setName("Paleodunas");

        // Add the GeoObjects to the world
        sharedWorld.addBeyondarObject(go1);
        sharedWorld.addBeyondarObject(go2);
        sharedWorld.addBeyondarObject(go3);
        sharedWorld.addBeyondarObject(go4);
        sharedWorld.addBeyondarObject(go5);
        sharedWorld.addBeyondarObject(go6);
        sharedWorld.addBeyondarObject(go7);
        sharedWorld.addBeyondarObject(go8);
        sharedWorld.addBeyondarObject(go9);
        sharedWorld.addBeyondarObject(go10);
        sharedWorld.addBeyondarObject(go11);
        sharedWorld.addBeyondarObject(go12);
        sharedWorld.addBeyondarObject(go13);
        sharedWorld.addBeyondarObject(go14);
        sharedWorld.addBeyondarObject(go15);
        sharedWorld.addBeyondarObject(go16);
        sharedWorld.addBeyondarObject(go17);
        sharedWorld.addBeyondarObject(go18);
        sharedWorld.addBeyondarObject(go19);
        sharedWorld.addBeyondarObject(go20);
        sharedWorld.addBeyondarObject(go21);
        sharedWorld.addBeyondarObject(go22);
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

        // User position (you can change it using the GPS listeners form Android
        // API)
        sharedWorld.setGeoPosition(10.951398d, -85.709450d);

        agregarCarteles();

        return sharedWorld;
    }

}
