package com.eniacs_team.rutamurcielago;

import android.app.Activity;

/**
 * Created by kenca on 21/04/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kenca on 18/04/2017.
 */


/*Clase que tiene el splash mientras carga la aplicación.*/
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        // close splash activity
        finish();
    }
}