package com.example.lemalabsample;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity14 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main14);
        VideoView videoView =(VideoView)findViewById(R.id.vdVw);

        VideoView videoView1 =(VideoView)findViewById(R.id.vdVw1);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        MediaController mediaController1= new MediaController(this);
        mediaController1.setAnchorView(videoView1);
        //Location of Media File
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        Uri uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView1.setMediaController(mediaController1);
        videoView1.setVideoURI(uri1);
        videoView1.requestFocus();
        // videoView1.start();
    }
}
