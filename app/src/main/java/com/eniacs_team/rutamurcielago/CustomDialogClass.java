package com.eniacs_team.rutamurcielago;

/**
 * Created by Johan Duran Cerdas on 23/5/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Clase para controlar el dialogo que indica que el usuario esta fuera del rango del
 * punto de interes
 */
public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Button btnAceptar;
    public Button btnVisitarSitio;

    static int id;
    //id=1 no está dentro del recorrido; id=2 no está dentro del punto
    public CustomDialogClass(Activity a, int identificador) {
        super(a);
        this.c = a;
        this.id = identificador;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(id==2){
            setContentView(R.layout.dialogo_ver_mas);
        }else{
            setContentView(R.layout.web_site_link);
        }
        btnAceptar = (Button) findViewById(R.id.aceptar);
        btnAceptar.setOnClickListener(this);
        btnVisitarSitio = (Button) findViewById(R.id.visitar_sitio);
        btnVisitarSitio.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aceptar:
                dismiss();
                break;
            case R.id.visitar_sitio:
                Uri uriUrl = Uri.parse("http://www.geologia.ucr.ac.cr/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                c.startActivity(launchBrowser);
                break;
            default:
                break;
        }
        dismiss();
    }
}