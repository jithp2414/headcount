package com.futureproducts.headcount.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.futureproducts.headcount.R;
import com.futureproducts.headcount.model.resturantsmodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class viewresturantsactivity extends AppCompatActivity {

    ImageView logo;
    TextView headcount, original, covid, current, name;
    EditText ecovid;
    Button update;
    String key;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewresturantsactivity);

        key = getIntent().getStringExtra("rid");
        logo = findViewById(R.id.rlogo);
        headcount = findViewById(R.id.headcount);
        original = findViewById(R.id.vieworiginal);
        covid = findViewById(R.id.covidtotal);
        current = findViewById(R.id.viewcurrent);
        ecovid = findViewById(R.id.editcovid);
        update = findViewById(R.id.updatebtn);
        name = findViewById(R.id.rname);


        calldata();




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = ecovid.getText().toString();
                if(count.matches("")){
                    Toast.makeText(viewresturantsactivity.this, "Please enter count", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference uref = db.child("resturants").child(key);
                    uref.child("headcount").setValue(count);
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(key)
                            .update("headcount", count);
                    calldata();
                }
            }
        });

    }

    private void calldata() {
        DatabaseReference dref = db.child("resturants").child(key);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    resturantsmodel data = snapshot.getValue(resturantsmodel.class);
                    Log.d("TAG", "checkdata"+data.getResturantname());
                    Picasso.get().load(data.getLogo()).into(logo);
                    headcount.setText(data.getHeadcount());
                    original.setText(data.getOriginal());
                    covid.setText("/"+data.getCovid());
                    name.setText(data.getResturantname());
                    int a = Integer.parseInt(data.getCovid());
                    int b = Integer.parseInt(data.getHeadcount());
                    int c = b*100;
                    int d = c/a;
                    current.setText(String.valueOf(d)+"%");
                    ecovid.setText(data.getHeadcount());

                    Log.d("TAG", "checkpercentage"+a+" "+b+" "+" "+c+" "+d);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}