package com.example.lemalabsample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    EditText first,middle,last,mobile1,mobile2,dateob;


    Button next11,prev1;
    private DatabaseReference myref;
    String text=MainActivity2.kishore;
    private ProgressBar spinner;
    private static int splash_time = 2000;
    public static String a,b,c,e,d,f,g;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);




        first = (EditText)findViewById(R.id.firstname);
        middle = (EditText)findViewById(R.id.middlename);
        last = (EditText)findViewById(R.id.lastname);
        mobile1 = (EditText)findViewById(R.id.mobilenumber1);
        mobile2= (EditText)findViewById(R.id.mobilenumber2);
        dateob = (EditText)findViewById(R.id.dateofbirth);
        spinner=(ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);


        next11=(Button) findViewById(R.id.next1);
        myref = FirebaseDatabase.getInstance().getReference();
        myref.addValueEventListener(new ValueEventListener() {


            public String deviceName = Devicename.getDeviceName();




            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spinner.setVisibility(View.VISIBLE);
               final String value = dataSnapshot.child(text.trim()).child("firstname").getValue(String.class);
                final String value1 = dataSnapshot.child(text.trim()).child("middlename").getValue(String.class);
                final String value2 = dataSnapshot.child(text.trim()).child("lastname").getValue(String.class);
                final String value3= dataSnapshot.child(text.trim()).child("mobilenumbera").getValue(String.class);
               final String value4 = (dataSnapshot.child(text.trim()).child("mobilenumberb").getValue(String.class));
                final String value5= dataSnapshot.child(text.trim()).child("dateofbirth").getValue(String.class);

                a= first.getText().toString();
                b = middle.getText().toString();
                c= last.getText().toString();
                d=mobile1.getText().toString();
                e=mobile2.getText().toString();
                f=dateob.getText().toString();
                g=deviceName.trim();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do any action here. Now we are moving to next page
                        spinner.setVisibility(View.GONE);
                        first.setText(value);
                        middle.setText(value1);
                        last.setText(value2);
                        mobile1.setText(value3);
                        mobile2.setText(value4);
                        dateob.setText(value5);
                        /* This 'finish()' is for exiting the app when back button pressed
                         *  from Home page which is ActivityHome
                         */


                    }
                }, splash_time);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }


        });

        next11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(),MainActivity4.class);

                startActivity(intent1);
            }
        });



        prev1=(Button) findViewById(R.id.previous1);
        prev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent1);
            }
        });

    }
}
