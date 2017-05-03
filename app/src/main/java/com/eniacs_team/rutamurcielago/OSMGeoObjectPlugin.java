/*
 * Copyright (C) 2014 BeyondAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Important: Some modifications were made to support working with OpenStreetMaps rather than GoogleMaps.
 */
package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.beyondar.android.opengl.renderable.Renderable;
import com.beyondar.android.opengl.texture.Texture;
import com.beyondar.android.plugin.GeoObjectPlugin;
import com.beyondar.android.util.math.geom.Point3;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class OSMGeoObjectPlugin implements GeoObjectPlugin {
    private Marker mMarker;
    private GeoPoint mLatLng;
    private GeoObject mGeoObject;
    private boolean mAttached;
    private OSMWorldPlugin mWorldOSMapPlugin;
    private Context context;
    private MapView mapView;

    public OSMGeoObjectPlugin(OSMWorldPlugin worldGoogleMapPlugin, BeyondarObject beyondarObject, Context c, MapView m) {
        mAttached = false;
        mWorldOSMapPlugin = worldGoogleMapPlugin;
        if (mWorldOSMapPlugin == null) {
            throw new NullPointerException("The WorldOSMapPlugin must not be null");
        }
        context = c;
        mapView = m;

        setBeyondarObject(beyondarObject);
    }

    /**
     * Setup the plugin according to the BeyondarObject
     *
     * @param beyondarObject
     */
    private void setBeyondarObject(BeyondarObject beyondarObject) {
        if (beyondarObject instanceof GeoObject) {
            mGeoObject = (GeoObject) beyondarObject;
        } else {
            // throw new
            // IllegalArgumentException("beyondarObject must be a GeoObject");
        }
        if (mGeoObject == null) {
            throw new NullPointerException("The BeyondarObject must not be null");
        }
        mAttached = true;
    }

    @Override
    public void onGeoPositionChanged(double latitude, double longitude, double altitude) {
        if (mMarker == null) {
            return;
        }
        mMarker.setPosition(getLatLng());
    }

    /**
     * Get the {@link GeoPoint} instance that represents the {@link com.beyondar.android.world.GeoObject GeoObject}. It
     * will try to recycle the {@link GeoPoint} object if it is possible
     *
     * @return
     */
    public GeoPoint getLatLng() {
        if (mLatLng == null) {
            mLatLng = new GeoPoint(mGeoObject.getLatitude(), mGeoObject.getLongitude());
            return mLatLng;
        }

        if (mLatLng.getLatitude() == mGeoObject.getLatitude() && mLatLng.getLongitude() == mGeoObject.getLongitude()) {
            return mLatLng;
        }

        mLatLng = new GeoPoint(mGeoObject.getLatitude(), mGeoObject.getLongitude());
        return mLatLng;
    }

    /**
     * Set the {@link Marker} that belongs to the {@link com.beyondar.android.world.GeoObject GeoObject}
     *
     * @param marker
     */
    public void setMarker(Marker marker) {
        mMarker = marker;
        mWorldOSMapPlugin.registerMarker(mMarker, this);
    }

    /**
     * Get the marker that belongs to the {@link com.beyondar.android.world.GeoObject GeoObject}
     *
     * @return
     */
    public Marker getMarker() {
        return mMarker;
    }

    @Override
    public GeoObject getGeoObject() {
        return mGeoObject;
    }

    /**
     * Create the marker options in order to create the Marker.
     *
     * @param bitmap
     *            The bitmap to use for representing the {@link Marker}
     * @return
     */
    public Marker createMarkerOptions(Bitmap bitmap) {
        Marker marker = new Marker(mapView);
        marker.setTitle(mGeoObject.getName());
        marker.setPosition(getLatLng());

        if (bitmap != null) {
            Drawable d = new BitmapDrawable(context.getResources(), bitmap);
            marker.setIcon(d);
        }
        return marker;
    }

    @Override
    public void onAngleChanged(Point3 angle) {
    }

    @Override
    public void onPositionChanged(Point3 position) {
    }

    @Override
    public void onTextureChanged(Texture texture) {
    }

    @Override
    public void onRenderableChanged(Renderable openglObject) {
    }

    @Override
    public void onFaceToCameraChanged(boolean faceToCamera) {
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
    }

    @Override
    public void onNameChanged(String name) {
        if (mMarker == null) {
            return;
        }
        mMarker.setTitle(name);
    }

    @Override
    public void onImageUriChanged(String uri) {
        mWorldOSMapPlugin.setMarkerImage(mMarker, mGeoObject);
    }

    @Override
    public void onDetached() {
        mAttached = false;
        if (mMarker == null) {
            return;
        }
        mMarker.remove(mapView);
    }

    @Override
    public boolean isAttached() {
        return mAttached;
    }

    @Override
    public BeyondarObject getbeyondarObject() {
        return getGeoObject();
    }
}
