package com.example.lemalabsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity15 extends AppCompatActivity {

    private EditText addresskish;
    private Button submit;
   ImageView imageView;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main15);

        addresskish= (EditText) findViewById(R.id.desc);
        imageView = (ImageView) findViewById(R.id.camersview);
        submit = (Button)findViewById(R.id.camera);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }
}
