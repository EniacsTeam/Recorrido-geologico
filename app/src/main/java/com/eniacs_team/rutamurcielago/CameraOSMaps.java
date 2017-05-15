package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.world.World;

/**
 * Esta actividad proporciona una camara de realidad aumentada que permite sobreponer elementos digitales sobre el mundo real.
 *
 * @author EniacsTeam
 */
public class CameraOSMaps extends FragmentActivity implements OnClickListener{

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;

    private Button mShowMap;

    private AnimationDrawable mouthAnimation;
    private ImageButton charButton;

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

        charButton = (ImageButton) findViewById(R.id.imageButton);
        charButton.setOnClickListener(this);

        ImageView mouthImage = (ImageView) findViewById(R.id.imageView);
        mouthImage.setBackgroundResource(R.drawable.mouth_anim);
        mouthAnimation = (AnimationDrawable) mouthImage.getBackground();
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (v == charButton) {
            mouthAnimation.start();
        }
    }


}
