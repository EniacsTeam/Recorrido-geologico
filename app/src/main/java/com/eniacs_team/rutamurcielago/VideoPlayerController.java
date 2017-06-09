package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.beyondar.android.util.location.BeyondarLocationManager;

/**
 * Created by Francisco on 6/8/2017.
 */

public class VideoPlayerController extends Activity {

    private VideoView videoView;
    private MediaController mediaController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_view);

        videoView = (VideoView) findViewById(R.id.VideoView);
        mediaController = new MediaController(this);
        playvideo();
        if (savedInstanceState != null) {
            int buffer = savedInstanceState.getInt("Buffer");
            videoView.seekTo(buffer);
            videoView.start();
        }

    }

    private void playvideo(){
        try{
            String videopath = "android.resource://" + this.getPackageName() + "/raw/test";
            Uri uri = Uri.parse(videopath);
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("Buffer",videoView.getCurrentPosition());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // To avoid unnecessary battery usage disable BeyondarLocationManager
        // when the activity goes on pause.
        if(videoView != null)
        {
            videoView = null;
        }
        if (mediaController != null)
        {
            mediaController = null;
        }
    }

}