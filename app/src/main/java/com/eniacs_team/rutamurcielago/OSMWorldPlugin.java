package com.eniacs_team.rutamurcielago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

import com.beyondar.android.plugin.GeoObjectPlugin;
import com.beyondar.android.plugin.WorldPlugin;
import com.beyondar.android.util.ImageUtils;
import com.beyondar.android.util.PendingBitmapsToBeLoaded;
import com.beyondar.android.util.cache.BitmapCache;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.BeyondarObjectList;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class OSMWorldPlugin implements WorldPlugin, BitmapCache.OnExternalBitmapLoadedCacheListener {
    /** Default icon size for the markers in dips */
    public static final int DEFAULT_ICON_SIZE_MARKER = 40;

    private World mWorld;
    private MapView mMap;

    private BitmapCache mCache;
    private int mIconSize;
    private PendingBitmapsToBeLoaded<GeoObject> mPendingBitmaps;

    private HashMap<Marker, OSMGeoObjectPlugin> mMarkerHashMap;

    private GeoPoint mLatLng;

    private boolean mAttached;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private Context mContext;

    public OSMWorldPlugin(Context context) {
        mMarkerHashMap = new HashMap<Marker, OSMGeoObjectPlugin>();
        mPendingBitmaps = new PendingBitmapsToBeLoaded<GeoObject>();
        mAttached = false;
        mContext = context;
    }

    public OSMWorldPlugin(Context context, MapView map) {
        this(context);
        mMap = map;
    }

    /**
     * Set the size of the marker icons in pixels
     *
     * @param iconSize
     * @return The instance itself
     */
    public OSMWorldPlugin setMarkerIconSize(int iconSize) {
        mIconSize = iconSize;
        return this;
    }

    protected BitmapCache createBitmapCache() {
        return mWorld.getBitmapCache().newCache(getClass().getName(), true);
    }

    /**
     * This method adds the {@link OSMGeoObjectPlugin} to the
     * {@link com.beyondar.android.world.GeoObject GeoObject}
     *
     * @param beyondarObject
     */
    protected void addGooGleMapPlugin(BeyondarObject beyondarObject) {
        if (beyondarObject instanceof GeoObject) {
            if (!beyondarObject.containsAnyPlugin(OSMGeoObjectPlugin.class)) {
                OSMGeoObjectPlugin plugin = new OSMGeoObjectPlugin(this, beyondarObject, mContext, mMap);
                beyondarObject.addPlugin(plugin);
                createMarker((GeoObject) beyondarObject, plugin);
            }
        }
    }

    public GeoPoint getLatLng() {
        if (mLatLng == null) {
            mLatLng = new GeoPoint(mWorld.getLatitude(), mWorld.getLongitude());
            return mLatLng;
        }

        if (mLatLng.getLatitude() == mWorld.getLatitude() && mLatLng.getLongitude() == mWorld.getLongitude()) {
            return mLatLng;
        }

        mLatLng = new GeoPoint(mWorld.getLatitude(), mWorld.getLongitude());
        return mLatLng;
    }

    /**
     * Set the {@link MapView} to be able to create the markers for the world
     *
     * @param map
     * @return The instance of itself
     */
    public OSMWorldPlugin setOSMap(MapView map) {
        mMap = map;
        createMarkers();
        return this;
    }

    public void createMarkers() {
        if (mWorld == null || mMap == null) {
            return;
        }
        for (int i = 0; i < mWorld.getBeyondarObjectLists().size(); i++) {
            BeyondarObjectList list = mWorld.getBeyondarObjectList(i);
            for (int j = 0; j < list.size(); j++) {
                BeyondarObject beyondarObject = list.get(j);
                if (beyondarObject instanceof GeoObject) {
                    createMarker((GeoObject) beyondarObject);
                }
            }
        }
    }

    protected void createMarker(GeoObject geoObject) {
        createMarker(geoObject,
                (OSMGeoObjectPlugin) geoObject.getFirstPlugin(OSMGeoObjectPlugin.class));
    }

    protected void createMarker(GeoObject geoObject, OSMGeoObjectPlugin plugin) {
        if (geoObject == null || plugin == null) {
            return;
        }
        Marker marker = plugin.getMarker();
        if (marker != null) {
            marker.remove(mMap);
        }

        if (mMap == null) {
            return;
        }

        marker = createMarkerOptions(geoObject, plugin);
        if (marker != null) {
            mMap.getOverlays().add(marker);
            plugin.setMarker(marker);
        }
    }

    public void registerMarker(Marker marker, OSMGeoObjectPlugin plugin) {
        mMarkerHashMap.put(marker, plugin);
    }

    protected Marker createMarkerOptions(GeoObject geoObject, OSMGeoObjectPlugin plugin) {
        if (geoObject == null || plugin == null) {
            return null;
        }
        Bitmap btm = getBitmapFromGeoObject(geoObject);

        return plugin.createMarkerOptions(btm);

    }

    protected Marker createMarkerOptions(GeoObject geoObject) {
        if (geoObject == null) {
            return null;
        }
        OSMGeoObjectPlugin plugin = (OSMGeoObjectPlugin) geoObject
                .getFirstPlugin(OSMGeoObjectPlugin.class);

        return createMarkerOptions(geoObject, plugin);
    }

    private Bitmap getBitmapFromGeoObject(GeoObject geoObject) {
        if (geoObject.getImageUri() == null) {
            return null;
        }
        boolean canRemove = !mPendingBitmaps.existPendingList(geoObject.getImageUri());
        if (!mCache.isImageLoaded(geoObject.getImageUri())) {
            mPendingBitmaps.addObject(geoObject.getImageUri(), geoObject);
        }
        Bitmap btm = mCache.getBitmap(geoObject.getImageUri());

        if (btm == null) {
            String uri = mWorld.getDefaultImage(geoObject.getWorldListType());
            btm = mCache.getBitmap(uri);
        } else if (canRemove) {
            mPendingBitmaps.removePendingList(geoObject.getImageUri());
        }

        return resizeBitmap(geoObject.getImageUri(), btm);
    }

    public void setMarkerImage(Marker marker, GeoObject geoObject) {
        if (marker == null || geoObject == null) {
            return;
        }
        Bitmap btm = getBitmapFromGeoObject(geoObject);
        if (btm != null) {
            Drawable d = new BitmapDrawable(mContext.getResources(), btm);
            marker.setIcon(d);
        }
    }

    protected Bitmap resizeBitmap(String uri, Bitmap btm) {
        if (btm == null || uri == null) {
            return null;
        }
        if (btm.getHeight() != mIconSize && btm.getWidth() != mIconSize) {
            Bitmap tmp = ImageUtils.resizeImage(btm, mIconSize, mIconSize);
            mCache.storeBitmap(uri, tmp);
            if (btm != tmp) {
                btm.recycle();
            }
            btm = tmp;
        }
        return btm;
    }

    @Override
    public void onExternalBitmapLoaded(BitmapCache cache, String url, Bitmap btm) {
        final Bitmap resizedBtm = resizeBitmap(url, btm);
        ArrayList<GeoObject> list = mPendingBitmaps.getPendingList(url);
        for (int i = 0; i < list.size(); i++) {
            GeoObject gogm = list.get(i);

            final OSMGeoObjectPlugin plugin = (OSMGeoObjectPlugin) gogm
                    .getFirstPlugin(OSMGeoObjectPlugin.class);
            if (plugin != null) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (plugin.isAttached() && resizedBtm != null) {
                            Drawable d = new BitmapDrawable(mContext.getResources(), resizedBtm);
                            plugin.getMarker().setIcon(d);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setup(World world) {
        mWorld = world;
        if (mIconSize == 0) {
            mIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_ICON_SIZE_MARKER, mContext.getResources().getDisplayMetrics());
        }

        mCache = createBitmapCache();
        mCache.addOnExternalBitmapLoadedCahceListener(this);
        createMarkers();
        mAttached = true;

        addPluginToAllObjects();

        createMarkers();
    }

    private void addPluginToAllObjects() {
        List<BeyondarObjectList> beyondARLists = mWorld.getBeyondarObjectLists();
        for (BeyondarObjectList list : beyondARLists) {
            for (BeyondarObject beyondarObject : list) {
                addGooGleMapPlugin(beyondarObject);
            }
        }
    }

    @Override
    public void onDetached() {
        mAttached = false;
        mCache.clean();
    }

    @Override
    public boolean isAttached() {
        return mAttached;
    }

    @Override
    public void onBeyondarObjectAdded(BeyondarObject beyondarObject, BeyondarObjectList beyondarObjectList) {
        addGooGleMapPlugin(beyondarObject);
    }

    @Override
    public void onBeyondarObjectListCreated(BeyondarObjectList beyondarObjectList) {
    }

    @Override
    public void onBeyondarObjectRemoved(BeyondarObject beyondarObject, BeyondarObjectList beyondarObjectList) {
        if (beyondarObject instanceof GeoObject) {
            GeoObject geoObject = (GeoObject) beyondarObject;
            OSMGeoObjectPlugin gogmMod = (OSMGeoObjectPlugin) geoObject
                    .getFirstPlugin(OSMGeoObjectPlugin.class);
            if (gogmMod != null) {
                if (gogmMod.getMarker() != null) {
                    mMarkerHashMap.remove(gogmMod.getMarker());
                }
            }
        }
    }

    /**
     * Retrieve the {@link com.beyondar.android.world.GeoObject GeoObject} that
     * owns an specific {@link Marker}
     *
     * @param marker
     *            The Marker that whant's to be checked
     * @return The {@link com.beyondar.android.world.GeoObject GeoObject} owner
     *         or null if there is no owner
     */
    public GeoObject getGeoObjectOwner(Marker marker) {
        GeoObjectPlugin geoObjectPlugin = mMarkerHashMap.get(marker);
        if (geoObjectPlugin != null) {
            return geoObjectPlugin.getGeoObject();
        }
        return null;
    }

    @Override
    public void onWorldCleaned() {
        mMarkerHashMap.clear();
        mPendingBitmaps.clear();
    }

    @Override
    public void onGeoPositionChanged(double latitude, double longitude, double altitude) {
    }

    @Override
    public void onDefaultImageChanged(String uri) {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }
}
