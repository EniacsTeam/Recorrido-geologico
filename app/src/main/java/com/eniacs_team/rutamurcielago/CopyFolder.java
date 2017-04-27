package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Johan Duran Cerdas on 19/4/2017.
 */

public class CopyFolder {

    public static void copyAssets(Activity activity) {

        InputStream in = null;
        OutputStream out = null;
        try {

            in = activity.getAssets().open("tiles.zip");

            Log.i(TAG, ": " + Environment.getExternalStorageDirectory());
            File dir = new File(Environment.getExternalStorageDirectory(),
                    "osmdroid");
            Log.i(TAG, "existe: " + dir.exists());
            if (!dir.exists())
                dir.mkdirs();
            File fileZip = new File(dir, "tiles.zip");
            Log.i(TAG, "existe : " + fileZip.exists());

            out = new FileOutputStream(fileZip);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Error al copiar el archivo: " + e.getMessage());
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


}
