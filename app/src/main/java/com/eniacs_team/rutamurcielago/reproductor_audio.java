package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import static android.R.attr.orientation;
import static android.os.Looper.prepare;
import static com.eniacs_team.rutamurcielago.R.mipmap.audio;

public class reproductor_audio extends AppCompatActivity {
    SeekBar seekBar;
    ImageButton reproductor;
    TextView texto;

    private static MediaPlayer mediaPlayer;
    private BaseDatos baseDatos;

    Handler handler;
    Runnable runnable;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_audio);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(intent.hasExtra("id")){
            id = Integer.parseInt(extras.getString("id"));// id del punto.
        }else{
            onBackPressed();
        }


        handler = new Handler();

        baseDatos = new BaseDatos(this);
        baseDatos.cargarBase();

        seekBar = (SeekBar)findViewById(R.id.sb_reproductor);
        reproductor = (ImageButton) findViewById(R.id.btn_reproductor);
        texto = (TextView) findViewById(R.id.texto_audio);



/*

        mediaPlayer = new MediaPlayer();
        //mediaPlayer.setLooping(false);

        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.seekTo(0);
                reproductor.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24px));
                seekBar.setProgress(0);


            }

        });

        try {
            AssetFileDescriptor audio = baseDatos.selectAudio(id);
            String texto_del_audio = baseDatos.selectTextoAudio(id);
            texto.setText(texto_del_audio);
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(audio.getFileDescriptor());
            audio.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            playCycle();
        } catch (IOException e) {
            Log.i("Audio", "Error " + e);
        }
*/
        playAudio();
        seekBar.setMax(mediaPlayer.getDuration());






        reproductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    reproductor.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24px));
                }else{
                    mediaPlayer.start();
                    playCycle();
                    reproductor.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24px));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }


    public void playCycle(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer != null)
        {
            mediaPlayer.start();
            reproductor.setImageDrawable(getDrawable(R.drawable.ic_pause_white_24px));
            playCycle();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }

    }

    @Override
    protected void onDestroy() {
        stopAudio();
        super.onDestroy();
    }


    /**
     * Método para tomar la accion de un item, en este caso para devolverse a la activity anterior.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void playAudio() {
        try {
            AssetFileDescriptor descriptor = baseDatos.selectAudio(id);
            mPlayerBuilder();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor());
            descriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.i("Audio", "Error " + e);
        }
    }

    private void stopAudio() {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            handler.removeCallbacks(runnable);
            seekBar = null;
            mediaPlayer = null;
        }
    }

    private void mPlayerBuilder() {
        //Creo reproductor de audio
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //Listener para que se borre cuando termine de reproducirse audio
            MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.seekTo(0);
                    reproductor.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_white_24px));
                    seekBar.setProgress(0);
                }
            };
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    @Override
    public void onBackPressed() {
        stopAudio();
        super.onBackPressed();
    }


}
