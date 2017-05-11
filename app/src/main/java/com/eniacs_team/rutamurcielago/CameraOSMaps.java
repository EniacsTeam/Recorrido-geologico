package com.eniacs_team.rutamurcielago;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.world.World;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import static android.R.id.button1;
import static android.R.id.button2;
import static android.R.id.button3;

/**
 * Esta actividad proporciona una camara de realidad aumentada que permite sobreponer elementos digitales sobre el mundo real.
 *
 * @author EniacsTeam
 */
public class CameraOSMaps extends FragmentActivity implements OnClickListener {

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private Button mShowMap;
    private BaseDatos baseDatos;
    private AnimationDrawable mouthAnimation;
    private ImageButton charButton;

    private FloatingActionButton actionButton;
    private SubActionButton btn_video;
    private SubActionButton btn_audio;
    private SubActionButton btn_imagen;
    private SubActionButton btn_animacion;

    /**
     * Inicializa la vista, crea el mundo de realidad aumentada y asocia este mundo al fragmento de la camara.
     *
     * @param savedInstanceState contiene los datos mas recientemente suministrados en {@link #onSaveInstanceState(Bundle) onSaveInstanceState}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Esconde el titulo de la ventana.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViewFromXML();

        // We create the world and fill it
        mWorld = CustomWorldHelper.generateObjects(this);

        mBeyondarFragment.setWorld(mWorld);
    }

    /**
     * Carga y establece la vista de la actividad. Ademas, resuelve referencias a elementos visuales de dicha vista,
     * asocia un escuchador de clicks a algunos de ellos y fija el fondo para una de las vistas.
     */
    private void loadViewFromXML() {
        setContentView(R.layout.camera_with_osmaps);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        mShowMap = (Button) findViewById(R.id.showMapButton);
        mShowMap.setOnClickListener(this);
        baseDatos = new BaseDatos(this);
        crearFab();
    }

    /**
     * Metodo encargado de devolverse al mapa o iniciar la animación dependiendo de cual ha sido el boton apretado por el
     * usuario.
     *
     * @param v la vista que fue seleccionada
     */
    @Override
    public void onClick(View v) {
        if (v == mShowMap) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    /**
     * Metodo encargado de crear fab.
     */
    private void crearFab() {
        boolean boolAudio = true;
        boolean boolVideo = true;

        // Create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.mipmap.menu);


        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        //Pongo tamano de cada sub boton
        FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(180, 180);
        itemBuilder.setLayoutParams(layoutParams);

        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.mipmap.video);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.audio);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.imagen);

        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.mipmap.percy);


        //Creo botones
        btn_video = itemBuilder.setContentView(itemIcon1).build();
        btn_audio = itemBuilder.setContentView(itemIcon2).build();
        btn_imagen = itemBuilder.setContentView(itemIcon3).build();
        btn_animacion = itemBuilder.setContentView(itemIcon4).build();

        //Se agregan los botones al fab y verifica que agrega y que no
        fabBuilder();

        //Creo listener para los botones
        OnClickListener btn_Listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_video) {
                    Toast.makeText(getApplicationContext(), "Toque video", Toast.LENGTH_SHORT).show();
                }

                if (v == btn_audio) {
                    Toast.makeText(getApplicationContext(), "Toque audio", Toast.LENGTH_SHORT).show();
                }

                if (v == btn_imagen) {
                    Toast.makeText(getApplicationContext(), "Toque imagen", Toast.LENGTH_SHORT).show();
                }

                if (v == btn_animacion) {
                    Toast.makeText(getApplicationContext(), "Toque animacion", Toast.LENGTH_SHORT).show();
                }

            }
        };

        //Adjunto listener a los botones
        btn_video.setOnClickListener(btn_Listener);
        btn_audio.setOnClickListener(btn_Listener);
        btn_imagen.setOnClickListener(btn_Listener);
        btn_animacion.setOnClickListener(btn_Listener);








    }

    /**
     * Metodo encargado de agregar los botones al fab
     *
     */
    private void fabBuilder()
    {
        FloatingActionMenu actionMenu;

        //Pregunto si hay videos y audio en la base para agregarlos o no al fab
        if(baseDatos.existenciaPunto(idPunto, Video) == 0 && (baseDatos.existenciaPunto(idPunto, Audio) == 0) )
        {

            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(280)
                    .build();

        }

        //Pregunto si hay audios en la base para agregarlo o no al fab
        else if(baseDatos.existenciaPunto(idPunto, Audio) == 0)
        {
            //attach the sub buttons to the main button
             actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_video)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(280)
                    .build();
        }
        //Pregunto si hay videos en la base para agregarlo o no al fab
        else if(baseDatos.existenciaPunto(idPunto, Video) == 0)
        {
            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_animacion)
                    .addSubActionView(btn_audio)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(280)
                    .build();
        }
        else
        {
            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_animacion)
                    .addSubActionView(btn_video)
                    .addSubActionView(btn_audio)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(280)
                    .build();
        }

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

                Toast.makeText(getApplicationContext(), "Menu abierto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {

                Toast.makeText(getApplicationContext(), "Menu cerrado", Toast.LENGTH_SHORT).show();
            }
        });


    }




}
