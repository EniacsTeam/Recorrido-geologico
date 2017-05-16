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
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.beyondar.android.view.OnClickBeyondarObjectListener;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import static android.R.id.button1;
import static android.R.id.button2;
import static android.R.id.button3;
import static android.R.id.inputExtractEditText;

import java.util.ArrayList;

/**
 * Esta actividad proporciona una camara de realidad aumentada que permite sobreponer elementos digitales sobre el mundo real.
 *
 * @author EniacsTeam
 */
public class CameraOSMaps extends FragmentActivity implements OnClickListener, OnClickBeyondarObjectListener{

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private ImageButton mShowMap;
    private BaseDatos baseDatos;
    private Button mShowGallery;
    private int idPunto = 1;

    private FloatingActionButton actionButton;
    private SubActionButton btn_video;
    private SubActionButton btn_audio;
    private SubActionButton btn_imagen;
    private SubActionButton btn_animacion;

    private ImageView videoIcon;
    private ImageView audioIcon;
    private ImageView imagenIcon;
    private ImageView animacionIcon;
    private ImageView muteIcon;

    private boolean audio_bool = true;

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

        // set listener for the geoObjects
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);
    }

    /**
     * Carga y establece la vista de la actividad. Ademas, resuelve referencias a elementos visuales de dicha vista,
     * asocia un escuchador de clicks a algunos de ellos y fija el fondo para una de las vistas.
     */
    private void loadViewFromXML() {
        setContentView(R.layout.camera_with_osmaps);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        mShowGallery = (Button) findViewById(R.id.showGalleryButton);
        mShowGallery.setOnClickListener(this);

        mShowMap = (ImageButton) findViewById(R.id.imageButton1);
        mShowMap.setOnClickListener(this);

        baseDatos = new BaseDatos(this);
    }

    /**
     * Metodo encargado de devolverse al mapa o iniciar la animación dependiendo de cual ha sido el boton apretado por el
     * usuario.
     *
     * @param v la vista que fue seleccionada
     */
    @Override
    public void onClick(View v) {
        if (v == mShowGallery) {
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
        }
        else if (v == mShowMap) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            idPunto = (int) beyondarObjects.get(0).getId()-99;
            crearFab();
        }
    }

    /**
     * Metodo encargado de crear fab.
     */
    private void crearFab() {
        final boolean boolAudio = true;
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
        videoIcon = new ImageView(this);
        videoIcon.setImageResource(R.mipmap.video);

        audioIcon = new ImageView(this);
        audioIcon.setImageResource(R.mipmap.audio);

        imagenIcon = new ImageView(this);
        imagenIcon.setImageResource(R.mipmap.imagen);

        animacionIcon = new ImageView(this);
        animacionIcon.setImageResource(R.mipmap.percy);

        muteIcon = new ImageView(this);
        muteIcon.setImageResource(R.mipmap.mute);


        //Creo botones
        btn_video = itemBuilder.setContentView(videoIcon).build();
        btn_audio = itemBuilder.setContentView(audioIcon).build();
        btn_imagen = itemBuilder.setContentView(imagenIcon).build();
        btn_animacion = itemBuilder.setContentView(animacionIcon).build();

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
                    ImageView intermedio = new ImageView(CameraOSMaps.this);
                    if(audio_bool)
                    {
                        //reproduzco
                        intermedio.setImageResource(R.mipmap.audio);
                        audioIcon.setImageDrawable(intermedio.getDrawable());
                        audio_bool = false;
                    }
                    else
                    {
                        //cambio icono y stop audio
                        audioIcon.setImageDrawable(muteIcon.getDrawable());
                        audio_bool = true;
                    }

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
        if(baseDatos.existenciaPunto(idPunto, "Video") == 0 && (baseDatos.existenciaPunto(idPunto, "Audio") == 0) )
        {

            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(180)
                    .build();

        }

        //Pregunto si hay audios en la base para agregarlo o no al fab
        else if(baseDatos.existenciaPunto(idPunto, "Audio") == 0)
        {
            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_video)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(200)
                    .build();
        }
        //Pregunto si hay videos en la base para agregarlo o no al fab
        else if(baseDatos.existenciaPunto(idPunto, "Video") == 0)
        {
            //attach the sub buttons to the main button
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(btn_animacion)
                    .addSubActionView(btn_audio)
                    .addSubActionView(btn_imagen)
                    .attachTo(actionButton)
                    .setRadius(260)
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
