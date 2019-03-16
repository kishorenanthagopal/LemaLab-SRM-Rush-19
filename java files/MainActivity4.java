package com.example.lemalabsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity4 extends AppCompatActivity {

    private ImageButton studentbtn,collegebtn,professionalbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        studentbtn = (ImageButton)findViewById(R.id.student);
        collegebtn=(ImageButton)findViewById(R.id.college);
        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity5.class);
                startActivity(intent1);

            }
        });
        collegebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity6.class);
                startActivity(intent1);

            }
        });






    }
}
