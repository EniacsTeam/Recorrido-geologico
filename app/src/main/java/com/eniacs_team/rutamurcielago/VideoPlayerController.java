package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Francisco on 6/8/2017.
 */

public class VideoPlayerController extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_view);

        VideoView videoView = (VideoView) findViewById(R.id.VideoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
// Set video link (mp4 format )
        Uri video = Uri.parse("mp4 video link");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.start();

    }
}