package com.futureproducts.headcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.futureproducts.headcount.admin.adminpage;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button user, admin;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.enter);
        admin = findViewById(R.id.admin);


        DatabaseReference dref = db.child("users").child(FirebaseAuth.getInstance().getUid());
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String type = snapshot.child("usertype").getValue(String.class);
                    if(type.matches("admin")){
                        admin.setVisibility(View.VISIBLE);

                    }else{
                        admin.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                double lat = intent.getDoubleExtra("lat", 0);
                double lng = intent.getDoubleExtra("lng", 0);



//                Intent go = new Intent(MainActivity.this, MapsActivity.class);
//                go.putExtra("lat", lat);
//                go.putExtra("lng", lng);
//                startActivity(go);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(MainActivity.this, adminpage.class);
                startActivity(go);
            }
        });


    }
}