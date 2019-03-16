package com.example.lemalabsample;



import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity9 extends AppCompatActivity {

    ViewFlipper view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        VideoView videoView =(VideoView)findViewById(R.id.vdVw);

        VideoView videoView1 =(VideoView)findViewById(R.id.vdVw1);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        MediaController mediaController1= new MediaController(this);
        mediaController.setAnchorView(videoView1);
        //Location of Media File
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        Uri uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView1.setMediaController(mediaController1);
        videoView1.setVideoURI(uri1);
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
