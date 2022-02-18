package com.futureproducts.headcount.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.futureproducts.headcount.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminpage extends AppCompatActivity {

    Button add, remove, my;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        my = findViewById(R.id.myrest);


        dref = db.child("users").child(FirebaseAuth.getInstance().getUid());
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String admintype = snapshot.child("admintype").getValue(String.class);
                    if(admintype.matches("admin")){
//                        add.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(adminpage.this, addactivity.class);
                startActivity(go);

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(adminpage.this, removeactivity.class);
                startActivity(go);


            }
        });

        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(adminpage.this, myresturants.class);
                startActivity(go);
                finish();

            }
        });



    }
}