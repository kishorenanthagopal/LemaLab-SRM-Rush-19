package com.example.lemalabsample;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewFlipper;


public class MainActivity9 extends AppCompatActivity {

    ViewFlipper view;
    Button ml,iot,robo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        ml=(Button) findViewById(R.id.mlbtn);
        iot = (Button) findViewById(R.id.iotbtn);
        robo = (Button) findViewById(R.id.robobtn);



        VideoView videoView =(VideoView)findViewById(R.id.vdVw);

        VideoView videoView1 =(VideoView)findViewById(R.id.vdVw1);

        VideoView videoView2 =(VideoView)findViewById(R.id.vdVw2);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        MediaController mediaController1= new MediaController(this);
        mediaController1.setAnchorView(videoView1);
        MediaController mediaController2= new MediaController(this);
        mediaController2.setAnchorView(videoView2);
        //Location of Media File
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        Uri uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);

        Uri uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView1.setMediaController(mediaController1);
        videoView1.setVideoURI(uri1);
        videoView1.requestFocus();

        videoView2.setMediaController(mediaController2);
        videoView2.setVideoURI(uri1);
        videoView2.requestFocus();


        boolean b = videoView1.requestFocus();

        view = (ViewFlipper) findViewById(R.id.viewflipper1);

        int images[] = {R.drawable.makae,R.drawable.technology};

        view = findViewById(R.id.viewflipper1);

        //for (int i = 0; i < images.length; i++) {
        //flipperimages(images[i]);
        // }

        for (int image: images){
            flipperimages(image);
        }


        ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity10.class);
                startActivity(intent1);
            }
        });

        iot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),MainActivity11.class);
                startActivity(intent2);
            }
        });

        robo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity12.class);
                startActivity(intent);
            }
        });



    }

    public void flipperimages(int images)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(images);
        view.addView(imageView);
        view.setFlipInterval(4000);
        view.setAutoStart(true);
        view.setInAnimation(this,android.R.anim.slide_in_left);
        view.setOutAnimation(this,android.R.anim.slide_out_right);


    }
}
