package com.example.lemalabsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity5 extends AppCompatActivity {
    public EditText name,year,per,add,cour;
    public TextView one;
    public Button ne;
    public String l,fir,las,mid,mn1,mn2,dob,dev;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        name=(EditText)findViewById(R.id.collegename);
        year=(EditText)findViewById(R.id.yearqwe);
        per=(EditText)findViewById(R.id.percent);
        add=(EditText)findViewById(R.id.schladdress);
        cour=(EditText)findViewById(R.id.courses);
        ref = FirebaseDatabase.getInstance().getReference();

        one=(TextView)findViewById(R.id.textView24);

        ne=(Button)findViewById(R.id.nextbutton);

        fir=MainActivity3.a;
        mid=MainActivity3.b;
        las=MainActivity3.c;
        mn1=MainActivity3.d;
        mn2=MainActivity3.e;
        dob=MainActivity3.f;
        dev=MainActivity3.g;


        l=MainActivity2.kishore.trim();
        ne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String colname=name.getText().toString();
                String yearpass=year.getText().toString();
                String percen=per.getText().toString();
                String address=add.getText().toString();
                String course=cour.getText().toString();

                ref.child(l).child("collegename").setValue(colname);
                ref.child(l).child("yearofpassing").setValue(yearpass);
                ref.child(l).child("percentage").setValue(percen);
                ref.child(l).child("address").setValue(address);
                ref.child(l).child("course").setValue(course);
                ref.child(l).child("DeviceName").setValue(dev);
                Intent intent1=new Intent(getApplicationContext(),MainActivity9.class);
                startActivity(intent1);
            }
        });






    }
}
