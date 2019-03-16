package com.example.lemalabsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity8 extends AppCompatActivity {
    public Button gobtn;
    public EditText pin;
    public String pinnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        pin=(EditText)findViewById(R.id.editText);
        gobtn=(Button)findViewById(R.id.go);

        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pinnumber=pin.getText().toString();
                if(!pinnumber.equals("1234")){
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity9.class);
                    startActivity(intent);

                }

            }
        });


    }
}
