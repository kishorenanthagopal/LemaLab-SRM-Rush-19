package com.example.lemalabsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class MainActivity12 extends AppCompatActivity {
    Button rlq,rlv,rlt,rlc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);

        rlq = (Button)findViewById(R.id.roboqueries);
        rlv = (Button)findViewById(R.id.robovideo);
        rlt = (Button) findViewById(R.id.robotexttut);
        rlc = (Button) findViewById(R.id.robosubmit);

        VideoView videoView1 =(VideoView)findViewById(R.id.vdVw1);
        //Set MediaController  to enable play, pause, forward, etc options.

        MediaController mediaController1= new MediaController(this);
        mediaController1.setAnchorView(videoView1);
        //Location of Media File
      Uri uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
        //Starting VideView By Setting MediaController and URI

        videoView1.setMediaController(mediaController1);
        videoView1.setVideoURI(uri1);
        videoView1.requestFocus();

        rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity13.class);
                startActivity(intent);
            }
        });

        rlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity14.class);
                startActivity(intent);
            }
        });
        rlq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivitychat.class);
                startActivity(intent);
            }
        });
        rlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity15.class);
                startActivity(intent);
            }
        });
        // videoView1.start();
    }
}