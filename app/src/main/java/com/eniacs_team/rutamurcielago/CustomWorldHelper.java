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
        sharedWorld.setGeoPosition(10.936519d, -85.782294d);

        // Create an object with an image in the app resources.
        GeoObject go1 = new GeoObject(1l);
        go1.setGeoPosition(10.936514d, -85.782079d);
        go1.setImageResource(R.drawable.github_logo);
        go1.setName("GitHub Logo");

        // Also possible to get images from the SDcard
        /*GeoObject go3 = new GeoObject(3l);
        go3.setGeoPosition(41.90550959641445d, 2.565873388087619d);
        go3.setImageUri("/sdcard/someImageInYourSDcard.jpeg");
        go3.setName("IronMan from sdcard");*/

        // And the same goes for the app assets
        /*GeoObject go4 = new GeoObject(4l);
        go4.setGeoPosition(41.90518862002349d, 2.565662767707665d);
        go4.setImageUri("assets://creature_7.png");
        go4.setName("Image from assets");*/

        // Add the GeoObjects to the world
        sharedWorld.addBeyondarObject(go1);

        return sharedWorld;
    }

}
