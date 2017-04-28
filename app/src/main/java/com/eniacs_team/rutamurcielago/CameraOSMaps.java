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

public class CameraOSMaps extends FragmentActivity implements OnClickListener{

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;

    private Button mShowMap;

    private AnimationDrawable mouthAnimation;
    private ImageButton charButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViewFromXML();

        ImageView mouthImage = (ImageView) findViewById(R.id.imageView);
        mouthImage.setBackgroundResource(R.drawable.mouth_anim);
        mouthAnimation = (AnimationDrawable) mouthImage.getBackground();

        charButton = (ImageButton) findViewById(R.id.imageButton);
        charButton.setOnClickListener(this);

        // We create the world and fill it
        mWorld = CustomWorldHelper.generateObjects(this);

        mBeyondarFragment.setWorld(mWorld);
    }

    private void loadViewFromXML() {
        setContentView(R.layout.camera_with_osmaps);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        mShowMap = (Button) findViewById(R.id.showMapButton);
        mShowMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mShowMap) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (v == charButton) {
            mouthAnimation.start();
        }
    }


}
